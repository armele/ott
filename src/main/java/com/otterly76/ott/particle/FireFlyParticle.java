package com.otterly76.ott.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class FireFlyParticle extends TextureSheetParticle {
    FireFlyParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = 0.0F;
        this.lifetime = this.random.nextInt(250, 500);
        this.setSize(0.01F, 0.01F);
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- > 0 && !this.onGround) {
            this.move(this.xd, this.yd, this.zd);
            this.xd += (this.random.nextDouble() - (double) 0.5F) * 0.02;
            this.yd += (this.random.nextDouble() - (double) 0.5F) * 0.02;
            this.zd += (this.random.nextDouble() - (double) 0.5F) * 0.02;
            this.xd *= 0.9;
            this.yd *= 0.9;
            this.zd *= 0.9;
        } else {
            this.remove();
        }

    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(@NotNull SimpleParticleType var1, @NotNull ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FireFlyParticle fireFlyParticle = new FireFlyParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            fireFlyParticle.lifetime = world.random.nextInt(250, 500);
            fireFlyParticle.setColor(1.0F, 1.0F, 1.0F);
            fireFlyParticle.setSprite(this.sprite.get(world.random.nextInt(16), 16));
            return fireFlyParticle;
        }
    }
}