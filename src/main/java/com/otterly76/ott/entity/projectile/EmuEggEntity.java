package com.otterly76.ott.entity.projectile;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.Emu;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EmuEggEntity extends ThrowableItemProjectile {
    public EmuEggEntity(EntityType<? extends EmuEggEntity> type, Level level) {
        super(type, level);
    }

    public EmuEggEntity(Level level, LivingEntity shooter) {
        super(ModEntities.EMU_EGG.get(), shooter, level);
    }

    public EmuEggEntity(Level level, double x, double y, double z) {
        super(ModEntities.EMU_EGG.get(), x, y, z, level);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.EMU_EGG.get();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08);
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
                    Emu emu = ModEntities.EMU.get().create(this.level());
                    if (emu != null) {
                        emu.setAge(-24000);
                        emu.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        this.level().addFreshEntity(emu);
                    }
                }
            }

            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }
}
