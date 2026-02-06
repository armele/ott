package com.otterly76.ott.mixin.client;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.network.ServerboundOpenItemPacket;
import com.otterly76.ott.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "onPress", at = @At("HEAD"))
    private void ott$onPress(long window, int button, int action, int mods, CallbackInfo ci) {
        // Feature toggle (reuses the existing flag)
        if (!OttConfig.GENERAL.ENABLE_RIGHT_CLICK_OPEN.get()) return;

        // Middle mouse pressed
        if (button == 2 && action == 1) {
            if (minecraft.screen == null && minecraft.player != null && !minecraft.player.isSpectator()) {
                // Only when looking at air (avoid interfering with normal pick block behavior)
                HitResult hr = minecraft.hitResult;
                if (hr == null || hr.getType() == HitResult.Type.MISS) {
                    ItemStack held = minecraft.player.getMainHandItem();
                    if (!held.isEmpty() && held.is(ModTags.Items.INVENTORY_OPENABLE) && minecraft.getConnection() != null) {
                        // -1 denotes main hand on the server-side handler
                        minecraft.getConnection().send(new ServerboundOpenItemPacket(-1));
                    }
                }
            }
        }
    }
}