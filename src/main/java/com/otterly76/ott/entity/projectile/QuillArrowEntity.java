package com.otterly76.ott.entity.projectile;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class QuillArrowEntity extends AbstractArrow {
    public QuillArrowEntity(EntityType<? extends QuillArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public QuillArrowEntity(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ModEntities.QUILL_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    public QuillArrowEntity(Level level, LivingEntity owner, ItemStack pickup, ItemStack weapon) {
        super(ModEntities.QUILL_ARROW.get(), owner, level, pickup, weapon);
    }

    @NotNull
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.QUILL_ARROW.get());
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        this.level().broadcastEntityEvent(this, (byte) 3);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        this.level().broadcastEntityEvent(this, (byte) 3);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        if (!this.inGround && !this.isNoGravity()) {
            Vec3 motion = this.getDeltaMovement()
                    .scale(0.92D)
                    .add(0.0D, -0.03D, 0.0D);
            this.setDeltaMovement(motion);
        }
        super.tick();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            var particle = new ItemParticleOption(ParticleTypes.ITEM, this.getDefaultPickupItem());
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), (this.random.nextDouble() - 0.5D) * 0.08D, (this.random.nextDouble() - 0.5D) * 0.08D, (this.random.nextDouble() - 0.5D) * 0.08D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }
}
