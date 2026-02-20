package com.otterly76.ott.client.render.blockentity;

import com.otterly76.ott.block.custom.CopperChestBlock;
import com.otterly76.ott.block.entity.CopperChestBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;

public class CopperChestRenderer extends ChestRenderer<CopperChestBlockEntity> {
    private static final Material COPPER = chestMaterial("copper");
    private static final Material COPPER_LEFT = chestMaterial("copper_left");
    private static final Material COPPER_RIGHT = chestMaterial("copper_right");
    
    private static final Material EXPOSED = chestMaterial("copper_exposed");
    private static final Material EXPOSED_LEFT = chestMaterial("copper_exposed_left");
    private static final Material EXPOSED_RIGHT = chestMaterial("copper_exposed_right");
    
    private static final Material WEATHERED = chestMaterial("copper_weathered");
    private static final Material WEATHERED_LEFT = chestMaterial("copper_weathered_left");
    private static final Material WEATHERED_RIGHT = chestMaterial("copper_weathered_right");
    
    private static final Material OXIDIZED = chestMaterial("copper_oxidized");
    private static final Material OXIDIZED_LEFT = chestMaterial("copper_oxidized_left");
    private static final Material OXIDIZED_RIGHT = chestMaterial("copper_oxidized_right");

    public CopperChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    private static Material chestMaterial(String name) {
        return new Material(Sheets.CHEST_SHEET, ResourceLocation.withDefaultNamespace("entity/chest/" + name));
    }

    @Override
    protected @NotNull Material getMaterial(@NotNull CopperChestBlockEntity blockEntity, @NotNull ChestType chestType) {
        WeatheringCopper.WeatherState state = WeatheringCopper.WeatherState.UNAFFECTED;
        if (blockEntity.getBlockState().getBlock() instanceof CopperChestBlock copperChest) {
            state = copperChest.getAge();
        }

        return switch (state) {
            case UNAFFECTED -> switch (chestType) {
                case SINGLE -> COPPER;
                case LEFT -> COPPER_LEFT;
                case RIGHT -> COPPER_RIGHT;
            };
            case EXPOSED -> switch (chestType) {
                case SINGLE -> EXPOSED;
                case LEFT -> EXPOSED_LEFT;
                case RIGHT -> EXPOSED_RIGHT;
            };
            case WEATHERED -> switch (chestType) {
                case SINGLE -> WEATHERED;
                case LEFT -> WEATHERED_LEFT;
                case RIGHT -> WEATHERED_RIGHT;
            };
            case OXIDIZED -> switch (chestType) {
                case SINGLE -> OXIDIZED;
                case LEFT -> OXIDIZED_LEFT;
                case RIGHT -> OXIDIZED_RIGHT;
            };
        };
    }
}