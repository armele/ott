package com.otterly76.ott.mixin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void ott$renderDeathCoordinates(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.minecraft != null && this.minecraft.player != null) {
            int x = (int) this.minecraft.player.getX();
            int y = (int) this.minecraft.player.getY();
            int z = (int) this.minecraft.player.getZ();

            Component coords = Component.literal("")
                    .append(Component.literal("X: ").withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(String.valueOf(x)).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal("  Y: ").withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(String.valueOf(y)).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal("  Z: ").withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(String.valueOf(z)).withStyle(ChatFormatting.WHITE));

            // Score is at 100, so we put coordinates at 115
            guiGraphics.drawCenteredString(this.font, coords, this.width / 2, 115, 16777215);
        }
    }
}
