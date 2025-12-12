package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
            return new DustParticle(level, x, y, z, this.provider);
        }
    }
}