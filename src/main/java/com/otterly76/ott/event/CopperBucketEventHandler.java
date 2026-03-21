package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import com.otterly76.ott.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class CopperBucketEventHandler {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (stack.is(ModItems.COPPER_BUCKET.get())) {
            switch (event.getTarget()) {
                case Cow cow when !cow.isBaby() -> {
                    event.getLevel().playSound(null, event.getEntity().blockPosition(), SoundEvents.COW_MILK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    ItemStack milkResult = ItemUtils.createFilledResult(stack, event.getEntity(), new ItemStack(ModItems.COPPER_MILK_BUCKET.get()));
                    event.getEntity().setItemInHand(event.getHand(), milkResult);
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
                }
                case MushroomCow mooshroom when !mooshroom.isBaby() -> {
                    event.getLevel().playSound(null, event.getEntity().blockPosition(), SoundEvents.COW_MILK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    ItemStack milkResult = ItemUtils.createFilledResult(stack, event.getEntity(), new ItemStack(ModItems.COPPER_MILK_BUCKET.get()));
                    event.getEntity().setItemInHand(event.getHand(), milkResult);
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
                }
                case Goat goat when !goat.isBaby() -> {
                    event.getLevel().playSound(null, event.getEntity().blockPosition(), SoundEvents.GOAT_MILK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    ItemStack milkResult = ItemUtils.createFilledResult(stack, event.getEntity(), new ItemStack(ModItems.COPPER_MILK_BUCKET.get()));
                    event.getEntity().setItemInHand(event.getHand(), milkResult);
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
                }
                default -> {}
            }
        }
    }
}
