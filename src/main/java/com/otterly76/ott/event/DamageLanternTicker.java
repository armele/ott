package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import com.otterly76.ott.OttDamageTypes;
import com.otterly76.ott.util.DamageLanternManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Map;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class DamageLanternTicker {

    private static final float DAMAGE_PER_PULSE = 2.0F;
    private static final int TICKS_PER_PULSE = 20; // once per second

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if ((level.getGameTime() % TICKS_PER_PULSE) != 0) return;

        for (Map.Entry<BlockPos, Integer> e : DamageLanternManager.getAll().entrySet()) {
            BlockPos pos = e.getKey();
            int r = e.getValue();
            AABB box = new AABB(pos).inflate(r);
            level.getEntitiesOfClass(Mob.class, box, mob -> mob.getType().getCategory() == MobCategory.MONSTER)
                    .forEach(mob -> mob.hurt(OttDamageTypes.of(level, OttDamageTypes.LANTERN_DAMAGE), DAMAGE_PER_PULSE));
        }
    }
}
