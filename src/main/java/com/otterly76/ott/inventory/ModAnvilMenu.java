package com.otterly76.ott.inventory;


import com.otterly76.ott.block.entity.AnvilBlockEntity;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.config.RenameAndRepairCost;
import com.otterly76.ott.inventory.state.AnvilMenuState;
import com.otterly76.ott.inventory.state.BuiltInAnvilMenu;
import com.otterly76.ott.inventory.state.VanillaAnvilMenu;
import com.otterly76.ott.mixin.common.AnvilMenuAccessor;
import com.otterly76.ott.mixin.common.ItemCombinerMenuAccessor;
import com.otterly76.ott.util.data.ComponentDecomposer;
import com.otterly76.ott.util.data.FormattedStringDecomposer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModAnvilMenu extends AnvilMenu {
    private final Container container;
    private final AnvilMenuState builtInAnvilState;
    private final AnvilMenuState vanillaAnvilState;

    public ModAnvilMenu(int id, Inventory inventory) {
        super(id, inventory);
        this.container = new SimpleContainer();
        this.builtInAnvilState = new BuiltInAnvilMenu(inventory, ContainerLevelAccess.NULL);
        this.vanillaAnvilState = new VanillaAnvilMenu(inventory, ContainerLevelAccess.NULL);
        this.createResult();
    }

    public ModAnvilMenu(int id, Inventory inventory, AnvilBlockEntity blockEntity, ContainerLevelAccess containerLevelAccess) {
        super(id, inventory, containerLevelAccess);
        this.container = blockEntity;
        this.builtInAnvilState = new BuiltInAnvilMenu(inventory, containerLevelAccess);
        this.vanillaAnvilState = new VanillaAnvilMenu(inventory, containerLevelAccess);
        this.initializeSlots(blockEntity);
        this.createResult();
    }

    private void initializeSlots(AnvilBlockEntity blockEntity) {
        for (int i = 0; i < blockEntity.getContainerSize(); i++) {
            this.inputSlots.setItem(i, blockEntity.getItem(i));
        }
        ((SimpleContainer) this.inputSlots).addListener(($) -> {
            for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                blockEntity.setItem(i, this.inputSlots.getItem(i));
            }
            blockEntity.setChanged();
        });
        this.resultSlots.setItem(0, blockEntity.getResult().getFirst());
    }

    public @NotNull MenuType<?> getType() {
        return ModMenuTypes.ANVIL_MENU_TYPE.get();
    }

    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }

    protected boolean mayPickup(Player player, boolean hasStack) {
        return (player.getAbilities().instabuild || player.experienceLevel >= this.getCost()) && this.getCost() >= 0;
    }

    public void createResult() {
        if (this.builtInAnvilState != null && this.vanillaAnvilState != null) {
            ItemStack left = this.inputSlots.getItem(0);
            ItemStack right = this.inputSlots.getItem(1);
            this.builtInAnvilState.init(left, right, ((AnvilMenuAccessor) this).ott$getItemName());
            this.vanillaAnvilState.init(left, right, ((AnvilMenuAccessor) this).ott$getItemName());
            this.builtInAnvilState.fillResultSlots();
            this.vanillaAnvilState.fillResultSlots();
            if (!AnvilMenuState.equals(this.builtInAnvilState, this.vanillaAnvilState)) {
                super.createResult();
            } else {
                this.createResult(left, right, ((AnvilMenuAccessor) this).ott$getItemName());
            }
            if (this.container instanceof AnvilBlockEntity be) {
                be.getResult().set(0, this.resultSlots.getItem(0));
            }
        }
    }

    private void createResult(ItemStack leftInput, ItemStack rightInput, String itemName) {
        this.setCost(1);
        if (!leftInput.isEmpty() && EnchantmentHelper.canStoreEnchantments(leftInput)) {
            ItemStack output = leftInput.copy();
            ItemEnchantments.Mutable leftEnchantments = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(output));
            int baseRepairCost = leftInput.getOrDefault(DataComponents.REPAIR_COST, 0) + (rightInput.isEmpty() ? 0 : rightInput.getOrDefault(DataComponents.REPAIR_COST, 0));
            baseRepairCost = OttConfig.ANVILS.PRIOR_WORK_PENALTY.PRIOR_WORK_PENALTY.get().operator.applyAsInt(baseRepairCost);
            ((AnvilMenuAccessor) this).ott$setRepairItemCountCost(0);
            boolean isBook = false;
            int repairOperationCost = 0;
            int enchantOperationCost = 0;
            int renameOperationCost = 0;
            if (!rightInput.isEmpty()) {
                isBook = rightInput.has(DataComponents.STORED_ENCHANTMENTS);
                if (output.isDamageableItem() && output.getItem().isValidRepairItem(leftInput, rightInput)) {
                    int l2 = (int)Math.min(output.getDamageValue(), Math.floor((double)output.getMaxDamage() * OttConfig.ANVILS.COSTS.REPAIR_WITH_MATERIAL_RESTORED_DURABILITY.get()));
                    if (l2 <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.setCost(0);
                        return;
                    }

                    int repairMaterials;
                    for(repairMaterials = 0; l2 > 0 && repairMaterials < rightInput.getCount(); ++repairMaterials) {
                        int j3 = output.getDamageValue() - l2;
                        output.setDamageValue(j3);
                        repairOperationCost += OttConfig.ANVILS.COSTS.REPAIR_WITH_MATERIAL_UNIT_COST.get();
                        l2 = (int)Math.min(output.getDamageValue(), Math.floor((double)output.getMaxDamage() * OttConfig.ANVILS.COSTS.REPAIR_WITH_MATERIAL_RESTORED_DURABILITY.get()));
                    }

                    ((AnvilMenuAccessor) this).ott$setRepairItemCountCost(repairMaterials);
                } else {
                    if (!isBook && (!output.is(rightInput.getItem()) || !output.isDamageableItem())) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.setCost(0);
                        return;
                    }

                    if (output.isDamageableItem() && !isBook) {
                        int l = leftInput.getMaxDamage() - leftInput.getDamageValue();
                        int i1 = rightInput.getMaxDamage() - rightInput.getDamageValue();
                        int j1 = i1 + (int)Math.floor((double)output.getMaxDamage() * OttConfig.ANVILS.COSTS.REPAIR_WITH_OTHER_ITEM_BONUS_DURABILITY.get());
                        int k1 = l + j1;
                        int l1 = output.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < output.getDamageValue()) {
                            output.setDamageValue(l1);
                            repairOperationCost += OttConfig.ANVILS.COSTS.REPAIR_WITH_OTHER_ITEM_COST.get();
                        }
                    }

                    ItemEnchantments rightEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(rightInput);
                    boolean itemWithCompatibleEnchantment = false;
                    boolean itemWithIncompatibleEnchantment = false;

                    for(Holder<Enchantment> rightHolder : rightEnchantments.keySet()) {
                        Enchantment rightEnchantment = rightHolder.value();
                        int leftEnchantmentLevel = leftEnchantments.getLevel(rightHolder);
                        int enchantmentLevel = rightEnchantments.getLevel(rightHolder);
                        enchantmentLevel = leftEnchantmentLevel == enchantmentLevel ? enchantmentLevel + 1 : Math.max(enchantmentLevel, leftEnchantmentLevel);
                        ContainerMenuHelper.EnchantmentCompatibilityResult compatibilityResult = ContainerMenuHelper.checkEnchantmentCompatibility(rightHolder, leftInput, leftEnchantments.keySet(), this.player.getAbilities().instabuild);
                        enchantOperationCost += compatibilityResult.costAddition();

                        if (!compatibilityResult.compatible()) {
                            if (repairOperationCost <= 0) {
                                itemWithIncompatibleEnchantment = true;
                            }
                        } else {
                            itemWithCompatibleEnchantment = true;
                            if (enchantmentLevel > rightEnchantment.getMaxLevel()) {
                                enchantmentLevel = rightEnchantment.getMaxLevel();
                            }

                            int maxLevel = Math.max(leftEnchantments.getLevel(rightHolder), rightEnchantments.getLevel(rightHolder));
                            maxLevel = Math.max(maxLevel, enchantmentLevel);
                            if (maxLevel != enchantmentLevel) {
                                enchantmentLevel = maxLevel;
                            }

                            int rarityCostMultiplier = rightEnchantment.getAnvilCost();
                            if (isBook && OttConfig.ANVILS.COSTS.HALVED_BOOK_COSTS.get()) {
                                rarityCostMultiplier = Math.max(1, rarityCostMultiplier / 2);
                            }

                            int oldEnchantmentLevel = leftEnchantments.getLevel(rightHolder);
                            leftEnchantments.set(rightHolder, enchantmentLevel);
                            if (oldEnchantmentLevel != 0 || oldEnchantmentLevel != enchantmentLevel) {
                                enchantOperationCost += rarityCostMultiplier * enchantmentLevel;
                            }

                            if (leftInput.getCount() > 1 && !this.player.getAbilities().instabuild) {
                                this.resultSlots.setItem(0, ItemStack.EMPTY);
                                this.setCost(-1);
                                return;
                            }
                        }
                    }

                    if (itemWithIncompatibleEnchantment && !itemWithCompatibleEnchantment) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.setCost(0);
                        return;
                    }
                }
            }

            boolean hasRenamedItem = false;
            if (ComponentDecomposer.getStringLength(itemName) == 0) {
                if (leftInput.has(DataComponents.CUSTOM_NAME)) {
                    renameOperationCost = OttConfig.ANVILS.COSTS.FREE_RENAMES.get().filter.test(leftInput) ? 0 : 1;
                    hasRenamedItem = true;
                    output.remove(DataComponents.CUSTOM_NAME);
                }
            } else if (!itemName.equals(ComponentDecomposer.toFormattedString(leftInput.getHoverName()))) {
                renameOperationCost = OttConfig.ANVILS.COSTS.FREE_RENAMES.get().filter.test(leftInput) ? 0 : 1;
                hasRenamedItem = true;
                output.set(DataComponents.CUSTOM_NAME, ComponentDecomposer.toFormattedComponent(itemName));
            }

            if (isBook && !output.getItem().isEnchantable(output) && !output.is(Items.ENCHANTED_BOOK)) {
                output = ItemStack.EMPTY;
            }

            int allOperationsCost = enchantOperationCost + repairOperationCost + renameOperationCost;
            if (allOperationsCost == 0) {
                this.setCost(0);
                if (!hasRenamedItem) {
                    output = ItemStack.EMPTY;
                }
            } else if (enchantOperationCost == 0 && OttConfig.ANVILS.PRIOR_WORK_PENALTY.RENAME_AND_REPAIR_COSTS.get() == RenameAndRepairCost.FIXED) {
                this.setCost(allOperationsCost);
            } else {
                this.setCost(baseRepairCost + allOperationsCost);
            }

            int maxAnvilRepairCost = OttConfig.ANVILS.COSTS.TOO_EXPENSIVE_LIMIT.get();
            boolean hasNoLimit = maxAnvilRepairCost == -1;
            if (hasNoLimit) {
                maxAnvilRepairCost = 40;
            }

            if (this.getCost() >= maxAnvilRepairCost) {
                if (enchantOperationCost == 0 && OttConfig.ANVILS.PRIOR_WORK_PENALTY.RENAME_AND_REPAIR_COSTS.get() == RenameAndRepairCost.LIMITED) {
                    this.setCost(maxAnvilRepairCost - 1);
                } else if (!hasNoLimit && !this.player.getAbilities().instabuild) {
                    output = ItemStack.EMPTY;
                }
            }

            if (!output.isEmpty()) {
                int outputRepairCost = output.getOrDefault(DataComponents.REPAIR_COST, 0);
                if (!rightInput.isEmpty() && outputRepairCost < rightInput.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                    outputRepairCost = rightInput.getOrDefault(DataComponents.REPAIR_COST, 0);
                }

                if (allOperationsCost > 0 && (enchantOperationCost > 0 && (!isBook || !leftInput.is(Items.ENCHANTED_BOOK) || !OttConfig.ANVILS.PRIOR_WORK_PENALTY.PENALTY_FREE_ENCHANTS_FOR_BOOKS.get()) || !OttConfig.ANVILS.PRIOR_WORK_PENALTY.PENALTY_FREE_RENAMES_AND_REPAIRS.get())) {
                    outputRepairCost = AnvilMenu.calculateIncreasedRepairCost(outputRepairCost);
                }

                if (outputRepairCost > 0) {
                    output.set(DataComponents.REPAIR_COST, outputRepairCost);
                }

                EnchantmentHelper.setEnchantments(output, leftEnchantments.toImmutable());
            }

            this.resultSlots.setItem(0, output);
            this.broadcastChanges();
        } else {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.setCost(0);
        }

    }

    public void setCost(int cost) {
        this.setData(0, cost);
    }

    public void removed(@NotNull Player player) {
        ContainerLevelAccess containerLevelAccess = ((ItemCombinerMenuAccessor) this).ott$getAccess();
        ((ItemCombinerMenuAccessor) this).ott$setAccess(ContainerLevelAccess.NULL);
        super.removed(player);
        ((ItemCombinerMenuAccessor) this).ott$setAccess(containerLevelAccess);
    }

    public boolean setItemName(@NotNull String newName) {
        newName = FormattedStringDecomposer.filterText(newName);
        if (ComponentDecomposer.getStringLength(newName) <= 50 && !Objects.equals(newName, ((AnvilMenuAccessor) this).ott$getItemName())) {
            ((AnvilMenuAccessor) this).ott$setItemName(newName.trim());
            if (this.getSlot(2).hasItem()) {
                ItemStack itemStack = this.getSlot(2).getItem();
                setFormattedItemName(((AnvilMenuAccessor) this).ott$getItemName(), itemStack);
            }

            this.createResult();
            return true;
        } else {
            return false;
        }
    }

    public static void setFormattedItemName(String newName, ItemStack itemStack) {
        Component component = ComponentDecomposer.toFormattedComponent(newName);
        if (component.getString().isEmpty()) {
            itemStack.remove(DataComponents.CUSTOM_NAME);
        } else {
            itemStack.set(DataComponents.CUSTOM_NAME, component);
        }

    }
}
