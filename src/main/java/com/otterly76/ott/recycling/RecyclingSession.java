package com.otterly76.ott.recycling;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.network.recycling.ClientboundRecipeListPacket;
import com.otterly76.ott.network.recycling.ClientboundRecipeSelectRequestPacket;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecyclingSession {
    public final ServerPlayer player;

    public List<RecyclingRecipe> currentRecipes = new ArrayList<>();
    public RecyclingRecipe currentRecipe = null;
    public int experience = 0;
    public int experienceType;
    public ItemStack currentStack = ItemStack.EMPTY;
    public int page = 0;
    public RecyclingStatus status = RecyclingStatus.BLANK;

    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> experience;
                case 1 -> experienceType;
                case 2 -> status.getIndex();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> experience = value;
                case 1 -> experienceType = value;
                case 2 -> status = RecyclingStatus.byIndex(value);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private final ItemStackHandler inputHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            computeRecipes();
            if (!player.level().isClientSide()) {
                if (currentStack.getItem() != getStackInSlot(0).getItem() && !getStackInSlot(0).isEmpty()) {
                    for (int i = 0; i < outputHandler.getSlots(); i++) {
                        ItemStack outputStack = outputHandler.getStackInSlot(i);
                        if (!outputStack.isEmpty()) {
                            player.getInventory().placeItemBackInInventory(outputStack);
                            outputHandler.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    }
                }
                currentStack = getStackInSlot(0);
                sendRecipePage();
            }
        }
    };

    private final ItemStackHandler outputHandler = new ItemStackHandler(9) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (!player.level().isClientSide()) {
                handleRecipeSelection(currentRecipe);
            }
        }
    };

    public RecyclingSession(ServerPlayer player) {
        this.player = player;
        this.experienceType = OttConfig.RECYCLING.EXPERIENCE_TYPE.get() == OttConfig.Recycling.ExperienceType.LEVEL ? 1 : 0;
    }

    public void computeRecipes() {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;
        this.status = RecyclingStatus.BLANK;

        ItemStack inputStack = inputHandler.getStackInSlot(0);
        if (!RecyclingHelpers.validateInput(inputStack, this)) {
            currentRecipes.clear();
            currentRecipe = null;
            experience = 0;
            return;
        }

        List<RecipeHolder<?>> recipes = RecyclingHelpers.findRecipe(serverLevel, inputStack, this);

        if (!recipes.isEmpty() || inputStack.is(Items.TIPPED_ARROW)
                || (OttConfig.RECYCLING.ALLOW_ENCHANTED_ITEMS.getAsBoolean()
                && inputStack.get(DataComponents.ENCHANTMENTS) != ItemEnchantments.EMPTY)) {
            this.status = RecyclingStatus.BLANK;
            this.experience = OttConfig.RECYCLING.EXPERIENCE.getAsInt();
            this.experienceType = OttConfig.RECYCLING.EXPERIENCE_TYPE.get() == OttConfig.Recycling.ExperienceType.LEVEL ? 1 : 0;
        }

        var outputs = RecyclingHelpers.getOutputs(inputStack, recipes, this);
        if (!outputs.getB()) return;

        if (OttConfig.RECYCLING.PRIORITIZE_VANILLA_INGREDIENTS.getAsBoolean()) {
            this.currentRecipes = new ArrayList<>(outputs.getA().stream()
                    .sorted(Comparator.comparingInt(this::countVanillaIngredients).reversed())
                    .toList());
        } else {
            this.currentRecipes = outputs.getA();
        }

        if (!currentRecipes.isEmpty()) {
            PacketDistributor.sendToPlayer(player, new ClientboundRecipeSelectRequestPacket());
            if (!hasRecipe()) {
                this.status = RecyclingStatus.NO_SUITABLE_OUTPUT_SLOT;
            } else if (!hasEnoughExperience()) {
                this.status = RecyclingStatus.NOT_ENOUGH_EXP;
            }
        } else if (this.status == RecyclingStatus.BLANK && !inputStack.isEmpty()) {
            this.status = RecyclingStatus.NO_RECIPE_FOUND;
        }
    }

    public void sendRecipePage() {
        int fromIndex = page * 7;
        if (fromIndex >= currentRecipes.size()) fromIndex = 0;
        int toIndex = Math.min(fromIndex + 7, currentRecipes.size());
        PacketDistributor.sendToPlayer(player, new ClientboundRecipeListPacket(
                new ArrayList<>(currentRecipes.subList(fromIndex, toIndex)),
                currentRecipes.size(),
                true
        ));
    }

    public void updatePage(int newPage) {
        this.page = newPage;
        int fromIndex = page * 7;
        if (fromIndex >= currentRecipes.size()) fromIndex = 0;
        int toIndex = Math.min(fromIndex + 7, currentRecipes.size());
        PacketDistributor.sendToPlayer(player, new ClientboundRecipeListPacket(
                new ArrayList<>(currentRecipes.subList(fromIndex, toIndex)),
                currentRecipes.size(),
                false
        ));
    }

    public void handleRecipeSelection(RecyclingRecipe recipe) {
        this.currentRecipe = recipe;
        if (!hasRecipe()) {
            this.status = inputHandler.getStackInSlot(0).isEmpty() ? RecyclingStatus.BLANK : RecyclingStatus.NO_SUITABLE_OUTPUT_SLOT;
        } else {
            this.status = hasEnoughExperience() ? RecyclingStatus.BLANK : RecyclingStatus.NOT_ENOUGH_EXP;
        }
    }

    public void handleUncraftButtonClicked(boolean hasShiftDown) {
        if (hasShiftDown) {
            while (hasRecipe() && hasEnoughExperience()) {
                processUncraft();
            }
        } else {
            if (hasRecipe() && hasEnoughExperience()) {
                processUncraft();
            }
        }
    }

    private void processUncraft() {
        if (currentRecipe == null) return;
        List<ItemStack> outputs = currentRecipe.getOutputs();

        for (int i = 0; i < outputs.size(); i++) {
            ItemStack output = outputs.get(i);
            if (i < outputHandler.getSlots()) {
                ItemStack slotStack = outputHandler.getStackInSlot(i);
                if (slotStack.isEmpty()) {
                    outputHandler.setStackInSlot(i, output.copy());
                } else if (ItemStack.isSameItemSameComponents(slotStack, output) && slotStack.getCount() + output.getCount() <= slotStack.getMaxStackSize()) {
                    slotStack.grow(output.getCount());
                    outputHandler.setStackInSlot(i, slotStack);
                }
            }
        }

        if (OttConfig.RECYCLING.EXPERIENCE_TYPE.get() == OttConfig.Recycling.ExperienceType.POINT) {
            player.giveExperiencePoints(-experience);
        } else {
            player.giveExperiencePoints(-calculateBaseXpFromLevel(experience));
        }

        inputHandler.extractItem(0, currentRecipe.getInput().getCount(), false);
    }

    public boolean hasRecipe() {
        if (currentRecipes.isEmpty() || currentRecipe == null) return false;
        ItemStack inputStack = inputHandler.getStackInSlot(0);
        if (inputStack.getCount() < currentRecipe.getInput().getCount()) return false;

        List<ItemStack> results = currentRecipe.getOutputs();
        for (ItemStack result : results) {
            if (cannotInsertIntoOutput(result)) return false;
        }
        return checkOutputSlots(results);
    }

    private boolean cannotInsertIntoOutput(ItemStack result) {
        for (int i = 0; i < outputHandler.getSlots(); i++) {
            ItemStack slotStack = outputHandler.getStackInSlot(i);
            if (slotStack.isEmpty() || (ItemStack.isSameItemSameComponents(slotStack, result) && slotStack.getCount() + result.getCount() <= slotStack.getMaxStackSize())) {
                return false;
            }
        }
        return true;
    }

    private boolean checkOutputSlots(List<ItemStack> results) {
        int count = results.size();
        int emptyCount = 0;
        for (int i = 0; i < outputHandler.getSlots(); i++) {
            ItemStack slotStack = outputHandler.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                for (ItemStack result : results) {
                    if (slotStack.getItem() == result.getItem() && slotStack.getCount() + result.getCount() <= 64) {
                        emptyCount++;
                    }
                }
            } else {
                emptyCount++;
            }
        }
        return emptyCount >= count;
    }

    public boolean hasEnoughExperience() {
        if (OttConfig.RECYCLING.EXPERIENCE_TYPE.get() == OttConfig.Recycling.ExperienceType.POINT) {
            return player.totalExperience >= experience || player.isCreative();
        } else {
            return player.experienceLevel >= experience || player.isCreative();
        }
    }

    private int countVanillaIngredients(RecyclingRecipe recipe) {
        int count = 0;
        for (ItemStack stack : recipe.getOutputs()) {
            var key = stack.getItemHolder().getKey();
            if (key != null && key.location().getNamespace().equals("minecraft")) count++;
        }
        return count;
    }

    private int calculateBaseXpFromLevel(int level) {
        if (level <= 16) return level * level + 6 * level;
        if (level <= 31) return (int) (2.5 * level * level - 40.5 * level + 360);
        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }

    public ItemStackHandler getInputHandler() {
        return inputHandler;
    }

    public ItemStackHandler getOutputHandler() {
        return outputHandler;
    }
}
