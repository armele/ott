package com.otterly76.ott.item.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ButterflyJarItem extends BlockItem {
    private final Butterfly.Variant variant;

    public ButterflyJarItem(Block block, Butterfly.Variant variant, Properties properties) {
        super(block, properties);
        this.variant = variant;
    }

    public Butterfly.Variant getVariant() {
        return variant;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
        }
        return super.useOn(context);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                BlockPos pos = player.blockPosition();
                Butterfly butterfly = ModEntities.BUTTERFLY.get().spawn((ServerLevel) level, pos, MobSpawnType.EVENT);
                if (butterfly != null) {
                    butterfly.setVariant(getVariant(itemstack));
                    butterfly.setFromHand(true);
                }
                return InteractionResultHolder.success(ItemUtils.createFilledResult(itemstack, player, new ItemStack(ModItems.GLASS_JAR.get())));
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
        }
        return super.use(level, player, hand);
    }


    public static Butterfly.Variant getVariant(ItemStack stack) {
        return Butterfly.getVariant(stack);
    }

    public static void setVariant(ItemStack stack, Butterfly.Variant variant) {
        Butterfly.setVariant(stack, variant);
    }
}