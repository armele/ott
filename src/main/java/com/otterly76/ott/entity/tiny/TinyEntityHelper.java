package com.otterly76.ott.entity.tiny;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TinyEntityHelper {
    public static void performSnowballAttack(Mob mob, @NotNull LivingEntity target, @Nullable ItemStack item) {
        Snowball snowball = new Snowball(mob.level(), mob);
        if (item != null) {
            snowball.setItem(item);
        }
        double d0 = target.getEyeY() - (double)1.1F;
        double d1 = target.getX() - mob.getX();
        double d2 = d0 - snowball.getY();
        double d3 = target.getZ() - mob.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        mob.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 0.4F / (mob.getRandom().nextFloat() * 0.4F + 0.8F));
        mob.level().addFreshEntity(snowball);
    }
}