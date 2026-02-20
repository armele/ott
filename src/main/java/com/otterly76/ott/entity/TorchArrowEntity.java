package com.otterly76.ott.entity;

import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class TorchArrowEntity extends AbstractArrow {
    public TorchArrowEntity(EntityType<? extends TorchArrowEntity> type, Level level) {
        super(type, level);
    }

    public TorchArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, @javax.annotation.Nullable ItemStack weapon) {
        super(ModEntities.TORCH_ARROW.get(), shooter, level, pickupItemStack, weapon);
    }

    public TorchArrowEntity(Level level, double x, double y, double z, ItemStack pickupItemStack, @javax.annotation.Nullable ItemStack weapon) {
        super(ModEntities.TORCH_ARROW.get(), x, y, z, level, pickupItemStack, weapon);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.TORCH_ARROW.get());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            BlockPos hitPos = result.getBlockPos();
            Direction direction = result.getDirection();
            BlockPos placePos = hitPos.relative(direction);

            if (this.level().getBlockState(placePos).isAir()) {
                BlockState torchState = Blocks.TORCH.defaultBlockState();
                if (direction != Direction.UP && direction != Direction.DOWN) {
                    torchState = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, direction);
                }

                if (torchState.canSurvive(this.level(), placePos)) {
                    this.level().setBlockAndUpdate(placePos, torchState);
                    this.discard();
                }
            }
        }
    }
}