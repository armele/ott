package com.otterly76.ott.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttRegistryKeys;
import com.otterly76.ott.worldgen.NoiseRouterTarget;
import com.otterly76.ott.worldgen.modifier.WrapNoiseRouterModifier;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/RandomState;create(Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;Lnet/minecraft/core/HolderGetter;J)Lnet/minecraft/world/level/levelgen/RandomState;"
            )
    )
    private RandomState wrapNoiseRouter(NoiseGeneratorSettings noiseSettings, HolderGetter<NormalNoise.NoiseParameters> noiseGetter, long seed, Operation<RandomState> init, ServerLevel level, @Local(ordinal = 0) RegistryAccess registries) {
        NoiseRouter router = noiseSettings.noiseRouter();

        List<WrapNoiseRouterModifier> modifiers = Ott.registry(registries, OttRegistryKeys.WORLDGEN_MODIFIER).stream()
                .filter(modifier -> modifier instanceof WrapNoiseRouterModifier wrap && wrap.dimension().equals(level.dimension()))
                .map(modifier -> (WrapNoiseRouterModifier) modifier)
                .toList();

        if (!modifiers.isEmpty()) {
            NoiseRouter modifiedRouter = new NoiseRouter(
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.BARRIER, router.barrierNoise(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.FLUID_LEVEL_FLOODEDNESS, router.fluidLevelFloodednessNoise(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.FLUID_LEVEL_SPREAD, router.fluidLevelSpreadNoise(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.LAVA, router.lavaNoise(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.TEMPERATURE, router.temperature(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.VEGETATION, router.vegetation(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.CONTINENTS, router.continents(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.EROSION, router.erosion(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.DEPTH, router.depth(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.RIDGES, router.ridges(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.INITIAL_DENSITY, Ott.getInitialDensity(router), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.FINAL_DENSITY, router.finalDensity(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.VEIN_TOGGLE, router.veinToggle(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.VEIN_RIDGED, router.veinRidged(), modifiers),
                    WrapNoiseRouterModifier.modifyDensityFunction(NoiseRouterTarget.VEIN_GAP, router.veinGap(), modifiers)
            );
            ((NoiseGeneratorSettingsAccessor) (Object) noiseSettings).setNoiseRouter(modifiedRouter);
        }

        return init.call(noiseSettings, noiseGetter, seed);
    }
}
