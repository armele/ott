package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyceliumMammoth extends Mammoth {
    public MyceliumMammoth(EntityType<? extends Mammoth> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getVariant() {
        return 1; // Always mycelium
    }

    public static boolean checkMyceliumMammothSpawnRules(EntityType<? extends Mammoth> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBiome(pos).is(Biomes.MUSHROOM_FIELDS) && level.getBlockState(pos.below()).is(Blocks.MYCELIUM) && isBrightEnoughToSpawn(level, pos);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return ModEntities.MYCELIUM_MAMMOTH.get().create(level);
    }
}
