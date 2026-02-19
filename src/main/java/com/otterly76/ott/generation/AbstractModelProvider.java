package com.otterly76.ott.generation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.otterly76.ott.mixin.client.ModelTemplatesAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractModelProvider implements DataProvider {
    public static final String BLOCK_PATH = "block";
    public static final String ITEM_PATH = "item";
    public static final ModelTemplate SPAWN_EGG = ModelTemplatesAccessor.ott$invokeCreateItem("template_spawn_egg");
    private final String modId;
    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider modelPathProvider;
    private final Set<Object> skipValidation;

    public AbstractModelProvider(DataProviderContext context) {
        this(context.getModId(), context.getPackOutput());
    }

    public AbstractModelProvider(String modId, PackOutput packOutput) {
        this.skipValidation = Sets.newHashSet();
        this.modId = modId;
        this.blockStatePathProvider = packOutput.createPathProvider(Target.RESOURCE_PACK, "blockstates");
        this.modelPathProvider = packOutput.createPathProvider(Target.RESOURCE_PACK, "models");
    }

    public void addBlockModels(BlockModelGenerators builder) {
    }

    public void addItemModels(ItemModelGenerators builder) {
    }

    protected boolean isValidationEnabled() {
        return true;
    }

    protected void skipBlock(Block block) {
        this.skipValidation.add(block);
    }

    protected void skipItem(Item item) {
        this.skipValidation.add(item);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        Map<Block, BlockStateGenerator> generators = Maps.newHashMap();
        Consumer<BlockStateGenerator> blockStateOutput = (generator) -> {
            Block block = generator.getBlock();
            BlockStateGenerator blockstategenerator = generators.put(block, generator);
            if (blockstategenerator != null) {
                throw new IllegalStateException("Duplicate block state definition for " + block);
            }
        };
        Map<ResourceLocation, Supplier<JsonElement>> models = Maps.newHashMap();
        Set<Item> skippedAutoModels = Sets.newHashSet();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = (resourceLocation, supplier) -> {
            if (models.put(resourceLocation, supplier) != null) {
                throw new IllegalStateException("Duplicate model definition for " + resourceLocation);
            }
        };
        Objects.requireNonNull(skippedAutoModels);
        this.addBlockModels(new BlockModelGenerators(blockStateOutput, modelOutput, skippedAutoModels::add));
        this.addItemModels(new ItemModelGenerators(modelOutput));
        List<Block> missingBlocks;
        if (this.isValidationEnabled()) {
            missingBlocks = BuiltInRegistries.BLOCK.entrySet().stream()
                    .filter((entry) -> entry.getKey().location().getNamespace().equals(this.modId))
                    .map(Map.Entry::getValue)
                    .filter(block -> !generators.containsKey(block))
                    .filter(block -> !this.skipValidation.contains(block))
                    .toList();
        } else {
            missingBlocks = Collections.emptyList();
        }

        if (!missingBlocks.isEmpty()) {
            throw new IllegalStateException("Missing block state definitions for " + missingBlocks);
        } else {
            BuiltInRegistries.BLOCK.entrySet().forEach((entry) -> {
                Item item = Item.BY_BLOCK.get(entry.getValue());
                if (item != null) {
                    if (!entry.getKey().location().getNamespace().equals(this.modId) || skippedAutoModels.contains(item)) {
                        return;
                    }

                    ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(item);
                    if (!models.containsKey(resourcelocation)) {
                        models.put(resourcelocation, new DelegatedModel(ModelLocationUtils.getModelLocation(entry.getValue())));
                    }
                }

            });
            List<Item> missingItems;
            if (this.isValidationEnabled()) {
                missingItems = BuiltInRegistries.ITEM.entrySet().stream()
                        .filter((entry) -> entry.getKey().location().getNamespace().equals(this.modId))
                        .filter((entry) -> !models.containsKey(decorateItemModelLocation(entry.getKey().location())))
                        .map(Map.Entry::getValue)
                        .filter(item -> !this.skipValidation.contains(item))
                        .toList();
            } else {
                missingItems = Collections.emptyList();
            }

            if (!missingItems.isEmpty()) {
                throw new IllegalStateException("Missing item models for " + missingItems);
            } else {
                return CompletableFuture.allOf(
                        saveCollection(output, generators, (block) -> this.blockStatePathProvider.json(BuiltInRegistries.BLOCK.getKey(block))),
                        saveCollection(output, models, this.modelPathProvider::json)
                );
            }
        }
    }

    @Override
    public @NotNull String getName() {
        return "Model Definitions";
    }

    private static <T> CompletableFuture<?> saveCollection(CachedOutput output, Map<T, ? extends Supplier<JsonElement>> map, Function<T, Path> pathExtractor) {
        return CompletableFuture.allOf(map.entrySet().stream().map((entry) -> {
            Path path = pathExtractor.apply(entry.getKey());
            JsonElement jsonElement = entry.getValue().get();
            return DataProvider.saveStable(output, jsonElement, path);
        }).toArray(CompletableFuture[]::new));
    }

    public static ResourceLocation getModelLocation(Block block) {
        return decorateBlockModelLocation(getLocation(block));
    }

    public static ResourceLocation decorateBlockModelLocation(ResourceLocation resourceLocation) {
        return resourceLocation.withPrefix("block/");
    }

    public static ResourceLocation getLocation(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String getName(Block block) {
        return getLocation(block).getPath();
    }

    public static ResourceLocation getModelLocation(Item item) {
        return decorateItemModelLocation(getLocation(item));
    }

    public static ResourceLocation decorateItemModelLocation(ResourceLocation resourceLocation) {
        return resourceLocation.withPrefix("item/");
    }

    public static ResourceLocation getLocation(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static String getName(Item item) {
        return getLocation(item).getPath();
    }

    public static ResourceLocation stripUntil(ResourceLocation resourceLocation, String s) {
        String path = resourceLocation.getPath();
        if (path.contains(s)) {
            path = path.substring(path.lastIndexOf(s) + 1);
            return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), path);
        } else {
            return resourceLocation;
        }
    }

    public static ModelTemplate createBlockModelTemplate(ResourceLocation blockModelLocation, TextureSlot... requiredSlots) {
        return createBlockModelTemplate(blockModelLocation, "", requiredSlots);
    }

    public static ModelTemplate createBlockModelTemplate(ResourceLocation blockModelLocation, String suffix, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(decorateBlockModelLocation(blockModelLocation)), Optional.of(suffix), requiredSlots);
    }

    public static ModelTemplate createItemModelTemplate(ResourceLocation itemModelLocation, TextureSlot... requiredSlots) {
        return createItemModelTemplate(itemModelLocation, "", requiredSlots);
    }

    public static ModelTemplate createItemModelTemplate(ResourceLocation itemModelLocation, String suffix, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(decorateItemModelLocation(itemModelLocation)), Optional.of(suffix), requiredSlots);
    }

    public static ResourceLocation generateFlatItem(ResourceLocation resourceLocation, ModelTemplate modelTemplate, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
        return modelTemplate.create(decorateItemModelLocation(resourceLocation), TextureMapping.layer0(decorateItemModelLocation(resourceLocation)), modelOutput);
    }

    public static ResourceLocation generateFlatItem(Item item, ModelTemplate modelTemplate, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput, ModelTemplate.JsonFactory factory) {
        return modelTemplate.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), modelOutput, factory);
    }
}
