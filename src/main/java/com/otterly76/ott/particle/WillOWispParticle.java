package com.otterly76.ott.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class WillOWispParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float colorOffset;

    @SuppressWarnings("this-escape")

    protected WillOWispParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.colorOffset = this.random.nextFloat();

        this.lifetime = 80 + this.random.nextInt(40);
        this.quadSize = 0.12F + this.random.nextFloat() * 0.08F;
        this.alpha = 0.0F;

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        updateArsColor();

        if (this.age < 20) {
            this.alpha = (float)this.age / 20.0F;
        } else if (this.age > this.lifetime - 20) {
            this.alpha = (float)(this.lifetime - this.age) / 20.0F;
        } else {
            this.alpha = 1.0F;
        }

        this.xd += (this.random.nextDouble() - 0.5D) * 0.005D;
        this.yd += (this.random.nextDouble() - 0.5D) * 0.005D;
        this.zd += (this.random.nextDouble() - 0.5D) * 0.005D;

        this.setSpriteFromAge(sprites);
    }

    private void updateArsColor() {
        float time = (float)this.age * 0.01F + colorOffset;
        float pulse = Mth.sin(time * (float)Math.PI * 2.0F) * 0.5F + 0.5F;

        float r = Mth.lerp(pulse, 0.3F, 0.6F);
        float g = Mth.lerp(pulse, 0.4F, 0.3F);
        float b = Mth.lerp(pulse, 0.9F, 0.8F);

        this.setColor(r * 0.9F, g * 0.9F, b * 0.9F);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WillOWispParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}








