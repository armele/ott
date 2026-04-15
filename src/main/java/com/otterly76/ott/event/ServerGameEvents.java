package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CamelHuskEntity;
import com.otterly76.ott.handler.WeatheringHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ServerGameEvents {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        // Zombie jockey riding a zombie horse drops a red mushroom (10%)
        if (entity instanceof Zombie && !(entity instanceof Husk)
                && entity.getVehicle() instanceof ZombieHorse
                && entity.getRandom().nextFloat() < 0.1F) {
            entity.level().addFreshEntity(new ItemEntity(
                    entity.level(), entity.getX(), entity.getY(), entity.getZ(),
                    new ItemStack(Items.RED_MUSHROOM)));
        }

        // Husk jockey riding a camel husk drops a rabbit's foot (10%)
        if (entity instanceof Husk
                && entity.getVehicle() instanceof CamelHuskEntity
                && entity.getRandom().nextFloat() < 0.1F) {
            entity.level().addFreshEntity(new ItemEntity(
                    entity.level(), entity.getX(), entity.getY(), entity.getZ(),
                    new ItemStack(Items.RABBIT_FOOT)));
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof LivingEntity living)) return;

        // Weather armor periodically
        if (living.tickCount % 1200 == 0) { // Every minute
            if (living.getRandom().nextInt(64) == 0) { // Average ~64 minutes per stage
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    ItemStack stack = living.getItemBySlot(slot);
                    if (!stack.isEmpty()) {
                        WeatheringHandler.getNextItem(stack).ifPresent(next -> {
                            living.setItemSlot(slot, next);
                        });
                    }
                }
            }
        }
    }
}
