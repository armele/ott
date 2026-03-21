package com.otterly76.ott.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SnailBucketItem extends NoFluidMobBucketItem {
    private final Supplier<? extends EntityType<?>> entityTypeSupplier;

    public SnailBucketItem(Supplier<? extends EntityType<?>> entityTypeSupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Properties properties) {
        super(entityTypeSupplier, fluidSupplier, soundSupplier, properties);
        this.entityTypeSupplier = entityTypeSupplier;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack stack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel) level, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    private void spawn(ServerLevel level, ItemStack stack, BlockPos pos) {
        Entity entity = this.entityTypeSupplier.get().spawn(level, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            CustomData customData = stack.get(DataComponents.BUCKET_ENTITY_DATA);
            if (customData != null) {
                bucketable.loadFromBucketTag(customData.copyTag());
            }
            bucketable.setFromBucket(true);
        }
    }
}