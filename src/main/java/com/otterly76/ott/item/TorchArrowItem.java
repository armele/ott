package com.otterly76.ott.item;

import com.otterly76.ott.entity.TorchArrowEntity;
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

public class TorchArrowItem extends ArrowItem {
    public TorchArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level level, ItemStack stack, @NotNull LivingEntity shooter, @Nullable ItemStack weapon) {
        return new TorchArrowEntity(level, shooter, stack.copyWithCount(1), weapon);
    }

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level level, Position pos, ItemStack stack, @NotNull Direction direction) {
        TorchArrowEntity torchArrow = new TorchArrowEntity(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        torchArrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return torchArrow;
    }
}
