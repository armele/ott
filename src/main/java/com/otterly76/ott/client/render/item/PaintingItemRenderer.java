package com.otterly76.ott.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Optional;

public class PaintingItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static PaintingItemRenderer INSTANCE;
    private static final ResourceLocation PAINTING_BACK = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/painting/back.png");
    
    public static PaintingItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PaintingItemRenderer();
        }
        return INSTANCE;
    }

    public PaintingItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ResourceLocation variantLoc = getVariant(stack);
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/painting/" + variantLoc.getPath() + ".png");

        int width = 1;
        int height = 1;

        if (Minecraft.getInstance().level != null) {
            var registry = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);
            Optional<PaintingVariant> variant = registry.getOptional(variantLoc);
            if (variant.isPresent()) {
                width = variant.get().width();
                height = variant.get().height();
            }
        }

        poseStack.pushPose();
        
        // Center and scale
        poseStack.translate(0.5f, 0.5f, 0.5f);
        float maxDim = Math.max(width, height);
        float scale = 1.0f / maxDim;
        poseStack.scale(width * scale, height * scale, 1.0f);
        poseStack.translate(-0.5f, -0.5f, -0.5f);

        Matrix4f matrix = poseStack.last().pose();
        
        // Face North (-Z) for the painting side by default
        float zNorth = 0.46875f; // 7.5/16 (North side of center)
        float zSouth = 0.53125f; // 8.5/16 (South side of center)

        // All faces use full bright light to prevent the item from being too dark
        int brightLight = 15728880;

        // Front face (Painting) - Facing North (-Z)
        // Opaque backing first
        VertexConsumer frontOpaque = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        vertex(matrix, frontOpaque, 1, 0, zNorth, 1, 1, 0, 0, -1, packedLight, packedOverlay);
        vertex(matrix, frontOpaque, 0, 0, zNorth, 0, 1, 0, 0, -1, packedLight, packedOverlay);
        vertex(matrix, frontOpaque, 0, 1, zNorth, 0, 0, 0, 0, -1, packedLight, packedOverlay);
        vertex(matrix, frontOpaque, 1, 1, zNorth, 1, 0, 0, 0, -1, packedLight, packedOverlay);
        
        // Emissive glow layer on top
        VertexConsumer frontEmissive = buffer.getBuffer(RenderType.entityTranslucentEmissive(texture));
        vertex(matrix, frontEmissive, 1, 0, zNorth - 0.001f, 1, 1, 0, 0, -1, brightLight, packedOverlay);
        vertex(matrix, frontEmissive, 0, 0, zNorth - 0.001f, 0, 1, 0, 0, -1, brightLight, packedOverlay);
        vertex(matrix, frontEmissive, 0, 1, zNorth - 0.001f, 0, 0, 0, 0, -1, brightLight, packedOverlay);
        vertex(matrix, frontEmissive, 1, 1, zNorth - 0.001f, 1, 0, 0, 0, -1, brightLight, packedOverlay);

        // Back face (Wood) - Facing South (+Z)
        // CCW from South: BL -> BR -> TR -> TL
        VertexConsumer backBuffer = buffer.getBuffer(RenderType.entityCutoutNoCull(PAINTING_BACK));
        vertex(matrix, backBuffer, 0, 0, zSouth, 0, 1, 0, 0, 1, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 0, zSouth, 1, 1, 0, 0, 1, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 1, zSouth, 1, 0, 0, 0, 1, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 0, 1, zSouth, 0, 0, 0, 0, 1, packedLight, packedOverlay);

        // Sides (all using back texture - wood)
        // Top (+Y) - CCW from Top: TL South -> TR South -> TR North -> TL North
        vertex(matrix, backBuffer, 0, 1, zSouth, 0, 1, 0, 1, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 1, zSouth, 1, 1, 0, 1, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 1, zNorth, 1, 0, 0, 1, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 0, 1, zNorth, 0, 0, 0, 1, 0, packedLight, packedOverlay);

        // Bottom (-Y) - CCW from Bottom: BL North -> BR North -> BR South -> BL South
        vertex(matrix, backBuffer, 0, 0, zNorth, 0, 1, 0, -1, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 0, zNorth, 1, 1, 0, -1, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 0, zSouth, 1, 0, 0, -1, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 0, 0, zSouth, 0, 0, 0, -1, 0, packedLight, packedOverlay);

        // Left (West, -X) - CCW from West: BL North -> BL South -> TL South -> TL North
        vertex(matrix, backBuffer, 0, 0, zNorth, 1, 1, -1, 0, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 0, 0, zSouth, 0, 1, -1, 0, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 0, 1, zSouth, 0, 0, -1, 0, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 0, 1, zNorth, 1, 0, -1, 0, 0, packedLight, packedOverlay);

        // Right (East, +X) - CCW from East: BR South -> BR North -> TR North -> TR South
        vertex(matrix, backBuffer, 1, 0, zSouth, 1, 1, 1, 0, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 0, zNorth, 0, 1, 1, 0, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 1, zNorth, 0, 0, 1, 0, 0, packedLight, packedOverlay);
        vertex(matrix, backBuffer, 1, 1, zSouth, 1, 0, 1, 0, 0, packedLight, packedOverlay);

        poseStack.popPose();
    }

    private void vertex(Matrix4f matrix, VertexConsumer buffer, float x, float y, float z, float u, float v, float nx, float ny, float nz, int light, int overlay) {
        buffer.addVertex(matrix, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(nx, ny, nz);
    }

    private ResourceLocation getVariant(ItemStack stack) {
        CustomData customdata = stack.get(DataComponents.ENTITY_DATA);
        if (customdata != null) {
            CompoundTag tag = customdata.copyTag();
            if (tag.contains("variant", 8)) {
                ResourceLocation loc = ResourceLocation.tryParse(tag.getString("variant"));
                if (loc != null) return loc;
            }
        }
        return ResourceLocation.withDefaultNamespace("alban");
    }
}