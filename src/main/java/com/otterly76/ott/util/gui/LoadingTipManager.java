package com.otterly76.ott.util.gui;

import net.minecraft.network.chat.Component;
import java.util.List;
import java.util.Random;

public class LoadingTipManager {
    private static final List<String> TIPS = List.of(
            "ott.tip.discord",
            "ott.tip.webpage",
            "ott.tip.bisect",
            "ott.tip.howdoi",
            "ott.tip.couriers",
            "ott.tip.menu",
            "ott.tip.foods",
            "ott.tip.upgrading",
            "ott.tip.expand",
            "ott.tip.village",
            "ott.tip.zombie",
            "ott.tip.farmers",
            "ott.tip.desync",
            "ott.tip.clipboards",
            "ott.tip.vanillafood",
            "ott.tip.planning",
            "ott.tip.builders",
            "ott.tip.guards",
            "ott.tip.samerecipe",
            "ott.tip.smelter",
            "ott.tip.bakerbottles",
            "ott.tip.crafterbottles",
            "ott.tip.tieredarmor"
    );

    private static String currentTip = "";
    private static final Random RANDOM = new Random();

    public static void pickNewTip() {
        currentTip = TIPS.get(RANDOM.nextInt(TIPS.size()));
    }

    public static Component getTipComponent() {
        return Component.translatable(currentTip);
    }
}
