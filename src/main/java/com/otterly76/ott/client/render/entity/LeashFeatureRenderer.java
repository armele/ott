package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.mixin.access.EntityRendererAccessor;
import com.otterly76.ott.util.entity.LeashExtension;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class LeashFeatureRenderer<T extends Entity> {
    private static final int LEASH_RENDER_STEPS = 24;
    private static final float LEASH_WIDTH = 0.05F;
    private final EntityRenderDispatcher dispatcher;
    private @Nullable List<LeashState> leashStates;

    public LeashFeatureRenderer(EntityRenderDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public boolean shouldRender(T entity, Frustum camera, boolean isVisible, boolean fallback) {
        if (!isVisible) {
            AABB boundingBox = entity.getBoundingBoxForCulling().inflate(0.5F);
            if (boundingBox.hasNaN() || boundingBox.getSize() == 0.0F) {
                boundingBox = new AABB(entity.getX() - 2.0F, entity.getY() - 2.0F, entity.getZ() - 2.0F, entity.getX() + 2.0F, entity.getY() + 2.0F, entity.getZ() + 2.0F);
            }

            if (camera.isVisible(boundingBox)) {
                return true;
            }

            if (entity instanceof Leashable leashable) {
                Entity leashHolder = leashable.getLeashHolder();
                if (leashHolder != null) {
                    AABB leasherBox = leashHolder.getBoundingBoxForCulling();
                    return camera.isVisible(leasherBox) || camera.isVisible(boundingBox.minmax(leasherBox));
                }
            }
        }

        return fallback;
    }

    public boolean shouldRender(T entity, Frustum camera, boolean isVisible) {
        return this.shouldRender(entity, camera, isVisible, isVisible);
    }

    public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer) {
        this.setupLeashRendering(entity, partialTick);
        if (this.leashStates != null) {
            for(LeashState leashState : this.leashStates) {
                this.renderLeash(poseStack, buffer, leashState);
            }
        }

    }

    private void renderLeash(PoseStack stack, MultiBufferSource buffer, LeashState state) {
        float dx = (float)(state.end.x - state.start.x);
        float dy = (float)(state.end.y - state.start.y);
        float dz = (float)(state.end.z - state.start.z);
        float offsetFactor = Mth.invSqrt(dx * dx + dz * dz) * 0.05F / 2.0F;
        float dxOff = dz * offsetFactor;
        float dzOff = dx * offsetFactor;
        stack.pushPose();
        stack.translate(state.offset.x, state.offset.y, state.offset.z);
        VertexConsumer builder = buffer.getBuffer(RenderType.leash());
        Matrix4f matrices = stack.last().pose();

        for(int segment = 0; segment <= 24; ++segment) {
            addVertexPair(builder, matrices, dx, dy, dz, dxOff, dzOff, segment, false, state);
        }

        for(int segment = 24; segment >= 0; --segment) {
            addVertexPair(builder, matrices, dx, dy, dz, dxOff, dzOff, segment, true, state);
        }

        if (!state.slack) {
            for(int segment = 0; segment <= 24; ++segment) {
                addVertexPair(builder, matrices, dx, dy, dz, -dxOff, dzOff, segment, false, state);
            }

            for(int segment = 24; segment >= 0; --segment) {
                addVertexPair(builder, matrices, dx, dy, dz, -dxOff, dzOff, segment, true, state);
            }
        }

        stack.popPose();
    }

    private static void addVertexPair(VertexConsumer builder, Matrix4f pose, float dx, float dy, float dz, float dxOff, float dzOff, int segment, boolean backwards, LeashState state) {
        float progress = (float)segment / 24.0F;
        int block = (int)Mth.lerp(progress, (float)state.startBlockLight, (float)state.endBlockLight);
        int sky = (int)Mth.lerp(progress, (float)state.startSkyLight, (float)state.endSkyLight);
        int lightCoords = LightTexture.pack(block, sky);
        float colorModifier = segment % 2 == (backwards ? 1 : 0) ? 0.7F : 1.0F;
        float r = 0.5F * colorModifier;
        float g = 0.4F * colorModifier;
        float b = 0.3F * colorModifier;
        float x = dx * progress;
        float y = dy * progress;
        float z = dz * progress;
        if (state.slack) {
            y = dy > 0.0F ? dy * progress * progress : dy - dy * (1.0F - progress) * (1.0F - progress);
        }

        builder.addVertex(pose, x - dxOff, y + (float) 0.05, z + dzOff).setColor(r, g, b, 1.0F).setLight(lightCoords);
        builder.addVertex(pose, x + dxOff, y + 0.05F - (float) 0.05, z - dzOff).setColor(r, g, b, 1.0F).setLight(lightCoords);
    }

    private void setupLeashRendering(T entity, float partialTicks) {
        if (!(entity instanceof Leashable leashable)) {
            this.leashStates = null;
        } else {
            LeashExtension extension = (LeashExtension)leashable;
            Entity holder = leashable.getLeashHolder();
            if (holder != null) {
                float entityYRot;
                Vec3 attackOffset;
                int startBlockLight;
                int endBlockLight;
                int startSkyLight;
                int endSkyLight;
                boolean quadConnection;
                
                entityYRot = LeashExtension.vb$getPreciseBodyRotation(entity, partialTicks) * ((float)Math.PI / 180F);
                attackOffset = entity.getLeashOffset(partialTicks);
                BlockPos entityEyePos = BlockPos.containing(entity.getEyePosition(partialTicks));
                BlockPos roperEyePos = BlockPos.containing(holder.getEyePosition(partialTicks));
                startBlockLight = this.getBlockLightLevel(entity, entityEyePos);
                endBlockLight = this.getBlockLightLevel(holder, roperEyePos);
                startSkyLight = entity.level().getBrightness(LightLayer.SKY, entityEyePos);
                endSkyLight = entity.level().getBrightness(LightLayer.SKY, roperEyePos);
                
                if (holder instanceof LeashExtension ext) {
                    quadConnection = ext.vb$supportQuadLeashAsHolder() && extension.vb$supportQuadLeash();
                } else {
                    quadConnection = false;
                }

                int leashCount = quadConnection ? 4 : 1;
                if (this.leashStates == null || this.leashStates.size() != leashCount) {
                    this.leashStates = new ArrayList<>(leashCount);

                    for(int i = 0; i < leashCount; ++i) {
                        this.leashStates.add(new LeashState());
                    }
                }

                if (quadConnection) {
                    float roperYRot = LeashExtension.vb$getPreciseBodyRotation(holder, partialTicks) * ((float)Math.PI / 180F);
                    Vec3 holderPos = holder.getPosition(partialTicks);
                    Vec3[] leashableAttachmentPoints = extension.vb$getQuadLeashOffsets();
                    Vec3[] roperAttachmentPoints = ((LeashExtension)holder).vb$getQuadLeashHolderOffsets();

                    for(int i = 0; i < leashCount; ++i) {
                        LeashState leashState = this.leashStates.get(i);
                        leashState.offset = leashableAttachmentPoints[i].yRot(-entityYRot);
                        leashState.start = entity.getPosition(partialTicks).add(leashState.offset);
                        leashState.end = holderPos.add(roperAttachmentPoints[i].yRot(-roperYRot));
                        leashState.startBlockLight = startBlockLight;
                        leashState.endBlockLight = endBlockLight;
                        leashState.startSkyLight = startSkyLight;
                        leashState.endSkyLight = endSkyLight;
                        leashState.slack = false;
                    }
                } else {
                    Vec3 rotatedAttachOffset = attackOffset.yRot(-entityYRot);
                    LeashState leashState = this.leashStates.getFirst();
                    leashState.offset = rotatedAttachOffset;
                    leashState.start = entity.getPosition(partialTicks).add(rotatedAttachOffset);
                    leashState.end = holder.getRopeHoldPosition(partialTicks);
                    leashState.startBlockLight = startBlockLight;
                    leashState.endBlockLight = endBlockLight;
                    leashState.startSkyLight = startSkyLight;
                    leashState.endSkyLight = endSkyLight;
                    leashState.slack = true;
                }
            } else {
                this.leashStates = null;
            }

        }
    }

    private int getBlockLightLevel(Entity entity, BlockPos pos) {
        return ((EntityRendererAccessor)this.dispatcher.getRenderer(entity)).callGetBlockLightLevel(entity, pos);
    }
}