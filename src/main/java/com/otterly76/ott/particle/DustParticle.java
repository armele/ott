package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class DustParticle extends DustMoteParticle {
    protected DustParticle(ClientLevel clientWorld, double x, double y, double z, SpriteSet provider) {
        super(clientWorld, x, y, z, provider);
        this.quadSize = 0.2F;
        this.gravity = 0.0F;
        this.yd = 0.1D;
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            this.yd = 0.01F;
        }

    }

    public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float tickPercentage) {
        Vec3 camPos = camera.getPosition();
        float x = (float)(Mth.lerp(tickPercentage, this.xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(tickPercentage, this.yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(tickPercentage, this.zo, this.z) - camPos.z());
        Quaternionf quaternion = camera.rotation();
        y = y + Mth.sin(Mth.lerp(tickPercentage, (float)this.age - 1.0F, (float)this.age) / 20.0F) + 1.5F;
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y, z, tickPercentage);
    }

    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new DustParticle(level, x, y, z, this.provider);
        }
    }
}