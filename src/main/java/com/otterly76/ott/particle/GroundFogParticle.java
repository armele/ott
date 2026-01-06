package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.ClientModEvents;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.particle.render.GroundFogRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;

import java.awt.*;

public class GroundFogParticle extends WeatherParticle {
    float xdxd;
    float zdzd;

    private GroundFogParticle(ClientLevel level, double x, double y, double z, SpriteSet provider) {
        super(level, x, y, z);
        ++ClientModEvents.fogCount;
        this.setSprite(provider.get(level.getRandom()));
        this.quadSize = OttConfig.WEATHER.GROUND_FOG.SIZE.get().floatValue();
        this.lifetime = 30000;
        Color color = new Color(this.level.getBiome(this.pos).value().getFogColor());
        this.rCol = (float)color.getRed() / 255.0F;
        this.gCol = (float)color.getGreen() / 255.0F;
        this.bCol = (float)color.getBlue() / 255.0F;
        this.roll = ((float)Math.PI / 2F) * (float)level.random.nextInt(4);
        this.oRoll = this.roll;
        this.xdxd = (this.random.nextFloat() - 0.5F) / 100.0F;
        this.zdzd = (this.random.nextFloat() - 0.5F) / 100.0F;
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            this.remove();
        }

        this.xd = this.xdxd;
        this.zd = this.zdzd;
    }

    public void remove() {
        if (this.isAlive()) {
            --ClientModEvents.fogCount;
        }

        super.remove();
    }

    public @NotNull AABB getRenderBoundingBox(float partialTicks) {
        return this.getBoundingBox().inflate(4.0F);
    }

    public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float f) {
        Vec3 camPos = camera.getPosition();
        float x = (float)(Mth.lerp(f, this.xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(f, this.yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(f, this.zo, this.z) - camPos.z());
        Quaternionf quaternion = new Quaternionf(new AxisAngle4d((float)Math.PI / 2F, -1.0F, 0.0F, 0.0F));
        quaternion.rotateZ(Mth.lerp(f, this.oRoll, this.roll));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y, z, f);
    }

    public @NotNull ParticleRenderType getRenderType() {
        return GroundFogRenderType.INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new GroundFogParticle(level, x, y, z, this.provider);
        }
    }
}