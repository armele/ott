package com.otterly76.ott.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FireflyParticle extends TextureSheetParticle {
    private static final float PARTICLE_FADE_OUT_LIGHT_TIME = 0.1F;
    private static final float PARTICLE_FADE_IN_LIGHT_TIME = 0.3F;
    private static final float PARTICLE_FADE_OUT_ALPHA_TIME = 0.5F;
    private static final float PARTICLE_FADE_IN_ALPHA_TIME = 0.3F;
    private static final int PARTICLE_MIN_LIFETIME = 200;
    private static final int PARTICLE_MAX_LIFETIME = 300;

    protected FireflyParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.speedUpWhenYMotionIsBlocked = true;
        this.friction = 0.96F;
        this.quadSize *= 0.75F;
        this.yd *= 0.8F;
        this.xd *= 0.8F;
        this.zd *= 0.8F;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return (int)(255.0F * this.getFadeAmount(this.getLifetimeProgress((float)this.age + partialTick), 0.1F, 0.3F));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
            this.remove();
        } else {
            this.setAlpha(this.getFadeAmount(this.getLifetimeProgress((float)this.age), 0.3F, 0.5F));
            if (Math.random() > 0.95 || this.age == 1) {
                this.setParticleSpeed(-0.05 + 0.1F * Math.random(), -0.05 + 0.1F * Math.random(), -0.05 + 0.1F * Math.random());
            }
        }
    }

    private float getLifetimeProgress(float age) {
        return Mth.clamp(age / (float)this.lifetime, 0.0F, 1.0F);
    }

    private float getFadeAmount(float lifetime, float fadeIn, float fadeOut) {
        if (lifetime >= 1.0F - fadeIn) {
            return (1.0F - lifetime) / fadeIn;
        } else {
            return lifetime <= fadeOut ? lifetime / fadeOut : 1.0F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FireflyParticle particle = new FireflyParticle(level, x, y, z, 0.5F - level.random.nextDouble(), level.random.nextBoolean() ? ySpeed : -ySpeed, 0.5F - level.random.nextDouble());
            particle.setLifetime(level.random.nextIntBetweenInclusive(200, 300));
            particle.scale(1.5F);
            particle.pickSprite(this.sprite);
            particle.setAlpha(0.0F);
            return particle;
        }
    }
}
