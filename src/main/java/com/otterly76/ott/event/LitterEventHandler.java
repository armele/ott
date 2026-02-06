package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class LitterEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBabySpawn(BabyEntitySpawnEvent event) {
        if (event.isCanceled()) return;

        if (!(event.getParentA() instanceof AgeableMob ageableParentA) || !(event.getParentB() instanceof AgeableMob ageableParentB)) return;

        Level level = ageableParentA.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        boolean isTarget = switch (ageableParentA) {
            case Pig pig -> true;
            case Wolf wolf when wolf.isTame() -> true;
            case Cat cat when cat.isTame() -> true;
            default -> false;
        };

        if (isTarget) {
            // Litter size between 2-4. Event already handles the first baby.
            // We spawn 1 to 3 additional babies.
            int additionalBabies = serverLevel.getRandom().nextInt(1, 4); // 1, 2, or 3
            for (int i = 0; i < additionalBabies; i++) {
                AgeableMob child = ageableParentA.getBreedOffspring(serverLevel, ageableParentB);
                if (child != null) {
                    child.setBaby(true);
                    child.moveTo(ageableParentA.getX(), ageableParentA.getY(), ageableParentA.getZ(), 0.0F, 0.0F);
                    serverLevel.addFreshEntityWithPassengers(child);
                }
            }
        }
    }
}