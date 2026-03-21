package com.otterly76.ott.item.custom;

import com.otterly76.ott.entity.projectile.QuillArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuillArrowItem extends ArrowItem {
    public QuillArrowItem(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack ammo, @NotNull LivingEntity shooter, @Nullable ItemStack weapon) {
        return new QuillArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @NotNull
    @Override
    public Projectile asProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack stack, @NotNull Direction direction) {
        QuillArrowEntity arrow = new QuillArrowEntity(level, position.x(), position.y(), position.z(), stack.copyWithCount(1), ItemStack.EMPTY);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }
}
