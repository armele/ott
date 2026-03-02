package com.otterly76.ott.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FallingLeavesParticle extends TextureSheetParticle {
    private static final float ACCELERATION_SCALE = 0.0025F;
    private static final int INITIAL_LIFETIME = 300;
    private float rotSpeed;
    private final float spinAcceleration;
    private final float windBig;
    private final boolean swirl;
    private final boolean flowAway;
    private final double xaFlowScale;
    private final double zaFlowScale;
    private final double swirlPeriod;

    protected FallingLeavesParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float fallAcceleration, float windBig, boolean swirl, boolean flowAway, float scale, float startVelocity) {
        super(level, x, y, z);
        this.rotSpeed = (float) Math.toRadians(this.random.nextBoolean() ? -30.0 : 30.0);
        this.spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.setSprite(sprites.get(this.random.nextInt(12), 12));
        this.windBig = windBig;
        this.swirl = swirl;
        this.flowAway = flowAway;
        this.lifetime = INITIAL_LIFETIME;
        this.gravity = fallAcceleration * 1.2F * ACCELERATION_SCALE;
        float size = scale * (this.random.nextBoolean() ? 0.05F : 0.075F);
        this.quadSize = size;
        this.setSize(size, size);
        this.friction = 1.0F;
        this.yd = -startVelocity;
        float particleRandom = this.random.nextFloat();
        this.xaFlowScale = Math.cos(Math.toRadians(particleRandom * 60.0F)) * (double) this.windBig;
        this.zaFlowScale = Math.sin(Math.toRadians(particleRandom * 60.0F)) * (double) this.windBig;
        this.swirlPeriod = Math.toRadians(1000.0F + particleRandom * 3000.0F);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (!this.removed) {
            float aliveTicks = (float) (INITIAL_LIFETIME - this.lifetime);
            float relativeAge = Math.min(aliveTicks / (float) INITIAL_LIFETIME, 1.0F);
            double xa = 0.0;
            double za = 0.0;
            if (this.flowAway) {
                xa += this.xaFlowScale * Math.pow(relativeAge, 1.25);
                za += this.zaFlowScale * Math.pow(relativeAge, 1.25);
            }

            if (this.swirl) {
                xa += (double) relativeAge * Math.cos((double) relativeAge * this.swirlPeriod) * (double) this.windBig;
                za += (double) relativeAge * Math.sin((double) relativeAge * this.swirlPeriod) * (double) this.windBig;
            }

            this.xd += xa * (double) ACCELERATION_SCALE;
            this.zd += za * (double) ACCELERATION_SCALE;
            this.yd -= this.gravity;
            this.rotSpeed += this.spinAcceleration / 20.0F;
            this.oRoll = this.roll;
            this.roll += this.rotSpeed / 20.0F;
            this.move(this.xd, this.yd, this.zd);
            if (this.onGround || this.lifetime < 299 && (this.xd == 0.0 || this.zd == 0.0)) {
                this.remove();
            }

            if (!this.removed) {
                this.xd *= this.friction;
                this.yd *= this.friction;
                this.zd *= this.friction;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static FallingLeavesParticle create(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        return new FallingLeavesParticle(level, x, y, z, sprites, 0.07F, 10.0F, true, false, 2.0F, 0.021F);
    }

    @OnlyIn(Dist.CLIENT)
    public record PaleOakProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return FallingLeavesParticle.create(level, x, y, z, this.sprites);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public record TintedLeavesProvider(SpriteSet sprites) implements ParticleProvider<ColorParticleOption> {
        @Override
        public Particle createParticle(@NotNull ColorParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingLeavesParticle particle = FallingLeavesParticle.create(level, x, y, z, this.sprites);
            particle.setColor(type.getRed(), type.getGreen(), type.getBlue());
            return particle;
        }
    }
}
