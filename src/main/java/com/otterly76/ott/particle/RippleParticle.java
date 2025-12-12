package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RippleParticle extends WeatherParticle {
    private RippleParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSprite(Minecraft.getInstance().particleEngine.textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath("ott", "ripple0")));
        this.quadSize = 0.25F;
        this.alpha = 0.1F;
        this.x = (float)Math.round(this.x / (double)0.0625F) * 0.0625F;
        this.z = (float)Math.round(this.z / (double)0.0625F) * 0.0625F;
    }

    public void tick() {
        super.tick();
        this.alpha = Mth.lerp((float)this.age / 9.0F, 0.3F, 0.0F);
        if (this.age > 8) {
            this.remove();
        }

        this.setSprite(Minecraft.getInstance().particleEngine.textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath("ott", "ripple" + (this.age - 1))));
    }

    public void fadeIn() {
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
        public DefaultFactory(SpriteSet provider) {
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new RippleParticle(level, x, y, z);
        }
    }
}