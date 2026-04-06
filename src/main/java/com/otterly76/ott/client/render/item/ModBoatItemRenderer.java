package com.otterly76.ott.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.otterly76.ott.Constants;
import com.otterly76.ott.client.registries.ModModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ModBoatItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final Supplier<? extends EntityType<? extends Boat>> entityType;
    private final Consumer<Boat> initializer;
    private final String woodSetName;
    private final boolean isChest;
    private Boat fakeBoat;
    private ListModel<Boat> model;
    private ResourceLocation texture;

    public ModBoatItemRenderer(Supplier<? extends EntityType<? extends Boat>> entityType, Consumer<Boat> initializer, String woodSetName, boolean isChest) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.entityType = entityType;
        this.initializer = initializer;
        this.woodSetName = woodSetName;
        this.isChest = isChest;
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (fakeBoat == null) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                fakeBoat = entityType.get().create(level);
                if (fakeBoat != null) initializer.accept(fakeBoat);
            }
        }
        if (fakeBoat == null) return;

        if (model == null) {
            var models = Minecraft.getInstance().getEntityModels();
            if (isChest) {
                model = new ChestBoatModel(models.bakeLayer(ModModelLayers.OTT_WOOD_SET_CHEST_BOAT));
                texture = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/chest_boat/" + woodSetName + ".png");
            } else {
                model = new BoatModel(models.bakeLayer(ModModelLayers.OTT_WOOD_SET_BOAT));
                texture = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/boat/" + woodSetName + ".png");
            }
        }

        // Replicate BoatRenderer.render() transforms and rendering, skipping hurt/bubble animations
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        model.setupAnim(fakeBoat, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(model.renderType(texture));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
