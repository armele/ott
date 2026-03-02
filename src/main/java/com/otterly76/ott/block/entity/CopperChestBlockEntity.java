package com.otterly76.ott.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import com.otterly76.ott.sound.ModSounds;
import org.jetbrains.annotations.NotNull;

public class CopperChestBlockEntity extends ChestBlockEntity {
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
            CopperChestBlockEntity.this.playSound(state, ModSounds.COPPER_CHEST_OPEN.get());
        }

        @Override
        protected void onClose(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
            CopperChestBlockEntity.this.playSound(state, ModSounds.COPPER_CHEST_CLOSE.get());
        }

        @Override
        protected void openerCountChanged(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, int count1, int count2) {
            CopperChestBlockEntity.this.signalOpenCount(level, pos, state, count1, count2);
        }

        @Override
        protected boolean isOwnContainer(@NotNull Player player) {
            if (!(player.containerMenu instanceof net.minecraft.world.inventory.ChestMenu)) {
                return false;
            } else {
                net.minecraft.world.Container container = ((net.minecraft.world.inventory.ChestMenu)player.containerMenu).getContainer();
                return container == CopperChestBlockEntity.this;
            }
        }
    };

    public CopperChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COPPER_CHEST.get(), pos, state);
    }

    @Override
    public void startOpen(@NotNull Player player) {
        if (!player.isSpectator()) {
            Level level = this.getLevel();
            if (level != null) {
                this.openersCounter.incrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
            }
        }
    }

    @Override
    public void stopOpen(@NotNull Player player) {
        if (!player.isSpectator()) {
            Level level = this.getLevel();
            if (level != null) {
                this.openersCounter.decrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
            }
        }
    }

    @Override
    public void recheckOpen() {
        Level level = this.getLevel();
        if (level != null) {
            this.openersCounter.recheckOpeners(level, this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.minecraft.copper_chest");
    }

    private void playSound(BlockState state, SoundEvent sound) {
        if (this.level != null) {
            double d0 = (double)this.worldPosition.getX() + 0.5;
            double d1 = (double)this.worldPosition.getY() + 0.5;
            double d2 = (double)this.worldPosition.getZ() + 0.5;
            this.level.playSound(null, d0, d1, d2, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}
