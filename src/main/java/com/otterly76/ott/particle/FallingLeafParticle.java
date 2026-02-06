package com.otterly76.ott.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FallingLeafParticle extends TextureSheetParticle {
    FallingLeafParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.6F;
        this.lifetime = (int) ((double) 16.0F / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = true;
        this.friction = 1.0F;
        this.gravity = 1.0F;
        this.yd = -Math.abs(this.yd);
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
            this.xd *= 0.99;
            this.zd *= 0.99;
        } else {
            this.remove();
        }

    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        public @NotNull Particle createParticle(@NotNull SimpleParticleType var1, @NotNull ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingLeafParticle leaf = new FallingLeafParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            leaf.lifetime = Mth.randomBetweenInclusive(world.random, 500, 1000);
            leaf.setColor(1.0F, 1.0F, 1.0F);
            leaf.setSprite(this.sprite.get(world.random.nextInt(16), 16));
            return leaf;
        }
    }
}
