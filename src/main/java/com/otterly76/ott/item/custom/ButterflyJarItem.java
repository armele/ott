package com.otterly76.ott.item.custom;

import com.otterly76.ott.client.renderer.item.ButterflyJarItemRenderer;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.item.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ButterflyJarItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ButterflyJarItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ButterflyJarItemRenderer renderer;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new ButterflyJarItemRenderer();
                }
                return this.renderer;
            }
        });
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

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(this.getDescriptionId() + "." + getVariant(stack).getName());
    }

    public static Butterfly.Variant getVariant(ItemStack stack) {
        return Butterfly.getVariant(stack);
    }

    public static void setVariant(ItemStack stack, Butterfly.Variant variant) {
        Butterfly.setVariant(stack, variant);
    }
}