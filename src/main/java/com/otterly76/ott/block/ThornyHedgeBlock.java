package com.otterly76.ott.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.otterly76.ott.OttDamageTypes;

public class ThornyHedgeBlock extends Block implements BonemealableBlock {
    private static final VoxelShape HEDGE_BB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    private static final float DAMAGE = 2.0F;
    private static final int MAX_HEIGHT = 5;

    private static final TagKey<EntityType<?>> MINECOLONIES_RAIDER = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("minecolonies", "raider"));

    public ThornyHedgeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return HEDGE_BB;
    }

    @Override
    public PathType getBlockPathType(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @Nullable Mob mob) {
        return this.shouldDamage(mob) ? PathType.DANGER_OTHER : null;
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (this.shouldDamage(entity)) {
            this.hurtEntity(level, entity);
        }
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (this.shouldDamage(entity)) {
            this.hurtEntity(level, entity);
        }
    }

    @Override
    public void attack(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        this.hurtEntity(level, player);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity te, @NotNull ItemStack stack) {
        super.playerDestroy(level, player, pos, state, te, stack);
        this.hurtEntity(level, player);
    }

    private void hurtEntity(Level level, Entity entity) {
        if (level.isClientSide() || (entity instanceof Player player && player.isCreative())) {
            return;
        }
        entity.hurt(OttDamageTypes.of(level, OttDamageTypes.FLORA_DAMAGE), DAMAGE);
    }

    private int calculateHedgeHeight(LevelReader level, BlockPos pos) {
        int height = 0;
        BlockPos.MutableBlockPos checkPos = pos.mutable();
        while (level.getBlockState(checkPos).is(this)) {
            height++;
            checkPos.move(0, -1, 0);
        }
        return height;
    }

    private boolean shouldDamage(Entity entity) {
        return entity instanceof LivingEntity;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return calculateHedgeHeight(level, pos) < MAX_HEIGHT && level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, @NotNull RandomSource random, BlockPos pos, @NotNull BlockState state) {
        level.setBlockAndUpdate(pos.above(), this.defaultBlockState());
    }
}