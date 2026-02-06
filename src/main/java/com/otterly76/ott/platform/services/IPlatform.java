package com.otterly76.ott.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public interface IPlatform {
    ResourceLocation getResourceLocation(Item var1);

    boolean isModLoaded(String var1);

    boolean isPhysicalClient();

    boolean isFakePlayer(Player var1);
}
