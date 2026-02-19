package com.otterly76.ott.entity.variant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Optional;

public final class VanillaWolfVariantUtils {
    private VanillaWolfVariantUtils() {
    }

    public static Optional<Holder<WolfVariant>> selectVariantForSpawn(ServerLevelAccessor level, BlockPos pos) {
        SpawnContext context = SpawnContext.create(level, pos);
        return VanillaWolfVariantRegistry.selectVariantForSpawn(context, level.getRandom());
    }

    public static Optional<Holder<WolfVariant>> selectVariantForSpawn(SpawnContext context, RandomSource random) {
        return VanillaWolfVariantRegistry.selectVariantForSpawn(context, random);
    }

    public static void applyVariantToWolf(Wolf wolf, Holder<WolfVariant> variant) {
        wolf.setVariant(variant);
    }

    public static boolean selectAndApplyVariant(Wolf wolf, ServerLevelAccessor level, BlockPos pos) {
        Optional<Holder<WolfVariant>> variant = selectVariantForSpawn(level, pos);
        variant.ifPresent((v) -> applyVariantToWolf(wolf, v));
        return variant.isPresent();
    }

    public static Holder<WolfVariant> selectOffspringVariant(Wolf parent1, Wolf parent2, RandomSource random) {
        return random.nextBoolean() ? parent1.getVariant() : parent2.getVariant();
    }
}
