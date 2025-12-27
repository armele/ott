package com.otterly76.ott.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class GroundFogParticle extends TextureSheetParticle {
    private final float xdxd;
    private final float zdzd;
    private final float rotSpeed; // Add rotation speed

    protected GroundFogParticle(ClientLevel level, double x, double y, double z, SpriteSet provider) {
        super(level, x, y, z);
        this.setSpriteFromAge(provider);
        this.quadSize = 3.0F;
        // Increase lifetime: from ~600 to ~1200 ticks (approx 1 min)
        this.lifetime = 1000 + level.random.nextInt(400);

        int fogColor = level.getBiome(BlockPos.containing(x, y, z)).value().getFogColor();
        this.rCol = (float)((fogColor >> 16) & 255) / 255.0F;
        this.gCol = (float)((fogColor >> 8) & 255) / 255.0F;
        this.bCol = (float)(fogColor & 255) / 255.0F;

        this.alpha = 0.0F;
        this.roll = level.random.nextFloat() * ((float)Math.PI * 2F);
        this.oRoll = this.roll;
        // Slower swirl: 0.05F -> 0.02F
        this.rotSpeed = (level.random.nextFloat() - 0.5F) * 0.02F;
        // Slower horizontal drift: 0.01F -> 0.005F
        this.xdxd = (this.random.nextFloat() - 0.5F) * 0.005F;
        this.zdzd = (this.random.nextFloat() - 0.5F) * 0.005F;
        // Slower initial upward lift: 0.005F -> 0.002F
        this.yd = 0.002F + (this.random.nextFloat() * 0.002F);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;

        if (this.age >= this.lifetime) {
            this.remove();
            return;
        }

        // Keep the "burn off" logic but make it gentler.
        // If it's not raining, age it slightly faster, but not 10x faster.
        if (this.level.getRainLevel(1.0f) <= 0.0f) {
            this.age += 1;
        }

        this.roll += this.rotSpeed;

        // Shorter fade-in so we see it sooner, but long enough to be smooth
        float fadeInTicks = 60.0F;
        float fadeOutTicks = 200.0F;
        float maxAlpha = 0.4F;

        if (this.age < fadeInTicks) {
            this.alpha = (float)this.age / fadeInTicks * maxAlpha;
        } else if (this.age > (this.lifetime - fadeOutTicks)) {
            float fadeOutLeft = (float)(this.lifetime - this.age);
            this.alpha = (fadeOutLeft / fadeOutTicks) * maxAlpha;
        } else {
            this.alpha = maxAlpha;
        }

        this.xd = this.xdxd;
        this.zd = this.zdzd;

        // This subtle lift is key to the "rising" look without hitting the clouds
        this.yd += 0.00002F;

        this.move(this.xd, this.yd, this.zd);

        this.age++;
    }

    @Override
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
            return new GroundFogParticle(level, x, y, z, this.provider);
        }
    }
}