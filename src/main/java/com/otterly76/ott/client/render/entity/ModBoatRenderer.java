package com.otterly76.ott.client.render.entity;

import com.mojang.datafixers.util.Pair;
import com.otterly76.ott.Constants;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.vehicle.OttWoodSetBoatEntity;
import com.otterly76.ott.entity.vehicle.OttWoodSetChestBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModBoatRenderer extends BoatRenderer implements CustomBoatModel {
    private final Map<String, Pair<ResourceLocation, ListModel<Boat>>> boatResources = new HashMap<>();
    private final EntityRendererProvider.Context context;
    private final boolean isChest;

    public ModBoatRenderer(EntityRendererProvider.Context context, boolean isChest) {
        super(context, isChest);
        this.context = context;
        this.isChest = isChest;
    }

    @Override
    public @NotNull Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(@NotNull Boat boat) {
        String woodType = getWoodType(boat);
        return boatResources.computeIfAbsent(woodType, k -> {
            ResourceLocation texture;
            ListModel<Boat> model;
            
            if ("pale_oak".equals(k)) {
                texture = ResourceLocation.withDefaultNamespace(isChest ? "textures/entity/chest_boat/pale_oak.png" : "textures/entity/boat/pale_oak.png");
                model = isChest ? new ChestBoatModel(context.bakeLayer(ModModelLayers.PALE_OAK_CHEST_BOAT)) : new BoatModel(context.bakeLayer(ModModelLayers.PALE_OAK_BOAT));
            } else {
                String folder = isChest ? "textures/entity/chest_boat/" : "textures/entity/boat/";
                texture = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, folder + k + ".png");
                model = isChest ? new ChestBoatModel(context.bakeLayer(ModModelLayers.OTT_WOOD_SET_CHEST_BOAT)) : new BoatModel(context.bakeLayer(ModModelLayers.OTT_WOOD_SET_BOAT));
            }
            return Pair.of(texture, model);
        });
    }

    private String getWoodType(Boat boat) {
        if (boat instanceof OttWoodSetBoatEntity b) return b.getWoodSetName();
        if (boat instanceof OttWoodSetChestBoatEntity b) return b.getWoodSetName();
        // Fallback or specific backport entities if they aren't the generic ones
        return "pale_oak";
    }
}