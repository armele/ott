package com.otterly76.ott.entity.projectile;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class ChupacabraSpitEntity extends ThrowableItemProjectile implements ItemSupplier {
    public ChupacabraSpitEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ChupacabraSpitEntity(Level level, LivingEntity shooter) {
        super(ModEntities.CHUPACABRA_SPIT.get(), shooter, level);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity living) {
            if (!this.level().isClientSide) {
                living.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 2));
                this.playSound(ModSounds.ENTITY_POISON_SPIT_HIT.get(), 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ModParticle.POISON_SPIT.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.AIR;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemStack(this.getDefaultItem());
    }
}