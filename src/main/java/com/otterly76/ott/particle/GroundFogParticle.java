package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;

public class GroundFogParticle extends WeatherParticle {
    float xdxd;
    float zdzd;

    private GroundFogParticle(ClientLevel level, double x, double y, double z, SpriteSet provider) {
        super(level, x, y, z);
        this.setSprite(provider.get(level.getRandom()));
        this.quadSize = 10.0F;
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
        super.remove();
    }

    public @NotNull AABB getRenderBoundingBox(float partialTicks) {
        return this.getBoundingBox().inflate(4.0F);
    }

    @SuppressWarnings("DuplicatedCode")
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float f) {
        Vector3f localPos = this.getInterpolatedRelPos(camera, f);
        float x = localPos.x();
        float y = localPos.y();
        float z = localPos.z();

        Quaternionf quaternion = new Quaternionf(new AxisAngle4d(this.roll, 0.0F, 1.0F, 0.0F));
        this.flipItTurnwaysIfBackfaced(quaternion, new Vector3f(x, y, z));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y + 0.25F, z, f);
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