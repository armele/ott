package com.otterly76.ott.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.otterly76.ott.Constants;
import com.otterly76.ott.network.ServerboundRenameNameTagPacket;
import com.otterly76.ott.network.ServerboundSetHomePacket;
import com.otterly76.ott.network.ServerboundTeleportHomePacket;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientKeyHandler {
    private static final String CATEGORY = "key.categories." + Constants.MOD_ID;

    public static final KeyMapping TELEPORT_HOME = new KeyMapping(
            "key." + Constants.MOD_ID + ".teleport_home",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            CATEGORY
    );

    public static final KeyMapping SET_HOME = new KeyMapping(
            "key." + Constants.MOD_ID + ".set_home",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TELEPORT_HOME);
        event.register(SET_HOME);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (TELEPORT_HOME.consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundTeleportHomePacket("home"));
        }

        while (SET_HOME.consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundSetHomePacket("home"));
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isUseItem()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.screen == null && mc.hitResult != null && mc.hitResult.getType() == net.minecraft.world.phys.HitResult.Type.MISS) {
                if (OttConfig.VISUALS.NAME_TAG_DIRECT_RENAME.get() && mc.player.isShiftKeyDown() && mc.player.getMainHandItem().is(Items.NAME_TAG)) {
                    PacketDistributor.sendToServer(new ServerboundRenameNameTagPacket());
                    event.setCanceled(true);
                    event.setSwingHand(true);
                }
            }
        }
    }
}