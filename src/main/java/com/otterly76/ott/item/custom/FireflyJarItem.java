package com.otterly76.ott.item.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import com.otterly76.ott.client.render.item.FireflyJarItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class FireflyJarItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FireflyJarItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // No animations for the item yet, or we could add 'idle'
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private FireflyJarItemRenderer renderer;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new FireflyJarItemRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && !itemstack.is(ModItems.GLASS_JAR.get())) {
            if (!level.isClientSide) {
                BlockPos pos = player.blockPosition();
                ModEntities.SMALL_FIREFLY.get().spawn((ServerLevel) level, pos, MobSpawnType.EVENT);
                
                ItemStack nextTier;
                if (itemstack.is(ModItems.FIREFLY_JAR.get())) {
                    nextTier = new ItemStack(ModItems.FIREFLIES_IN_A_JAR.get());
                } else if (itemstack.is(ModItems.FIREFLIES_IN_A_JAR.get())) {
                    nextTier = new ItemStack(ModItems.FIREFLY_IN_A_JAR.get());
                } else {
                    nextTier = new ItemStack(ModItems.GLASS_JAR.get());
                }
                
                return InteractionResultHolder.success(ItemUtils.createFilledResult(itemstack, player, nextTier));
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
        }
        return super.use(level, player, hand);
    }
}