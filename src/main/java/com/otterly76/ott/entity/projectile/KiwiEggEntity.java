package com.otterly76.ott.entity.projectile;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.KiwiEntity;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class KiwiEggEntity extends ThrowableItemProjectile {
    public KiwiEggEntity(EntityType<? extends KiwiEggEntity> entityType, Level level) {
        super(entityType, level);
    }

    public KiwiEggEntity(Level level, LivingEntity shooter) {
        super(ModEntities.KIWI_EGG.get(), shooter, level);
    }

    public KiwiEggEntity(Level level, double x, double y, double z) {
        super(ModEntities.KIWI_EGG.get(), x, y, z, level);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    KiwiEntity kiwi = ModEntities.KIWI.get().create(this.level());
                    if (kiwi != null) {
                        kiwi.setAge(-24000);
                        kiwi.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        this.level().addFreshEntity(kiwi);
                    }
                }
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.KIWI_EGG.get();
    }
}
