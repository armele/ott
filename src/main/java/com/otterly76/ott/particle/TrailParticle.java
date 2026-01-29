package com.otterly76.ott.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.util.FastColor.ABGR32;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrailParticle extends TextureSheetParticle {
    private final Vec3 target;

    public TrailParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, Vec3 vec3, int j) {
        super(clientLevel, d, e, f, g, h, i);
        j = scaleRGB(j, 0.875F + this.random.nextFloat() * 0.25F, 0.875F + this.random.nextFloat() * 0.25F, 0.875F + this.random.nextFloat() * 0.25F);
        this.rCol = (float)ABGR32.red(j) / 255.0F;
        this.gCol = (float)ABGR32.green(j) / 255.0F;
        this.bCol = (float)ABGR32.blue(j) / 255.0F;
        this.quadSize = 0.26F;
        this.target = vec3;
    }

    public static int scaleRGB(int i, float f, float g, float h) {
        return ABGR32.color(ABGR32.alpha(i), Math.clamp((int)((float)ABGR32.red(i) * f), 0, 255), Math.clamp((int)((float)ABGR32.green(i) * g), 0, 255), Math.clamp((int)((float)ABGR32.blue(i) * h), 0, 255));
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            int i = this.lifetime - this.age;
            double d = (double)1.0F / (double)i;
            this.x = Mth.lerp(d, this.x, this.target.x());
            this.y = Mth.lerp(d, this.y, this.target.y());
            this.z = Mth.lerp(d, this.z, this.target.z());
        }

    }

    protected int getLightColor(float p_107249_) {
        return 15728880;
    }

    public static class Provider implements ParticleProvider<TrailParticleOption> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public @Nullable Particle createParticle(TrailParticleOption trailParticleOption, @NotNull ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            TrailParticle trailParticle = new TrailParticle(clientLevel, d, e, f, g, h, i, trailParticleOption.target(), trailParticleOption.color());
            trailParticle.pickSprite(this.sprite);
            trailParticle.setLifetime(trailParticleOption.duration());
            return trailParticle;
        }
    }
}



