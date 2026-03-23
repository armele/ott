package com.otterly76.ott.item.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.Butterfly;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ButterflyItem extends CaughtMobItem {
    private final Butterfly.Variant variant;

    public ButterflyItem(Butterfly.Variant variant, Properties properties) {
        super(ModEntities.BUTTERFLY, () -> Fluids.EMPTY, () -> SoundEvents.BUCKET_EMPTY_FISH, properties);
        this.variant = variant;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack containerStack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            Butterfly butterfly = ModEntities.BUTTERFLY.get().spawn(serverLevel, containerStack, player, pos, MobSpawnType.BUCKET, true, false);
            if (butterfly != null) {
                butterfly.setVariant(this.variant);
                CustomData customData = containerStack.get(DataComponents.CUSTOM_DATA);
                if (customData != null) {
                    butterfly.loadFromHandTag(customData.copyTag());
                }
                butterfly.setFromHand(true);
            }
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }
}