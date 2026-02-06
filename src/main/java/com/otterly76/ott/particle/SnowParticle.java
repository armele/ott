package com.otterly76.ott.particle;

import com.otterly76.ott.ClientModEvents;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.otterly76.ott.Constants.MOD_ID;

public class SnowParticle extends WeatherParticle {
    float rotationAmount;

    protected SnowParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.quadSize = OttConfig.WEATHER.SNOW.SIZE.get().floatValue();
        this.gravity = OttConfig.WEATHER.SNOW.GRAVITY.get().floatValue();
        this.yd = -this.gravity;
        this.setSprite(Minecraft.getInstance().particleEngine.textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "snow" + this.random.nextInt(4))));

        if (level.isThundering()) {
            this.xd = this.gravity * OttConfig.WEATHER.SNOW.STORM_WIND_STRENGTH.get().floatValue();
        } else {
            this.xd = this.gravity * OttConfig.WEATHER.SNOW.WIND_STRENGTH.get().floatValue();
        }

        if (OttConfig.WEATHER.Y_LEVEL_WIND_ADJUSTMENT.get()) {
            this.xd *= ClientModEvents.yLevelWindAdjustment(y);
        }

        this.zd = this.xd;
        if (level.getRandom().nextBoolean()) {
            this.rotationAmount = 1.0F;
        } else {
            this.rotationAmount = -1.0F;
        }
    }

    public void tick() {
        super.tick();
        if (!this.level.getFluidState(this.pos.below()).isEmpty()) {
            this.alpha = 0.0F;
        } else if (!this.level.getFluidState(this.pos.below(2)).isEmpty()) {
            double distanceToWater = this.pos.below(2).getY() - this.pos.getY();
            this.alpha = (float)Math.abs(distanceToWater) / 2.0F;
        }

        this.oRoll = this.roll;
        this.roll = this.oRoll + (this.level.isThundering() ? OttConfig.WEATHER.SNOW.STORM_ROTATION_AMOUNT.get().floatValue() : OttConfig.WEATHER.SNOW.ROTATION_AMOUNT.get().floatValue()) * this.rotationAmount;

        if (this.onGround || this.removeIfObstructed()) {
            this.remove();
        }
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        public DefaultFactory(SpriteSet provider) {
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SnowParticle(level, x, y, z);
        }
    }
}
