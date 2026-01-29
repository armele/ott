package com.otterly76.ott.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.otterly76.ott.api.core.Constants;
import com.otterly76.ott.entity.OttWoodSetBoatEntity;
import com.otterly76.ott.entity.OttWoodSetChestBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
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

@SuppressWarnings("DuplicatedCode")
public class OttWoodSetBoatRenderer extends EntityRenderer<Boat> {
    private final boolean hasChest;
    private final ListModel<Boat> model;

    public OttWoodSetBoatRenderer(EntityRendererProvider.Context context, boolean hasChest) {
        super(context);
        this.hasChest = hasChest;

        ModelPart part = context.bakeLayer(hasChest ? ModModelLayers.OTT_WOOD_SET_CHEST_BOAT : ModModelLayers.OTT_WOOD_SET_BOAT);
        this.model = hasChest ? new ChestBoatModel(part) : new BoatModel(part);
    }

    @Override
    public void render(Boat boat, float yaw, float tickDelta, PoseStack matrices, @NotNull MultiBufferSource buffers, int light) {
        matrices.pushPose();
        matrices.translate(0.0F, 0.375F, 0.0F);
        matrices.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));

        float hurtTime = (float) boat.getHurtTime() - tickDelta;
        float damage = boat.getDamage() - tickDelta;
        if (damage < 0.0F) damage = 0.0F;

        if (hurtTime > 0.0F) {
            matrices.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurtTime) * hurtTime * damage / 10.0F * (float) boat.getHurtDir()));
        }

        float bubbleAngle = boat.getBubbleAngle(tickDelta);
        if (!Mth.equal(bubbleAngle, 0.0F)) {
            matrices.mulPose((new Quaternionf()).setAngleAxis(bubbleAngle * ((float) Math.PI / 180F), 1.0F, 0.0F, 1.0F));
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.mulPose(Axis.YP.rotationDegrees(90.0F));

        this.model.setupAnim(boat, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);

        ResourceLocation tex = getTextureLocation(boat);
        VertexConsumer vc = buffers.getBuffer(this.model.renderType(tex));
        this.model.renderToBuffer(matrices, vc, light, OverlayTexture.NO_OVERLAY);

        if (!boat.isUnderWater()) {
            VertexConsumer water = buffers.getBuffer(RenderType.waterMask());
            if (this.model instanceof WaterPatchModel waterPatchModel) {
                waterPatchModel.waterPatch().render(matrices, water, light, OverlayTexture.NO_OVERLAY);
            }
        }

        matrices.popPose();
        super.render(boat, yaw, tickDelta, matrices, buffers, light);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Boat boat) {
        String setName = "";

        if (boat instanceof OttWoodSetBoatEntity b) {
            setName = b.getWoodSetName();
        } else if (boat instanceof OttWoodSetChestBoatEntity b) {
            setName = b.getWoodSetName();
        }

        String folder = hasChest ? "textures/entity/chest_boat/" : "textures/entity/boat/";
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, folder + setName + ".png");
    }
}



