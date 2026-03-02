package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ShrubParticle extends WeatherParticle {
    protected ShrubParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.quadSize = 0.5F;
        this.gravity = OttConfig.WEATHER.SHRUB.GRAVITY.get().floatValue();
        this.xd = OttConfig.WEATHER.SAND.WIND_STRENGTH.get();
        this.zd = OttConfig.WEATHER.SAND.WIND_STRENGTH.get();
        if (OttConfig.WEATHER.SAND.SPAWN_ON_GROUND.get()) {
            this.yd = 0.1;
        }

        this.setSprite(sprites.get(this.random));
    }

    public void tick() {
        super.tick();
        this.removeIfObstructed();
        if (!this.level.getFluidState(this.pos).isEmpty()) {
            this.shouldFadeOut = true;
            this.gravity = 0.0F;
        } else {
            this.xd = 0.2;
            this.zd = 0.2;
        }

        this.oRoll = this.roll;
        this.roll += OttConfig.WEATHER.SHRUB.ROTATION_AMOUNT.get().floatValue();
        if (this.onGround) {
            this.yd = OttConfig.WEATHER.SHRUB.BOUNCINESS.get();
        }
    }

    public void fadeIn() {
        if (this.age < 10) {
            this.alpha = (float)this.age / 10.0F;
        }
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float tickPercentage) {
        Vector3f localPos = this.getRelativePosition(camera, tickPercentage);
        float angle = (float)Math.atan2(this.xd, this.zd);
        float roll = Mth.lerp(tickPercentage, this.oRoll, this.roll);

        Quaternionf tumble = new Quaternionf().rotateY(angle).rotateX(roll);

        Quaternionf quat1 = new Quaternionf(tumble).rotateY(0.0F);
        Quaternionf quat2 = new Quaternionf(tumble).rotateY((float)Math.PI / 3.0F);
        Quaternionf quat3 = new Quaternionf(tumble).rotateY(2.0F * (float)Math.PI / 3.0F);

        quat1 = this.flipItTurnwaysIfBackfaced(quat1, localPos);
        quat2 = this.flipItTurnwaysIfBackfaced(quat2, localPos);
        quat3 = this.flipItTurnwaysIfBackfaced(quat3, localPos);

        this.renderRotatedQuad(vertexConsumer, quat1, localPos.x, localPos.y, localPos.z, tickPercentage);
        this.renderRotatedQuad(vertexConsumer, quat2, localPos.x, localPos.y, localPos.z, tickPercentage);
        this.renderRotatedQuad(vertexConsumer, quat3, localPos.x, localPos.y, localPos.z, tickPercentage);
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ShrubParticle(level, x, y, z, this.provider);
        }
    }
}