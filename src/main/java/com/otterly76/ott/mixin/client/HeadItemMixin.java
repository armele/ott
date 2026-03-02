package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
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
import com.mojang.math.Axis;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class HeadItemMixin {
    @Unique
    private static final DragonHeadGeoRenderer ott$RENDERER = new DragonHeadGeoRenderer();

    @Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
    private void ott$replaceDragonHeadItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
        SkullBlock.Type type = null;
        if (stack.is(Items.DRAGON_HEAD)) type = SkullBlock.Types.DRAGON;
        else if (stack.is(Items.ZOMBIE_HEAD)) type = SkullBlock.Types.ZOMBIE;
        else if (stack.is(Items.SKELETON_SKULL)) type = SkullBlock.Types.SKELETON;
        else if (stack.is(Items.WITHER_SKELETON_SKULL)) type = SkullBlock.Types.WITHER_SKELETON;
        else if (stack.is(com.otterly76.ott.item.ModItems.DRAGON_SKULL.get())) type = com.otterly76.ott.util.block.ModSkullType.DRAGON_SKULL;

        if (type != null) {
            ci.cancel();
            ott$RENDERER.getAnimatable().setHeadType(type);

            poseStack.pushPose();

            boolean isDragon = (type == SkullBlock.Types.DRAGON || type == com.otterly76.ott.util.block.ModSkullType.DRAGON_SKULL);

            // 1. POSITIONING: Center the head
            poseStack.translate(0.5f, 0.0f, 0.5f);

            // 2. ROTATION: Turning them 180 degrees to face the player,
            // EXCEPT in item frames (FIXED) where we want them facing out from the wall.
            if (displayContext != ItemDisplayContext.FIXED) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            }

            // 3. SCALING & NUDGE: Dragon heads are massive, scale and nudge them down/right
            if (isDragon) {
                poseStack.scale(0.65f, 0.65f, 0.65f);
                poseStack.translate(-0.6f, -0.7f, 0.0f);
            }

            // 4. Render Call
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