package com.otterly76.ott.network;

import com.otterly76.ott.client.gui.RecyclingScreen;
import com.otterly76.ott.client.polymorph.PolymorphCraftingEvents;
import com.otterly76.ott.network.polymorph.ClientboundCraftingRecipesPacket;
import com.otterly76.ott.network.recycling.ClientboundRecipeListPacket;
import com.otterly76.ott.network.recycling.ClientboundRecipeSelectRequestPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleSyncNutrition(final ClientboundSyncNutritionPacket packet, final IPayloadContext context) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.getFoodData().setSaturation(packet.saturation());
            player.getFoodData().setExhaustion(packet.exhaustion());
        }
    }

    public static void handleOpenNameTagEditor(final S2COpenNameTagEditorMessage packet, final IPayloadContext context) {
        Minecraft.getInstance().setScreen(new com.otterly76.ott.client.gui.NameTagEditScreen(packet.hand(), packet.title()));
    }

    public static void handleAnvilRepair(final S2CAnvilRepairMessage packet, final IPayloadContext context) {
        var level = Minecraft.getInstance().level;
        if (level != null) {
            level.levelEvent(1030, packet.pos(), 0);
            Minecraft.getInstance().particleEngine.destroy(packet.pos(), net.minecraft.world.level.block.Block.stateById(packet.stateId()));
        }
    }

    public static void handleSyncAFKStatus(final S2CSyncAFKStatusPacket packet, final IPayloadContext context) {
        com.otterly76.ott.afk.AFKClientStates.setAFK(packet.playerUUID(), packet.afk());
    }

    public static void handleRecipeList(final ClientboundRecipeListPacket packet, final IPayloadContext context) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof RecyclingScreen recyclingScreen) {
            recyclingScreen.updateRecipes(packet.recipes(), packet.size(), packet.shouldSendPacket());
        }
    }

    public static void handleRecipeSelectRequest(final ClientboundRecipeSelectRequestPacket packet, final IPayloadContext context) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof RecyclingScreen recyclingScreen) {
            recyclingScreen.resubmitSelection();
        }
    }

    public static void handleCraftingRecipes(final ClientboundCraftingRecipesPacket packet, final IPayloadContext context) {
        PolymorphCraftingEvents.setRecipesList(packet);
    }
}
