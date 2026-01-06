package com.otterly76.ott.particle;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.MapColor.Brightness;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DustMoteParticle extends WeatherParticle {
    protected DustMoteParticle(ClientLevel level, double x, double y, double z, SpriteSet provider) {
        super(level, x, y, z);
        this.setSprite(provider.get(level.getRandom()));
        this.quadSize = OttConfig.WEATHER.SAND.MOTE_SIZE.get().floatValue();
        this.xd = OttConfig.WEATHER.SAND.WIND_STRENGTH.get();
        this.zd = OttConfig.WEATHER.SAND.WIND_STRENGTH.get();
        this.gravity = OttConfig.WEATHER.SAND.GRAVITY.get().floatValue();
        Color color = new Color(level.getBlockState(level.getHeightmapPos(Types.MOTION_BLOCKING, BlockPos.containing(x, y, z)).below()).getBlock().defaultMapColor().calculateRGBColor(Brightness.NORMAL));
        this.bCol = (float)color.getRed() / 255.0F;
        this.rCol = (float)color.getBlue() / 255.0F;
        this.gCol = (float)color.getGreen() / 255.0F;
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            this.yd = 0.1F;
        }

        this.removeIfObstructed();
        if (!this.level.getFluidState(this.pos).isEmpty()) {
            this.shouldFadeOut = true;
            this.gravity = 0.0F;
        } else {
            this.xd = 0.2;
            this.zd = 0.2;
        }

    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new DustMoteParticle(level, x, y, z, this.provider);
        }
    }
}