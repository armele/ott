package com.otterly76.ott.mixin.common;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void ott$onSpongeUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (OttConfig.GENERAL.SPONGES_PLACED_ON_WATER.get()) {
            Item item = (Item) (Object) this;
            if (item == Items.SPONGE || item == Items.WET_SPONGE) {
                BlockHitResult blockhitresult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
                if (blockhitresult.getType() == HitResult.Type.BLOCK) {
                    BlockState state = level.getBlockState(blockhitresult.getBlockPos());
                    if (!state.getFluidState().isEmpty()) {
                        ItemStack itemstack = player.getItemInHand(hand);
                        BlockHitResult blockhitresult1 = blockhitresult.withPosition(blockhitresult.getBlockPos().above());
                        InteractionResult interactionresult = item.useOn(new UseOnContext(player, hand, blockhitresult1));
                        if (interactionresult.consumesAction()) {
                            cir.setReturnValue(new InteractionResultHolder<>(interactionresult, itemstack));
                        }
                    }
                }
            }
        }
    }
}