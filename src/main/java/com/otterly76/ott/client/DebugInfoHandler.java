package com.otterly76.ott.client;

import java.text.DecimalFormat;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.food.FoodData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.common.NeoForge;
import com.otterly76.ott.helpers.HungerHelper;

@OnlyIn(Dist.CLIENT)
public class DebugInfoHandler {
    private static final DecimalFormat saturationDF = new DecimalFormat("#.##");
    private static final DecimalFormat exhaustionValDF = new DecimalFormat("0.00");
    private static final DecimalFormat exhaustionMaxDF = new DecimalFormat("#.##");

    public static void init() {
        NeoForge.EVENT_BUS.register(new DebugInfoHandler());
    }

    @SubscribeEvent
    public void onTextRender(CustomizeGuiOverlayEvent.DebugText textEvent) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getDebugOverlay().showDebugScreen()) {
            assert mc.player != null;
            FoodData stats = mc.player.getFoodData();
            float curExhaustion = stats.getExhaustionLevel();
            float maxExhaustion = HungerHelper.getMaxExhaustion(mc.player);
            List<String> var10000 = textEvent.getLeft();
            int var10001 = stats.getFoodLevel();
            var10000.add("hunger: " + var10001 + ", sat: " + saturationDF.format(stats.getSaturationLevel()) + ", exh: " + exhaustionValDF.format(curExhaustion) + "/" + exhaustionMaxDF.format(maxExhaustion));
        }
    }
}