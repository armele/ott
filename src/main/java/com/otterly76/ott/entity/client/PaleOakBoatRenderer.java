package com.otterly76.ott.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class PaleOakBoatRenderer extends EntityRenderer<Boat> {
    private final boolean hasChest;
    private final ListModel<Boat> model;
    private static final ResourceLocation BOAT_TEXTURE = ResourceLocation.tryBuild("minecraft", "textures/entity/boat/pale_oak.png");
    private static final ResourceLocation CHEST_BOAT_TEXTURE = ResourceLocation.tryBuild("minecraft", "textures/entity/chest_boat/pale_oak.png");
    public static final ModelLayerLocation BOAT_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("pale_oak_boat"), "main");
    public static final ModelLayerLocation CHEST_BOAT_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("pale_oak_chest_boat"), "main");

    public PaleOakBoatRenderer(EntityRendererProvider.Context context, boolean hasChest) {
        super(context);
        this.hasChest = hasChest;
        ModelPart part = context.bakeLayer(hasChest ? CHEST_BOAT_LAYER : BOAT_LAYER);
        this.model = hasChest ? new ChestBoatModel(part) : new BoatModel(part);
    }

    @SuppressWarnings("DuplicatedCode")
    public void render(Boat boat, float yaw, float tickDelta, PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light) {
        matrices.pushPose();
        matrices.translate(0.0F, 0.375F, 0.0F);
        matrices.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        float hurtTime = (float)boat.getHurtTime() - tickDelta;
        float damage = boat.getDamage() - tickDelta;
        if (damage < 0.0F) {
            damage = 0.0F;
        }

        if (hurtTime > 0.0F) {
            matrices.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurtTime) * hurtTime * damage / 10.0F * (float)boat.getHurtDir()));
        }

        float bubbleAngle = boat.getBubbleAngle(tickDelta);
        if (!Mth.equal(bubbleAngle, 0.0F)) {
            matrices.mulPose((new Quaternionf()).setAngleAxis(bubbleAngle * ((float)Math.PI / 180F), 1.0F, 0.0F, 1.0F));
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.mulPose(Axis.YP.rotationDegrees(90.0F));
        this.model.setupAnim(boat, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.renderType(this.getTexture()));
        this.model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
        if (!boat.isUnderWater()) {
            VertexConsumer waterConsumer = vertexConsumers.getBuffer(RenderType.waterMask());
            if (this.model instanceof WaterPatchModel waterPatchModel) {
                waterPatchModel.waterPatch().render(matrices, waterConsumer, light, OverlayTexture.NO_OVERLAY);
            }
        }

        matrices.popPose();
        super.render(boat, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull Boat boat) {
        return this.hasChest ? CHEST_BOAT_TEXTURE : BOAT_TEXTURE;
    }

    private ResourceLocation getTexture() {
        return this.hasChest ? CHEST_BOAT_TEXTURE : BOAT_TEXTURE;
    }
}