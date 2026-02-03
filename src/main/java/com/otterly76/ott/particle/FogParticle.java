package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;

public class FogParticle extends WeatherParticle {
    private static final float FADE_DURATION = 40.0F;
    private final float targetAlpha = 1.0F;
    private final float baseQuadSize;

    private FogParticle(ClientLevel level, double x, double y, double z, SpriteSet provider) {
        super(level, x, y, z);
        this.setSprite(provider.get(level.getRandom()));
        this.lifetime = OttConfig.WEATHER.PARTICLE_RADIUS.get() * 5;
        assert Minecraft.getInstance().cameraEntity != null;
        double distance = Minecraft.getInstance().cameraEntity.position().distanceTo(new Vec3(x, y, z));
        this.quadSize = (float)(OttConfig.WEATHER.FOG.SIZE.get() / distance);
        this.baseQuadSize = (float)(OttConfig.WEATHER.FOG.SIZE.get() / distance);
        Color color = (new Color(this.level.getBiome(this.pos).value().getFogColor())).darker();
        this.rCol = (float)color.getRed() / 255.0F;
        this.gCol = (float)color.getGreen() / 255.0F;
        this.bCol = (float)color.getBlue() / 255.0F;
        this.roll = level.random.nextFloat() * (float)Math.PI;
        this.oRoll = this.roll;
        this.xd = this.gravity / 3.0F;
        this.zd = this.gravity / 3.0F;
        this.gravity = OttConfig.WEATHER.FOG.GRAVITY.get().floatValue();
    }

    public void tick() {
        super.tick();
        assert Minecraft.getInstance().cameraEntity != null;
        double camdist = Minecraft.getInstance().cameraEntity.position().distanceTo(new Vec3(this.x, this.y, this.z));
        this.quadSize = (float)camdist / 2.0F;
        BlockState fallingTowards = this.level.getBlockState(this.pos.offset(3, -1, 3));
        BlockPos blockPos = this.pos.offset(2, -4, 2);
        if ((this.level.getHeight(Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ()) >= blockPos.getY() || !fallingTowards.getFluidState().isEmpty()) && !this.shouldFadeOut) {
            this.shouldFadeOut = true;
        }

        if (this.onGround) {
            this.remove();
        }
    }

    public void fadeIn() {
        if ((float)this.age < 40.0F) {
            float progress = (float)this.age / 40.0F;
            this.alpha = Mth.lerp(progress, 0.0F, this.targetAlpha);
            this.quadSize = Mth.lerp(progress, 0.0F, this.baseQuadSize);
        }
    }

    public void fadeOut() {
        float progress = Math.min(((float)this.age - ((float)this.lifetime - 40.0F)) / 40.0F, 1.0F);
        if (progress > 0.0F) {
            this.alpha = Mth.lerp(progress, this.targetAlpha, 0.0F);
            this.quadSize = Mth.lerp(progress, this.baseQuadSize, 0.0F);
            if (progress >= 1.0F) {
                this.remove();
            }
        }
    }

    public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float f) {
        Vec3 camPos = camera.getPosition();
        float x = (float)(Mth.lerp(f, this.xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(f, this.yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(f, this.zo, this.z) - camPos.z());
        Vector3f localPos = new Vector3f(x, y, z);
        Quaternionf quaternion = Axis.YP.rotation((float)Math.atan2(x, z) + (float)Math.PI);
        float yAngle = (float)Math.asin(y / localPos.length());
        quaternion.rotateX(yAngle);
        quaternion.rotateZ((float)Math.atan2(x, z));
        if (yAngle < -1.0F) {
            this.shouldFadeOut = true;
        }

        quaternion.rotateZ(Mth.lerp(f, this.oRoll, this.roll));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y, z, f);
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new FogParticle(level, x, y, z, this.provider);
        }
    }
}