package com.otterly76.ott.handler;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.otterly76.ott.Constants;
import com.otterly76.ott.Ott;
import com.otterly76.ott.block.AnvilWithInventoryBlock;
import com.otterly76.ott.mixin.common.BlockAccessor;
import com.otterly76.ott.mixin.common.BlockItemAccessor;
import com.otterly76.ott.mixin.common.HolderReferenceAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class BlockConversionHandler {
    public static final Component INVALID_BLOCK_COMPONENT = Component.translatable("container.invalidBlock");
    private static final BiMap<Block, Block> BLOCK_CONVERSIONS = HashBiMap.create();
    private static final java.util.Map<BlockState, BlockState> BLOCK_STATE_CONVERSIONS_CACHE = (new MapMaker()).weakKeys().weakValues().makeMap();

    public static void onRegisterBlocks(RegisterEvent event) {
        if (event.getRegistryKey().equals(net.minecraft.core.registries.Registries.BLOCK)) {
            java.util.List<java.util.Map.Entry<ResourceKey<Block>, Block>> entries = new java.util.ArrayList<>(BuiltInRegistries.BLOCK.entrySet());
            for (Map.Entry<ResourceKey<Block>, Block> entry : entries) {
                Block block = entry.getValue();
                if (Ott.ANVIL_BLOCK_PREDICATE.test(block)) {
                    ResourceLocation id = entry.getKey().location();
                    ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id.getNamespace() + "/" + id.getPath());
                    event.register(net.minecraft.core.registries.Registries.BLOCK, newId, () -> {
                        Block newBlock = new AnvilWithInventoryBlock(block);
                        BLOCK_CONVERSIONS.put(block, newBlock);
                        return newBlock;
                    });
                }
            }
        }
    }

    public static BiMap<Block, Block> getBlockConversions() {
        return Maps.unmodifiableBiMap(BLOCK_CONVERSIONS);
    }

    public static void performTagsUpdated(Predicate<Block> filter, net.minecraft.core.RegistryAccess registryAccess, boolean client) {
        for(java.util.Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            Object patt0$temp = entry.getValue();
            if (patt0$temp instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                setItemForBlock(filter, blockItem, block);
                Block newBlock = BLOCK_CONVERSIONS.get(block);
                if (newBlock != null) {
                    setBlockForItem(blockItem, newBlock);
                }
            }
        }

        BLOCK_CONVERSIONS.forEach(BlockConversionHandler::copyBoundTags);
    }

    private static void setItemForBlock(Predicate<Block> filter, BlockItem blockItem, Block block) {
        if (filter.test(block)) {
            setItemForBlock(BLOCK_CONVERSIONS.get(block), blockItem);
        }

    }

    public static void setItemForBlock(Block block, Item item) {
        Objects.requireNonNull(block, () -> {
            String var10000 = item != null ? "for item '" + BuiltInRegistries.ITEM.getKey(item) + "' " : "";
            return "block " + var10000 + "is null";
        });
        Objects.requireNonNull(item, () -> "item for block '" + BuiltInRegistries.BLOCK.getKey(block) + "' is null");
        Item.BY_BLOCK.put(block, item);
        ((BlockAccessor) block).setItem(item);
    }

    public static void setBlockForItem(BlockItem item, Block block) {
        Objects.requireNonNull(item, () -> {
            String var10000 = block != null ? "for block '" + BuiltInRegistries.BLOCK.getKey(block) + "' " : "";
            return "item " + var10000 + "is null";
        });
        Objects.requireNonNull(block, () -> "block for item '" + BuiltInRegistries.ITEM.getKey(item) + "' is null");
        Block oldBlock = item.getBlock();
        ((BlockAccessor) oldBlock).setItem(item);

        ((BlockItemAccessor) item).setBlock(block);
    }

    @SuppressWarnings("unchecked")
    public static void copyBoundTags(Block from, Block to) {
        Objects.requireNonNull(from, () -> {
            String var10000 = to != null ? "for target '" + BuiltInRegistries.BLOCK.getKey(to) + "' " : "";
            return "source " + var10000 + "is null";
        });
        Objects.requireNonNull(to, () -> "target for source '" + BuiltInRegistries.BLOCK.getKey(from) + "' is null");
        Holder.Reference<Block> fromHolder = BuiltInRegistries.BLOCK.getHolder(BuiltInRegistries.BLOCK.getKey(from)).orElseThrow();
        Holder.Reference<Block> toHolder = BuiltInRegistries.BLOCK.getHolder(BuiltInRegistries.BLOCK.getKey(to)).orElseThrow();
        Set<TagKey<Block>> fromTagKeys = fromHolder.tags().collect(Collectors.toSet());
        Set<TagKey<Block>> toTagKeys = toHolder.tags().collect(Collectors.toSet());
        if (toTagKeys.isEmpty()) {
            ((HolderReferenceAccessor<Block>) toHolder).callBindTags(fromTagKeys);
        } else if (!Objects.equals(fromTagKeys, toTagKeys)) {
            throw new IllegalStateException("Target block tags for " + BuiltInRegistries.BLOCK.getKey(to) + " not empty: " + toTagKeys);
        }

    }

    public static @Nullable BlockState convertToVanillaBlock(@Nullable BlockState blockState) {
        return applyBlockConversion(blockState, true);
    }

    public static @Nullable BlockState convertFromVanillaBlock(@Nullable BlockState blockState) {
        return applyBlockConversion(blockState, false);
    }

    private static @Nullable BlockState applyBlockConversion(@Nullable BlockState blockState, boolean inverse) {
        return blockState != null ? BLOCK_STATE_CONVERSIONS_CACHE.computeIfAbsent(blockState, (k) -> applyBlockConversion(inverse).apply(k)) : null;
    }

    private static UnaryOperator<BlockState> applyBlockConversion(boolean inverse) {
        return (blockState) -> {
            BiMap<Block, Block> blockConversions = inverse ? BLOCK_CONVERSIONS.inverse() : BLOCK_CONVERSIONS;
            if (blockState != null && blockConversions.containsKey(blockState.getBlock())) {
                Block block = blockConversions.get(blockState.getBlock());
                return copyAllProperties(blockState, block.defaultBlockState());
            } else {
                return blockState;
            }
        };
    }

    private static BlockState copyAllProperties(BlockState oldBlockState, BlockState newBlockState) {
        for (Map.Entry<Property<?>, Comparable<?>> entry : oldBlockState.getValues().entrySet()) {
            newBlockState = setProperty(newBlockState, entry.getKey(), entry.getValue());
        }

        return newBlockState;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState setProperty(BlockState blockState, Property<T> property, Comparable<?> value) {
        return blockState.trySetValue(property, (T) value);
    }
}