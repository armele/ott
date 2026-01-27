package com.otterly76.ott.mixin.visuals;

import com.otterly76.ott.block.entity.VisualAnvilBlockEntity;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin extends Block implements EntityBlock {

    public AnvilBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (OttConfig.VISUALS.EASY_ANVILS.get() && itemInHand.is(Items.IRON_BLOCK)) {
            BlockState newState = null;
            if (state.is(Blocks.DAMAGED_ANVIL)) {
                newState = Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, state.getValue(AnvilBlock.FACING));
            } else if (state.is(Blocks.CHIPPED_ANVIL)) {
                newState = Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, state.getValue(AnvilBlock.FACING));
            }

            if (newState != null) {
                if (!level.isClientSide) {
                    NonNullList<ItemStack> savedItems = NonNullList.withSize(2, ItemStack.EMPTY);
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof VisualAnvilBlockEntity visualAnvil) {
                        for (int i = 0; i < 2; i++) {
                            savedItems.set(i, visualAnvil.getItems().get(i).copy());
                        }
                    }

                    level.setBlockAndUpdate(pos, newState);

                    BlockEntity newBe = level.getBlockEntity(pos);
                    if (newBe instanceof VisualAnvilBlockEntity newVisualAnvil) {
                        for (int i = 0; i < 2; i++) {
                            newVisualAnvil.getItems().set(i, savedItems.get(i));
                        }
                        newVisualAnvil.setChanged();
                    }

                    if (!player.getAbilities().instabuild) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (OttConfig.VISUALS.EASY_ANVILS.get() && stack.is(Items.IRON_BLOCK)) {
            InteractionResult result = this.useWithoutItem(state, level, pos, player, hitResult);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new VisualAnvilBlockEntity(pos, state);
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof VisualAnvilBlockEntity visualAnvil) {
                Containers.dropContents(level, pos, visualAnvil.getItems());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}