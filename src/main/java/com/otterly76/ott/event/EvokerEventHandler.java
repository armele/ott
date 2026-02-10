package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class EvokerEventHandler {

    @SubscribeEvent
    public static void onEvokerDeath(LivingDeathEvent event) {
        if (!OttConfig.GENERAL.EVOKERS_KILL_SUMMONS_ON_DEATH.get()) return;

        LivingEntity entity = event.getEntity();
        if (entity instanceof Evoker evoker && !entity.level().isClientSide()) {
            ServerLevel level = (ServerLevel) entity.level();

            // Kill Vexes summoned by this evoker within a large radius
            level.getEntitiesOfClass(Vex.class, evoker.getBoundingBox().inflate(64.0), vex -> vex.getOwner() == evoker)
                    .forEach(Vex::kill);

            // Kill Evoker Fangs summoned by this evoker within a large radius
            level.getEntitiesOfClass(EvokerFangs.class, evoker.getBoundingBox().inflate(64.0), fangs -> fangs.getOwner() == evoker)
                    .forEach(EvokerFangs::discard);
        }
    }
}
