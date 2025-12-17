package com.otterly76.ott.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class MidnightLeafParticle extends TextureSheetParticle {
    private final float rollSpeed;

    protected MidnightLeafParticle(ClientLevel level, double x, double y, double z,
                                   double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);

        this.lifetime = Mth.randomBetweenInclusive(level.random, 40, 80);
        this.quadSize *= 0.35F + level.random.nextFloat() * 0.25F;

        this.hasPhysics = false;
        this.friction = 0.98F;

        // Falling leaf feel
        this.gravity = 0.02F;
        this.yd = -Math.abs(this.yd);

        // Slow rotation
        this.roll = level.random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;
        this.rollSpeed = (level.random.nextFloat() - 0.5F) * 0.08F; // tweak: smaller = slower

        this.alpha = 0.9F;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void tick() {
        // gentle sideways flutter while falling
        double flutter = 0.0010; // slightly calmer
        this.xd += (this.random.nextDouble() - 0.5) * flutter;
        this.zd += (this.random.nextDouble() - 0.5) * flutter;

        // stricter caps = no darts
        double maxXZ = 0.008;
        this.xd = Mth.clamp(this.xd, -maxXZ, maxXZ);
        this.zd = Mth.clamp(this.zd, -maxXZ, maxXZ);

        double maxDown = 0.06;
        this.yd = Mth.clamp(this.yd, -maxDown, 0.02);

        // advance rotation (store old -> new)
        this.oRoll = this.roll;
        this.roll += this.rollSpeed;

        super.tick();

        // gentle fade-out near the end
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
            MidnightLeafParticle p = new MidnightLeafParticle(level, x, y, z, xd, yd, zd);
            p.pickSprite(this.sprites);
            return p;
        }
    }
}