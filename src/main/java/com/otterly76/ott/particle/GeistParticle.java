package com.otterly76.ott.particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class GeistParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected GeistParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, float r, float g, float b, float a) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.lifetime = 20 + this.random.nextInt(20);
        this.quadSize = 0.2f + this.random.nextFloat() * 0.1f;
        this.hasPhysics = false;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.alpha = a;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.alpha *= 0.95f;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class GhostProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public GhostProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new GeistParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites, 1.0f, 1.0f, 1.0f, 0.8f);
        }
    }

    public static class GeistDarkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public GeistDarkProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            GeistParticle p = new GeistParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites, 0.15f, 0.15f, 0.15f, 0.5f);
            p.quadSize = 0.4f;
            return p;
        }
    }
}