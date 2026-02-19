package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DustParticle extends DustMoteParticle {
    protected DustParticle(ClientLevel clientWorld, double x, double y, double z, SpriteSet provider) {
        super(clientWorld, x, y, z, provider);
        this.quadSize = OttConfig.WEATHER.SAND.SIZE.get().floatValue();
        this.gravity = OttConfig.WEATHER.SAND.GRAVITY.get().floatValue() - 0.1F;
        if (OttConfig.WEATHER.SAND.SPAWN_ON_GROUND.get()) {
            this.yd = 0.1;
        }
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            this.yd = 0.01F;
        }

    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float tickPercentage) {
        Vector3f localPos = this.getRelativePosition(camera, tickPercentage);
        Quaternionf quaternion = camera.rotation();
        localPos.y = localPos.y + Mth.sin(Mth.lerp(tickPercentage, (float)this.age - 1.0F, (float)this.age) / 20.0F) + 1.5F;
        this.renderRotatedQuad(vertexConsumer, quaternion, localPos.x, localPos.y, localPos.z, tickPercentage);
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
