package com.otterly76.ott.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class HedgeLeafParticle extends TextureSheetParticle {
    private final float rollSpeed;

    protected HedgeLeafParticle(ClientLevel level, double x, double y, double z,
                                double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);

        // Use this.random instead of level.random for better isolation
        this.lifetime = Mth.randomBetweenInclusive(this.random, 40, 80);
        this.quadSize *= 0.35F + this.random.nextFloat() * 0.25F;

        this.hasPhysics = false;
        this.friction = 0.98F;

        this.gravity = 0.02F;
        this.yd = -Math.abs(this.yd);

        this.roll = this.random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;
        this.rollSpeed = (this.random.nextFloat() - 0.5F) * 0.08F;

        this.alpha = 0.9F;
    }

    @Override
    public void tick() {
        double flutter = 0.0010;
        this.xd += (this.random.nextDouble() - 0.5) * flutter;
        this.zd += (this.random.nextDouble() - 0.5) * flutter;

        double maxXZ = 0.008;
        this.xd = Mth.clamp(this.xd, -maxXZ, maxXZ);
        this.zd = Mth.clamp(this.zd, -maxXZ, maxXZ);

        double maxDown = 0.06;
        this.yd = Mth.clamp(this.yd, -maxDown, 0.02);

        this.oRoll = this.roll;
        this.roll += this.rollSpeed;

        super.tick();

        if (this.age > this.lifetime - 12) {
            this.alpha = Math.max(0.0F, this.alpha - 0.08F);
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            HedgeLeafParticle p = new HedgeLeafParticle(level, x, y, z, xd, yd, zd);
            p.pickSprite(this.sprites);
            return p;
        }
    }
}



