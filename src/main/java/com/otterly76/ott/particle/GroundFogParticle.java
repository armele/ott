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

    protected GroundFogParticle(ClientLevel level, double x, double y, double z, SpriteSet provider) {
        super(level, x, y, z);
        this.setSpriteFromAge(provider);
        this.quadSize = 3.0F;
        this.lifetime = 500 + level.random.nextInt(200);

        int fogColor = level.getBiome(BlockPos.containing(x, y, z)).value().getFogColor();
        this.rCol = (float)((fogColor >> 16) & 255) / 255.0F;
        this.gCol = (float)((fogColor >> 8) & 255) / 255.0F;
        this.bCol = (float)(fogColor & 255) / 255.0F;

        this.alpha = 0.0F;
        this.roll = (float)level.random.nextInt(4) * ((float)Math.PI / 2F);
        this.oRoll = this.roll;
        this.xdxd = (this.random.nextFloat() - 0.5F) * 0.02F;
        this.zdzd = (this.random.nextFloat() - 0.5F) * 0.02F;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.getRainLevel(1.0f) <= 0.0f) {
            this.age += 10;
        }

        float fadeDuration = 150.0F;
        float maxAlpha = 0.4F;

        if (this.age < fadeDuration) {
            float fadeInScale = (float)this.age / fadeDuration;
            this.alpha = (float)Math.sin(fadeInScale * (Math.PI / 2)) * maxAlpha;
        } else if (this.age > (this.lifetime - fadeDuration)) {
            float fadeOutScale = (float)(this.lifetime - this.age) / fadeDuration;
            this.alpha = (float)Math.sin(fadeOutScale * (Math.PI / 2)) * maxAlpha;
        } else {
            this.alpha = maxAlpha;
        }

        this.xd = this.xdxd;
        this.zd = this.zdzd;
        this.yd *= 0.85;

        if (this.onGround || this.age >= this.lifetime) {
            this.remove();
        }
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