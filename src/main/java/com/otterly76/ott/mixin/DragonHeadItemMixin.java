package com.otterly76.ott.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.client.render.DragonHeadGeoRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.BakedGeoModel;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class DragonHeadItemMixin {
    @Unique
    private static final DragonHeadGeoRenderer ott$RENDERER = new DragonHeadGeoRenderer();

    @Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
    private void ott$replaceDragonHeadItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
        SkullBlock.Type type = null;
        if (stack.is(Items.DRAGON_HEAD)) type = SkullBlock.Types.DRAGON;
        else if (stack.is(Items.ZOMBIE_HEAD)) type = SkullBlock.Types.ZOMBIE;
        else if (stack.is(Items.SKELETON_SKULL)) type = SkullBlock.Types.SKELETON;
        else if (stack.is(Items.WITHER_SKELETON_SKULL)) type = SkullBlock.Types.WITHER_SKELETON;

        if (type != null) {
            ci.cancel();
            ott$RENDERER.getAnimatable().setHeadType(type);

            poseStack.pushPose();

            // 1. Context-specific positioning and scaling
            if (displayContext == ItemDisplayContext.GUI) {
                // translate(X, Y, Z) - Lowered Y to 0.25 to sink it more into the slot
                poseStack.translate(0.5f, -0.1f, 1.0f);
                poseStack.scale(0.55f, 0.55f, 0.55f);

                // Adjusting rotation: If 180+45 was hard right,
                // then 180-45 (135) or just 180 should center it better.
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            } else {
                // In hand or dropped on ground
                poseStack.translate(0.5f, 0.35f, 0.5f);
                poseStack.scale(0.7f, 0.7f, 0.7f);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            }

            // 2. Render Call
            ResourceLocation texture = ott$RENDERER.getTextureLocation(ott$RENDERER.getAnimatable());
            BakedGeoModel bakedModel = ott$RENDERER.getGeoModel().getBakedModel(ott$RENDERER.getGeoModel().getModelResource(ott$RENDERER.getAnimatable()));
            RenderType renderType = ott$RENDERER.getRenderType(ott$RENDERER.getAnimatable(), texture, bufferSource, 0);

            ott$RENDERER.actuallyRender(poseStack, ott$RENDERER.getAnimatable(), bakedModel,
                    renderType, bufferSource, bufferSource.getBuffer(renderType != null ? renderType : RenderType.entityCutout(texture)),
                    false, 0, packedLight, packedOverlay, -1);

            poseStack.popPose();
        }
    }
}