package com.otterly76.ott.recycling;

import com.ldtteam.domumornamentum.client.model.data.MaterialTextureData;
import com.ldtteam.domumornamentum.recipe.architectscutter.ArchitectsCutterRecipe;
import com.mojang.logging.LogUtils;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class RecyclingHelpers {

    public static boolean validateInput(ItemStack inputStack, RecyclingSession session) {
        OttConfig.Recycling cfg = OttConfig.RECYCLING;

        if (inputStack.isEmpty()) {
            session.status = RecyclingStatus.BLANK;
            return false;
        }
        if (inputStack.getDamageValue() > 0 && !cfg.ALLOW_DAMAGED.getAsBoolean()) {
            session.status = RecyclingStatus.DAMAGED_ITEM;
            return false;
        }
        if (cfg.isItemBlacklisted(inputStack)) {
            session.status = RecyclingStatus.RESTRICTED_BY_CONFIG;
            return false;
        }
        if (cfg.isItemWhitelisted(inputStack)) {
            session.status = RecyclingStatus.RESTRICTED_BY_CONFIG;
            return false;
        }
        if (!cfg.isEnchantedItemsAllowed(inputStack) && !inputStack.has(DataComponents.TRIM)) {
            session.status = RecyclingStatus.ENCHANTED_ITEM;
            return false;
        }
        if (inputStack.getItem() == Items.SHULKER_BOX && inputStack.get(DataComponents.CONTAINER) != ItemContainerContents.EMPTY) {
            session.status = RecyclingStatus.NOT_EMPTY_SHULKER;
            return false;
        }
        if (inputStack.getItem() == Items.ENCHANTED_BOOK) {
            session.status = RecyclingStatus.NO_RECIPE_FOUND;
            return false;
        }
        return true;
    }

    public static List<RecipeHolder<?>> findRecipe(ServerLevel serverLevel, ItemStack input, RecyclingSession session) {
        OttConfig.Recycling cfg = OttConfig.RECYCLING;
        ItemStack inputStack = input.copy();
        inputStack.remove(DataComponents.CUSTOM_NAME);

        return serverLevel.getRecipeManager().getRecipes().stream().filter(recipeHolder -> {
            if (!recipeHolder.id().getNamespace().equals("minecraft")
                    && BuiltInRegistries.ITEM.getKey(inputStack.getItem()).getNamespace().equals("minecraft")
                    && cfg.PREVENT_MODDED_INGREDIENTS.getAsBoolean()) {
                return false;
            }

            if (recipeHolder.value() instanceof ShapedRecipe shapedRecipe) {
                return validateRecipe(shapedRecipe.result, inputStack, session);
            }
            if (recipeHolder.value() instanceof ShapelessRecipe shapelessRecipe) {
                return validateRecipe(shapelessRecipe.result, inputStack, session);
            }
            if (recipeHolder.value() instanceof ShulkerBoxColoring) {
                return inputStack.is(Tags.Items.SHULKER_BOXES) && !inputStack.is(Items.SHULKER_BOX);
            }
            if (recipeHolder.value() instanceof SmithingTransformRecipe smithingTransformRecipe) {
                if (!cfg.ALLOW_UN_SMITHING.getAsBoolean()
                        || (inputStack.get(DataComponents.ENCHANTMENTS) != ItemEnchantments.EMPTY && cfg.OUTPUT_ENCHANTED_BOOK.getAsBoolean())) {
                    return false;
                }
                return validateSmithingRecipe(smithingTransformRecipe, inputStack);
            }
            if (recipeHolder.value() instanceof SmithingTrimRecipe smithingTrimRecipe) {
                if (!cfg.ALLOW_UN_SMITHING.getAsBoolean()) return false;
                ArmorTrim armorTrim = inputStack.get(DataComponents.TRIM);
                if (armorTrim != null) {
                    Ingredient ingredient = smithingTrimRecipe.addition;
                    var trimPatternRef = TrimPatterns.getFromTemplate(serverLevel.registryAccess(),
                            smithingTrimRecipe.template.getValues()[0].getItems().stream().toList().getFirst());
                    if (ingredient != Ingredient.EMPTY && trimPatternRef.isPresent() && armorTrim.pattern().equals(trimPatternRef.get())) {
                        return true;
                    }
                }
            }

            if (recipeHolder.value() instanceof StonecutterRecipe stonecutterRecipe) {
                return validateRecipe(stonecutterRecipe.result, inputStack, session);
            }
            if (recipeHolder.value() instanceof ArchitectsCutterRecipe architectsCutterRecipe) {
                // Compare by item type only — DO items carry per-material TEXTURE_DATA components that
                // differ from the recipe's template result, so isSameItemSameComponents always fails.
                ItemStack result = architectsCutterRecipe.getResultItem(serverLevel.registryAccess());
                return result.is(inputStack.getItem()) && inputStack.getCount() >= result.getCount();
            }

            if (session.status == RecyclingStatus.BLANK && !inputStack.isEmpty()) {
                session.status = RecyclingStatus.NO_RECIPE_FOUND;
            }
            return false;
        }).toList();
    }

    public static boolean validateRecipe(ItemStack result, ItemStack inputStack, RecyclingSession session) {
        OttConfig.Recycling cfg = OttConfig.RECYCLING;

        if (result.getItem() == inputStack.getItem() && inputStack.getCount() < result.getCount()) {
            session.status = RecyclingStatus.NOT_ENOUGH_INPUT_ITEM;
        }
        if (inputStack.get(DataComponents.ENCHANTMENTS) != ItemEnchantments.EMPTY && cfg.OUTPUT_ENCHANTED_BOOK.getAsBoolean()) {
            return false;
        }
        if (inputStack.isDamaged()) {
            return result.getItem() == inputStack.getItem() && inputStack.getCount() >= result.getCount();
        }
        if (inputStack.get(DataComponents.ENCHANTMENTS) != ItemEnchantments.EMPTY
                && cfg.ALLOW_ENCHANTED_ITEMS.getAsBoolean()
                && result.getItem() == inputStack.getItem()) {
            return true;
        }
        return ItemStack.isSameItemSameComponents(result, inputStack) && inputStack.getCount() >= result.getCount();
    }

    public static boolean validateSmithingRecipe(SmithingTransformRecipe recipe, ItemStack inputStack) {
        OttConfig.Recycling cfg = OttConfig.RECYCLING;

        if (inputStack.get(DataComponents.ENCHANTMENTS) != ItemEnchantments.EMPTY && cfg.OUTPUT_ENCHANTED_BOOK.getAsBoolean()) {
            return false;
        }
        if (inputStack.isDamaged()) {
            return inputStack.is(recipe.result.getItem()) && inputStack.getCount() >= recipe.result.getCount();
        }
        if (inputStack.get(DataComponents.ENCHANTMENTS) != ItemEnchantments.EMPTY
                && cfg.ALLOW_ENCHANTED_ITEMS.getAsBoolean()
                && recipe.result.getItem() == inputStack.getItem()) {
            return true;
        }
        return ItemStack.isSameItemSameComponents(inputStack, new ItemStack(recipe.result.getItem().builtInRegistryHolder(), recipe.result.getCount(), recipe.result.getComponentsPatch()));
    }

    public static Tuple<List<RecyclingRecipe>, Boolean> getOutputs(ItemStack inputStack, List<RecipeHolder<?>> recipes, RecyclingSession session) {
        List<RecyclingRecipe> outputs = new ArrayList<>();
        OttConfig.Recycling cfg = OttConfig.RECYCLING;

        if (inputStack.is(Items.TIPPED_ARROW)) {
            PotionContents potionContents = inputStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            RecyclingRecipe outputStack = new RecyclingRecipe(new ItemStack(inputStack.getItem().builtInRegistryHolder(), 8, inputStack.getComponentsPatch()));
            ItemStack potion = new ItemStack(Items.LINGERING_POTION);
            potion.set(DataComponents.POTION_CONTENTS, potionContents);
            outputStack.addOutput(new ItemStack(Items.ARROW, 8));
            outputStack.addOutput(potion);
            outputs.add(outputStack);
        }

        if (inputStack.get(DataComponents.ENCHANTMENTS) != ItemEnchantments.EMPTY && recipes.isEmpty() && cfg.OUTPUT_ENCHANTED_BOOK.getAsBoolean()) {
            RecyclingRecipe outputStack = new RecyclingRecipe(new ItemStack(inputStack.getItem().builtInRegistryHolder(), 1, inputStack.getComponentsPatch()));
            ItemEnchantments enchantments = inputStack.get(DataComponents.ENCHANTMENTS);
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            book.set(DataComponents.STORED_ENCHANTMENTS, enchantments);
            ItemStack output = new ItemStack(inputStack.getItem(), 1);
            output.setDamageValue(inputStack.getDamageValue());
            outputStack.addOutput(output);
            outputStack.addOutput(book);
            outputs.add(outputStack);
        }

        for (RecipeHolder<?> r : recipes) {
            if (r.value() instanceof ShulkerBoxColoring) {
                List<Ingredient> ingredients = new ArrayList<>();
                ingredients.add(Ingredient.of(Tags.Items.SHULKER_BOXES));
                ingredients.add(Ingredient.of(DyeItem.byColor(Objects.requireNonNull(((ShulkerBoxBlock) ((BlockItem) inputStack.getItem()).getBlock()).getColor()))));

                for (List<Tuple<Item, DataComponentPatch>> combo : getAllShapelessIngredientCombinations(ingredients, inputStack)) {
                    RecyclingRecipe outputStack = new RecyclingRecipe(inputStack.copyWithCount(1));
                    for (Tuple<Item, DataComponentPatch> item : combo) {
                        if (outputStack.contains(item)) {
                            outputStack.getStack(item).grow(1);
                        } else {
                            ItemStack itemStack = new ItemStack(item.getA().builtInRegistryHolder(), 1, item.getB());
                            if (itemStack.has(DataComponents.CONTAINER)) itemStack.set(DataComponents.CONTAINER, inputStack.get(DataComponents.CONTAINER));
                            if (itemStack.has(DataComponents.BUNDLE_CONTENTS)) itemStack.set(DataComponents.BUNDLE_CONTENTS, inputStack.get(DataComponents.BUNDLE_CONTENTS));
                            outputStack.addOutput(itemStack);
                        }
                    }
                    outputs.add(outputStack);
                }
            }

            if (r.value() instanceof ShapedRecipe shapedRecipe) {
                for (List<Tuple<Item, DataComponentPatch>> combo : getAllIngredientCombinations(shapedRecipe.getIngredients().stream().map(Optional::of).toList(), inputStack)) {
                    RecyclingRecipe outputStack = new RecyclingRecipe(new ItemStack(shapedRecipe.result.getItem().builtInRegistryHolder(), shapedRecipe.result.getCount(), inputStack.getComponentsPatch()));
                    addComboToOutput(combo, outputStack, inputStack, outputs);
                }
            }

            if (r.value() instanceof ShapelessRecipe shapelessRecipe) {
                List<Ingredient> ingredients = new ArrayList<>(shapelessRecipe.ingredients);
                if (inputStack.has(DataComponents.FIREWORKS)) {
                    Fireworks fireworks = inputStack.getOrDefault(DataComponents.FIREWORKS, new Fireworks(1, List.of()));
                    for (int i = 1; i < fireworks.flightDuration(); i++) {
                        ingredients.add(Ingredient.of(Items.GUNPOWDER));
                    }
                }
                for (List<Tuple<Item, DataComponentPatch>> combo : getAllShapelessIngredientCombinations(ingredients, inputStack)) {
                    RecyclingRecipe outputStack = new RecyclingRecipe(new ItemStack(shapelessRecipe.result.getItem().builtInRegistryHolder(), shapelessRecipe.result.getCount(), inputStack.getComponentsPatch()));
                    addComboToOutput(combo, outputStack, inputStack, outputs);
                }
            }

            if (r.value() instanceof SmithingTransformRecipe smithingTransformRecipe) {
                List<Optional<Ingredient>> ingredients = List.of(
                        Optional.of(smithingTransformRecipe.base),
                        Optional.of(smithingTransformRecipe.addition),
                        Optional.of(smithingTransformRecipe.template)
                );
                for (List<Tuple<Item, DataComponentPatch>> combo : getAllIngredientCombinations(ingredients, inputStack)) {
                    RecyclingRecipe outputStack = new RecyclingRecipe(new ItemStack(smithingTransformRecipe.result.getItem().builtInRegistryHolder(), 1, inputStack.getComponentsPatch()));
                    for (Tuple<Item, DataComponentPatch> item : combo) {
                        if (outputStack.contains(item)) {
                            ItemStack stack = outputStack.getStack(item);
                            if (item.getA().getDefaultInstance().isDamageableItem()) {
                                stack.set(DataComponents.DAMAGE, inputStack.get(DataComponents.DAMAGE));
                            }
                            stack.grow(1);
                            outputStack.setOutput(outputStack.indexOf(item), stack);
                        } else {
                            ItemStack itemStack = new ItemStack(item.getA().builtInRegistryHolder(), 1, item.getB());
                            if (item.getA().getDefaultInstance().isDamageableItem()) {
                                itemStack.set(DataComponents.DAMAGE, inputStack.get(DataComponents.DAMAGE));
                                if (itemStack.getOrDefault(DataComponents.DAMAGE, 0) >= itemStack.getOrDefault(DataComponents.MAX_DAMAGE, 0)) {
                                    itemStack = ItemStack.EMPTY;
                                }
                            }
                            outputStack.addOutput(itemStack);
                        }
                    }
                    outputs.add(outputStack);
                }
            }

            if (r.value() instanceof SmithingTrimRecipe smithingTrimRecipe) {
                ArmorTrim armorTrim = inputStack.get(DataComponents.TRIM);
                List<Optional<Ingredient>> ingredients = new ArrayList<>();
                Arrays.stream(smithingTrimRecipe.base.getItems())
                        .filter(holder -> inputStack.is(holder.getItem()))
                        .forEach(holder -> ingredients.add(Optional.of(Ingredient.of(holder.getItem()))));
                ingredients.add(Optional.of(smithingTrimRecipe.template));
                if (armorTrim != null) {
                    Arrays.stream(smithingTrimRecipe.addition.getItems()).filter(holder -> {
                        ResourceKey<Item> itemKey = holder.getItemHolder().getKey();
                        ResourceKey<TrimMaterial> matKey = armorTrim.material().getKey();
                        return itemKey != null && matKey != null && itemKey.location().getPath().contains(matKey.location().getPath());
                    }).forEach(holder -> ingredients.add(Optional.of(Ingredient.of(holder.getItem()))));
                }
                ItemEnchantments itemEnchantments = inputStack.get(DataComponents.ENCHANTMENTS);
                for (List<Tuple<Item, DataComponentPatch>> combo : getAllIngredientCombinations(ingredients, inputStack)) {
                    RecyclingRecipe outputStack = new RecyclingRecipe(inputStack.copyWithCount(1));
                    Item baseItem = ingredients.getFirst().map(ing -> ing.getItems()[0].getItem()).orElse(Items.AIR);
                    for (Tuple<Item, DataComponentPatch> item : combo) {
                        if (outputStack.contains(item)) {
                            ItemStack stack = outputStack.getStack(item);
                            if (item.getA().getDefaultInstance().is(baseItem)) {
                                stack.set(DataComponents.ENCHANTMENTS, itemEnchantments);
                                stack.set(DataComponents.DAMAGE, inputStack.get(DataComponents.DAMAGE));
                            }
                            stack.grow(1);
                            outputStack.setOutput(outputStack.indexOf(item), stack);
                        } else {
                            ItemStack itemStack = new ItemStack(item.getA().builtInRegistryHolder(), 1, item.getB());
                            if (item.getA().getDefaultInstance().is(baseItem)) {
                                itemStack.set(DataComponents.ENCHANTMENTS, itemEnchantments);
                                itemStack.set(DataComponents.DAMAGE, inputStack.get(DataComponents.DAMAGE));
                            }
                            outputStack.addOutput(itemStack);
                        }
                    }
                    outputs.add(outputStack);
                }
            }
            if (r.value() instanceof StonecutterRecipe stonecutterRecipe) {
                for (List<Tuple<Item, DataComponentPatch>> combo : getAllShapelessIngredientCombinations(
                        new ArrayList<>(stonecutterRecipe.getIngredients()), inputStack)) {
                    RecyclingRecipe outputStack = new RecyclingRecipe(inputStack.copyWithCount(stonecutterRecipe.result.getCount()));
                    addComboToOutput(combo, outputStack, inputStack, outputs);
                }
            }

            if (r.value() instanceof ArchitectsCutterRecipe architectsCutterRecipe) {
                // Multiple DO recipes may match the same item type (one per material combo); only add once.
                // The actual materials come from the item's TEXTURE_DATA component, not the recipe template.
                if (outputs.stream().anyMatch(o -> o.getInput().is(inputStack.getItem()))) continue;
                MaterialTextureData textureData = MaterialTextureData.readFromItemStack(inputStack);
                Map<ResourceLocation, Block> materials = textureData.getTexturedComponents();
                if (materials.isEmpty()) continue;
                RecyclingRecipe outputStack = new RecyclingRecipe(inputStack.copyWithCount(architectsCutterRecipe.getCount()));
                for (Block materialBlock : materials.values()) {
                    if (materialBlock.asItem() != Items.AIR) {
                        outputStack.addOutput(new ItemStack(materialBlock.asItem(), 1));
                    }
                }
                outputs.add(outputStack);
            }
        }

        return new Tuple<>(outputs, true);
    }

    private static List<Tuple<Item, DataComponentPatch>> filterIngredientsByConfig(
            List<Tuple<Item, DataComponentPatch>> items, ItemStack inputStack, OttConfig.Recycling cfg) {
        return items.stream().filter(item -> {
            boolean isVanillaInput = BuiltInRegistries.ITEM.getKey(inputStack.getItem()).getNamespace().equals("minecraft");
            if (isVanillaInput && cfg.PREVENT_MODDED_INGREDIENTS.getAsBoolean()) {
                return BuiltInRegistries.ITEM.getKey(item.getA()).getNamespace().equals("minecraft");
            } else if (items.size() > 1) {
                return !cfg.RESTRICTED_MOD_INGREDIENTS.get().contains(BuiltInRegistries.ITEM.getKey(item.getA()).getNamespace());
            }
            return true;
        }).toList();
    }

    private static void addComboToOutput(List<Tuple<Item, DataComponentPatch>> combo,
            RecyclingRecipe outputStack, ItemStack inputStack, List<RecyclingRecipe> outputs) {
        Map<Tuple<Item, DataComponentPatch>, Integer> allIngredients = new HashMap<>();
        for (Tuple<Item, DataComponentPatch> item : combo) {
            if (item.getA() == Items.AIR) continue;
            if (outputStack.contains(item)) {
                ItemStack stack = outputStack.getStack(item);
                stack.grow(1);
                outputStack.setOutput(outputStack.indexOf(item), stack);
            } else {
                outputStack.addOutput(new ItemStack(item.getA().builtInRegistryHolder(), 1, item.getB()));
            }
            allIngredients.put(item, allIngredients.getOrDefault(item, 0) + 1);
        }
        if (inputStack.isDamaged()) {
            reduceDamagedOutputs(inputStack, outputStack, allIngredients);
        }
        outputs.add(outputStack);
    }

    private static void reduceDamagedOutputs(ItemStack inputStack, RecyclingRecipe outputStack, Map<Tuple<Item, DataComponentPatch>, Integer> allIngredients) {
        for (var x : allIngredients.entrySet()) {
            if (inputStack.getItem().isValidRepairItem(inputStack, new ItemStack(x.getKey().getA(), x.getValue()))) {
                int damagedPercentage = (int) Math.ceil((double) inputStack.getDamageValue() / inputStack.getMaxDamage() * x.getValue());
                while (outputStack.getStack(x.getKey()).getCount() > 0 && damagedPercentage != 0) {
                    if (outputStack.getStack(x.getKey()).is(x.getKey().getA())) {
                        outputStack.getStack(x.getKey()).shrink(1);
                        damagedPercentage--;
                    }
                }
                break;
            }
        }
    }

    public static List<Tuple<Item, DataComponentPatch>> getItemsFromIngredient(Ingredient ingredient, ItemStack inputStack) {
        List<Tuple<Item, DataComponentPatch>> items = new ArrayList<>();

        if (ingredient.getCustomIngredient() != null && !ingredient.getCustomIngredient().getItems().toList().isEmpty()) {
            if (ingredient.getCustomIngredient() instanceof DataComponentIngredient dci) {
                for (var holder : dci.items()) {
                    items.add(new Tuple<>(holder.value(), dci.components().asPatch()));
                }
            } else {
                for (var holder : ingredient.getCustomIngredient().getItems().toList()) {
                    items.add(new Tuple<>(holder.getItem(), DataComponentPatch.EMPTY));
                }
            }
        } else {
            try {
                items = Arrays.stream(ingredient.getItems())
                        .map(holder -> new Tuple<>(holder.getItem(), DataComponentPatch.EMPTY))
                        .distinct()
                        .toList();
            } catch (IllegalStateException e) {
                LogUtils.getLogger().warn("Skipping unsupported ingredient type: {}", ingredient);
                return Collections.emptyList();
            }
        }

        return items.stream()
                .filter(item -> filterIngredient(item, inputStack))
                .sorted(Comparator.comparing(tuple -> tuple.getA().getDescriptionId()))
                .toList();
    }

    private static boolean filterIngredient(Tuple<Item, DataComponentPatch> item, ItemStack inputStack) {
        String inputPath = inputStack.getItem().builtInRegistryHolder().key().location().getPath();
        String desc = item.getA().getDescriptionId();
        if (desc.contains("shulker_box") && inputPath.contains("_shulker_box")) return item.getA() == Items.SHULKER_BOX;
        if (desc.contains("bundle") && inputPath.contains("_bundle")) return item.getA() == Items.BUNDLE;
        if (desc.contains("wool") && inputPath.contains("_wool")) return item.getA() == Items.WHITE_WOOL;
        if (desc.contains("bed") && inputPath.contains("_bed") && inputStack.getItem().builtInRegistryHolder().key().location().getNamespace().contains("minecraft")) return item.getA() == Items.WHITE_BED;
        if (desc.contains("carpet") && inputPath.contains("_carpet")) return item.getA() == Items.WHITE_CARPET;
        return item.getA().getCraftingRemainingItem(item.getA().getDefaultInstance()) == ItemStack.EMPTY
                || item.getA().getCraftingRemainingItem(item.getA().getDefaultInstance()).getItem() != item.getA().getDefaultInstance().getItem();
    }

    public static List<List<Tuple<Item, DataComponentPatch>>> getAllIngredientCombinations(List<Optional<Ingredient>> ingredients, ItemStack inputStack) {
        Map<String, Group> groupKeyToGroup = new HashMap<>();
        OttConfig.Recycling cfg = OttConfig.RECYCLING;

        for (int i = 0; i < ingredients.size(); i++) {
            List<Tuple<Item, DataComponentPatch>> items = ingredients.get(i).map(ing -> {
                List<Tuple<Item, DataComponentPatch>> ingItems = getItemsFromIngredient(ing, inputStack);
                return ingItems.isEmpty() ? List.of(new Tuple<>(Items.AIR, DataComponentPatch.EMPTY)) : ingItems;
            }).orElse(List.of(new Tuple<>(Items.AIR, DataComponentPatch.EMPTY)));

            items = filterIngredientsByConfig(items, inputStack, cfg);

            String key = items.stream().map(Tuple::getA).map(Item::getDescriptionId).sorted().collect(Collectors.joining(","));
            List<Tuple<Item, DataComponentPatch>> finalItems = items;
            groupKeyToGroup.computeIfAbsent(key, k -> new Group(new ArrayList<>(), finalItems)).positions.add(i);
        }

        return getLists(groupKeyToGroup, ingredients.size());
    }

    public static List<List<Tuple<Item, DataComponentPatch>>> getAllShapelessIngredientCombinations(List<Ingredient> ingredients, ItemStack inputStack) {
        Map<String, Group> groupKeyToGroup = new HashMap<>();
        OttConfig.Recycling cfg = OttConfig.RECYCLING;

        for (int i = 0; i < ingredients.size(); i++) {
            List<Tuple<Item, DataComponentPatch>> items = getItemsFromIngredient(ingredients.get(i), inputStack);
            if (items.isEmpty()) items = List.of(new Tuple<>(Items.AIR, DataComponentPatch.EMPTY));

            items = filterIngredientsByConfig(items, inputStack, cfg);

            String key = items.stream().map(Tuple::getA).map(Item::getDescriptionId).sorted().collect(Collectors.joining(","));
            List<Tuple<Item, DataComponentPatch>> finalItems = items;
            groupKeyToGroup.computeIfAbsent(key, k -> new Group(new ArrayList<>(), finalItems)).positions.add(i);
        }

        return getLists(groupKeyToGroup, ingredients.size());
    }

    @NotNull
    public static List<List<Tuple<Item, DataComponentPatch>>> getLists(Map<String, Group> groupKeyToGroup, int size) {
        List<Group> groups = new ArrayList<>(groupKeyToGroup.values());
        List<List<Tuple<Item, DataComponentPatch>>> groupChoices = groups.stream().map(g -> g.items).collect(Collectors.toList());
        List<List<Tuple<Item, DataComponentPatch>>> product = cartesianProduct(groupChoices);
        List<List<Tuple<Item, DataComponentPatch>>> combinations = new ArrayList<>();

        for (List<Tuple<Item, DataComponentPatch>> choiceList : product) {
            NonNullList<Tuple<Item, DataComponentPatch>> itemsArray = NonNullList.withSize(size, new Tuple<>(Items.AIR, DataComponentPatch.EMPTY));
            for (int groupIdx = 0; groupIdx < groups.size(); groupIdx++) {
                Group group = groups.get(groupIdx);
                Tuple<Item, DataComponentPatch> chosenItem = choiceList.get(groupIdx);
                for (int pos : group.positions) {
                    if (pos >= 0 && pos < itemsArray.size()) {
                        itemsArray.set(pos, chosenItem);
                    }
                }
            }
            combinations.add(itemsArray);
        }

        return combinations;
    }

    public static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
        List<List<T>> result = new ArrayList<>();
        if (lists.isEmpty()) {
            result.add(new ArrayList<>());
            return result;
        }
        List<T> firstList = lists.getFirst();
        List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
        for (T item : firstList) {
            for (List<T> remaining : remainingLists) {
                List<T> combination = new ArrayList<>();
                combination.add(item);
                combination.addAll(remaining);
                result.add(combination);
            }
        }
        return result;
    }

    public static class Group {
        public List<Integer> positions;
        public List<Tuple<Item, DataComponentPatch>> items;

        public Group(List<Integer> positions, List<Tuple<Item, DataComponentPatch>> items) {
            this.positions = positions;
            this.items = items;
        }
    }
}