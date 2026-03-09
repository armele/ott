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
    public static final PaintingItemRenderer INSTANCE = new PaintingItemRenderer();
    private static final ResourceLocation PAINTING_BACK = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/painting/painting_back.png");
    private static final ResourceLocation PAINTING_FRONT = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/painting/painting_front.png");

    public PaintingItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ResourceLocation variantLoc = getVariant(stack);
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/painting/" + variantLoc.getPath() + ".png");

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
        float zFront = 0.46875f; // 7.5/16
        float zBack = 0.53125f;  // 8.5/16

        // All faces use full bright light (15728880 = 0xF000F0) to prevent the item from being too dark in inventory/hand
        int brightLight = 15728880;

        // Front face
        VertexConsumer frontBuffer = buffer.getBuffer(RenderType.entityCutout(texture));
        addQuad(matrix, frontBuffer, 1, 0, 0, 1, zFront, zFront, 0, 0, -1, brightLight, packedOverlay);

        // Back face
        VertexConsumer backBuffer = buffer.getBuffer(RenderType.entityCutout(PAINTING_BACK));
        addQuad(matrix, backBuffer, 0, 0, 1, 1, zBack, zBack, 0, 0, 1, brightLight, packedOverlay);

        // Sides (all using back texture)
        // Top
        addQuad(matrix, backBuffer, 0, 1, 1, 1, zFront, zBack, 0, 1, 0, brightLight, packedOverlay);
        // Bottom
        addQuad(matrix, backBuffer, 0, 1, 0, 0, zBack, zFront, 0, -1, 0, brightLight, packedOverlay);
        // Left (West)
        addQuad(matrix, backBuffer, 0, 0, 0, 1, zBack, zFront, -1, 0, 0, brightLight, packedOverlay);
        // Right (East)
        addQuad(matrix, backBuffer, 1, 1, 0, 1, zFront, zBack, 1, 0, 0, brightLight, packedOverlay);

        poseStack.popPose();
    }

    private void addQuad(Matrix4f matrix, VertexConsumer buffer, float x1, float y1, float x2, float y2, float z1, float z2, float nx, float ny, float nz, int light, int overlay) {
        // All quads start at (x1, y1, z1) with UV (0, 0)
        vertex(matrix, buffer, x1, y1, z1, 0, 0, nx, ny, nz, light, overlay);

        if (nx != 0) { // Left/Right
            vertex(matrix, buffer, x1, y2, z1, 0, 1, nx, ny, nz, light, overlay);
            vertex(matrix, buffer, x1, y2, z2, 1, 1, nx, ny, nz, light, overlay);
            vertex(matrix, buffer, x1, y1, z2, 1, 0, nx, ny, nz, light, overlay);
        } else {
            // Both Top/Bottom and Front/Back use this second vertex
            vertex(matrix, buffer, x2, y1, z1, 1, 0, nx, ny, nz, light, overlay);
            if (ny != 0) { // Top/Bottom
                vertex(matrix, buffer, x2, y1, z2, 1, 1, nx, ny, nz, light, overlay);
                vertex(matrix, buffer, x1, y1, z2, 0, 1, nx, ny, nz, light, overlay);
            } else { // Front/Back
                vertex(matrix, buffer, x2, y2, z1, 1, 1, nx, ny, nz, light, overlay);
                vertex(matrix, buffer, x1, y2, z1, 0, 1, nx, ny, nz, light, overlay);
            }
        }
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