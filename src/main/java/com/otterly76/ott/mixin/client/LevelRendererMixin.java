package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.CreepOverlayRenderer;
import com.otterly76.ott.client.neat.HealthBarRenderer;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.particle.WeatherParticleSpawner;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private int ticks;
    @Shadow
    private int rainSoundTime;
    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(
            method = {"tickRain"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void tickRain(Camera camera, CallbackInfo ci) {
        if (!OttConfig.WEATHER.TICK_VANILLA_WEATHER.get()) {
            assert this.minecraft.level != null;
            float f = this.minecraft.level.getRainLevel(1.0F);
            if (f > 0.0F) {
                java.util.Random random = new java.util.Random((long)this.ticks * 312987231L);
                LevelReader level = this.minecraft.level;
                BlockPos blockPos = BlockPos.containing(camera.getPosition());
                BlockPos blockPos2 = null;

                for(int j = 0; (float)j < 100.0F * f * f; ++j) {
                    int k = random.nextInt(21) - 10;
                    int l = random.nextInt(21) - 10;
                    BlockPos blockPos3 = level.getHeightmapPos(Types.MOTION_BLOCKING, blockPos.offset(k, 0, l));
                    if (blockPos3.getY() > level.getMinBuildHeight() && blockPos3.getY() <= blockPos.getY() + 10 && blockPos3.getY() >= blockPos.getY() - 10) {
                        blockPos2 = blockPos3.below();
                    }
                }

                if (blockPos2 != null && random.nextInt(3) < this.rainSoundTime++) {
                    this.rainSoundTime = 0;
                    if (blockPos2.getY() > blockPos.getY() + 1 && level.getHeightmapPos(Types.MOTION_BLOCKING, blockPos).getY() > Mth.floor((float)blockPos.getY())) {
                        SoundEvent sound = WeatherParticleSpawner.getBiomeSound(blockPos2, true);
                        if (sound != null) {
                            this.minecraft.level.playLocalSound(blockPos2, sound, SoundSource.WEATHER, 0.1F, 0.5F, false);
                        }
                    } else {
                        SoundEvent sound = WeatherParticleSpawner.getBiomeSound(blockPos2, false);
                        if (sound != null) {
                            this.minecraft.level.playLocalSound(blockPos2, sound, SoundSource.WEATHER, 0.2F, 1.0F, false);
                        }
                    }
                }
            }

            ci.cancel();
        }
    }

    @Inject(
            method = "renderSnowAndRain",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderWeather(LightTexture lightTexture, float partialTicks, double x, double y, double z, CallbackInfo ci) {
        if (!OttConfig.WEATHER.RENDER_VANILLA_WEATHER.get()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "blockChanged",
            at = @At("HEAD")
    )
    private void ott$onBlockChanged(BlockGetter level, BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        CreepOverlayRenderer.updateHedgeCache(pos, newState);
    }

    @Inject(
            method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    shift = At.Shift.AFTER
            )
    )
    private void ott$renderHealthBar(Entity entity, double camX, double camY, double camZ, float partialTick, PoseStack poseStack, MultiBufferSource buffers, CallbackInfo ci) {
        double d0 = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double d1 = Mth.lerp(partialTick, entity.yOld, entity.getY());
        double d2 = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        HealthBarRenderer.hookRender(entity, poseStack, buffers, entityRenderDispatcher.camera, entityRenderDispatcher.getRenderer(entity), partialTick, d0 - camX, d1 - camY, d2 - camZ);
    }
}
