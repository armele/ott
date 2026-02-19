package com.otterly76.ott.client.handler;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.otterly76.ott.handler.BlockConversionHandler;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockModelHandler {
    private static final Supplier<Map<ModelResourceLocation, ModelResourceLocation>> MODEL_LOCATIONS = Suppliers.memoize(() -> BlockConversionHandler.getBlockConversions().entrySet().stream().flatMap((entry) -> convertAllBlockStates(entry.getValue(), entry.getKey()).entrySet().stream()).collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

    public static void onModelBaking(ModelEvent.ModifyBakingResult event) {
        for (Map.Entry<ModelResourceLocation, ModelResourceLocation> entry : MODEL_LOCATIONS.get().entrySet()) {
            BakedModel bakedModel = event.getModels().get(entry.getValue());
            if (bakedModel != null) {
                event.getModels().put(entry.getKey(), bakedModel);
            }
        }
    }

    private static Map<ModelResourceLocation, ModelResourceLocation> convertAllBlockStates(Block oldBlock, Block newBlock) {
        Map<ModelResourceLocation, ModelResourceLocation> modelLocations = Maps.newHashMap();

        for (BlockState oldBlockState : oldBlock.getStateDefinition().getPossibleStates()) {
            BlockState newBlockState = convertBlockState(newBlock.getStateDefinition(), oldBlockState);
            modelLocations.put(BlockModelShaper.stateToModelLocation(oldBlockState), BlockModelShaper.stateToModelLocation(newBlockState));
        }

        return modelLocations;
    }

    private static BlockState convertBlockState(StateDefinition<Block, BlockState> newStateDefinition, BlockState oldBlockState) {
        BlockState newBlockState = newStateDefinition.any();

        for (Map.Entry<Property<?>, Comparable<?>> entry : oldBlockState.getValues().entrySet()) {
            Property<?> property = entry.getKey();
            Comparable<?> value = entry.getValue();
            newBlockState = setBlockStateValue(property, value, newStateDefinition::getProperty, newBlockState);
        }

        return newBlockState;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState setBlockStateValue(Property<?> oldProperty, Comparable<?> oldValue, Function<String, @Nullable Property<?>> propertyGetter, BlockState blockState) {
        Property<T> newProperty = (Property<T>) propertyGetter.apply(oldProperty.getName());
        return newProperty != null ? blockState.setValue(newProperty, (T) oldValue) : blockState;
    }
}
