package com.otterly76.ott.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BaggedMobItem extends Item {
    private final Supplier<? extends EntityType<?>> entityTypeSupplier;
    private final Supplier<? extends SoundEvent> emptySoundSupplier;

    public BaggedMobItem(Supplier<? extends EntityType<?>> entityType, Supplier<? extends SoundEvent> emptySound, Properties properties) {
        super(properties);
        this.entityTypeSupplier = entityType;
        this.emptySoundSupplier = emptySound;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockPos blockpos1 = blockpos.relative(direction);
            
            if (this.spawnEntity((ServerLevel)level, itemstack, blockpos1)) {
                Player player = context.getPlayer();
                if (player != null && !player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                    // Optionally return an empty bag here if needed, but for now we just shrink.
                    // If we want to return a REPTILE_BAG:
                    // player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(itemstack, player, new ItemStack(ModItems.REPTILE_BAG.get())));
                }

                level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos1);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    private boolean spawnEntity(ServerLevel level, ItemStack stack, BlockPos pos) {
        EntityType<?> entitytype = this.entityTypeSupplier.get();
        Entity entity = entitytype.spawn(level, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity != null) {
            level.playSound(null, pos, this.emptySoundSupplier.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return true;
        }
        return false;
    }
}