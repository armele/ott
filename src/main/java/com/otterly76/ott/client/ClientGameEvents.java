package com.otterly76.ott.client;

import com.otterly76.ott.Constants;
import com.otterly76.ott.client.controls.NewKeyBindsScreen;
import com.otterly76.ott.handler.BlockConversionHandler;
import com.otterly76.ott.mixin.client.OptionsSubScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientGameEvents {

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        BlockConversionHandler.revertAll();
    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof KeyBindsScreen gui && !(event.getScreen() instanceof NewKeyBindsScreen)) {
            event.setNewScreen(new NewKeyBindsScreen(
                    ((OptionsSubScreenAccessor) gui).ott$getLastScreen(),
                    Minecraft.getInstance().options));
        }
    }
}
