package com.otterly76.ott.afk;

import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.network.C2SNotifyActionPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class AFKClientEvents {

    private static long lastNotifyTime = 0;
    private static double lastMouseX = 0;
    private static double lastMouseY = 0;
    private static boolean mouseInitialized = false;

    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (AFKClientStates.isAFK(player.getUUID())) {
                String colorName = OttConfig.afk.AFK_TAG_COLOR.get();
                ChatFormatting color = ChatFormatting.getByName(colorName);
                if (color == null) color = ChatFormatting.GRAY;

                Component afkTag = Component.literal(" <AFK>").withStyle(color);
                event.setContent(Component.empty().append(event.getContent()).append(afkTag));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide && event.getEntity() == Minecraft.getInstance().player) {
            Player player = event.getEntity();

            // Check for mouse movement
            double mouseX = Minecraft.getInstance().mouseHandler.xpos();
            double mouseY = Minecraft.getInstance().mouseHandler.ypos();
            if (!mouseInitialized) {
                lastMouseX = mouseX;
                lastMouseY = mouseY;
                mouseInitialized = true;
            }
            boolean mouseMoved = mouseX != lastMouseX || mouseY != lastMouseY;
            lastMouseX = mouseX;
            lastMouseY = mouseY;

            // Check for inputs
            var options = Minecraft.getInstance().options;
            boolean hasInput = options.keyUp.isDown() || options.keyDown.isDown() ||
                               options.keyLeft.isDown() || options.keyRight.isDown() ||
                               options.keyJump.isDown() || options.keyShift.isDown() ||
                               options.keyUse.isDown() || options.keyAttack.isDown() ||
                               mouseMoved;

            if (hasInput) {
                long now = System.currentTimeMillis();
                if (AFKClientStates.isAFK(player.getUUID()) || now - lastNotifyTime > 5000) {
                    PacketDistributor.sendToServer(new C2SNotifyActionPacket());
                    lastNotifyTime = now;
                }
            }
        }
    }
}