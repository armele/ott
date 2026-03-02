package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.ClientModEvents;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class StreakParticle extends WeatherParticle {
    Direction direction;

    private StreakParticle(ClientLevel level, double x, double y, double z, int direction2D, SpriteSet provider) {
        super(level, x, y, z);
        if (OttConfig.WEATHER.BIOME_TINT.get()) {
            ClientModEvents.applyWaterTint(this, level, this.pos);
        } else {
            this.setColor(0.2F, 0.3F, 1.0F);
        }

        this.setSprite(provider.get(level.getRandom()));
        this.quadSize = 0.5F;
        this.gravity = this.random.nextFloat() / 10.0F;
        this.roll = (float)direction2D * ((float)Math.PI / 2F);
        this.direction = Direction.from2DDataValue(direction2D);
    }

    public void tick() {
        super.tick();
        if (this.age % 10 == 0) {
            if (this.random.nextBoolean()) {
                this.gravity = this.random.nextFloat() / 10.0F;
            } else {
                this.gravity = 0.0F;
            }
        }

        BlockState state = this.level.getBlockState(this.pos.relative(this.direction.getOpposite()));
        FluidState fluidState = this.level.getFluidState(this.pos);
        if (!this.shouldFadeOut && (this.onGround || !state.is(BlockTags.IMPERMEABLE) && !state.is(BlockTags.MINEABLE_WITH_PICKAXE) && !state.is(BlockTags.MINEABLE_WITH_AXE) && !state.is(BlockTags.LEAVES) && !state.is(BlockTags.LOGS) || !fluidState.isEmpty())) {
            if (state.isAir()) {
                double var10003 = this.y - (double)0.25F;
                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.DRIPPING_WATER, this.x, var10003, this.z, 0.0F, 0.0F, 0.0F);
            }

            this.gravity = 0.0F;
            this.yd = 0.0F;
            this.shouldFadeOut = true;
        }

    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float f) {
        Vector3f localPos = this.getRelativePosition(camera, f);
        Quaternionf quaternion = new Quaternionf(new AxisAngle4d(this.roll, 0.0F, 1.0F, 0.0F));
        this.flipItTurnwaysIfBackfaced(quaternion, localPos);
        this.renderRotatedQuad(vertexConsumer, quaternion, localPos.x, localPos.y + 0.25F, localPos.z, f);
    }

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
            return new StreakParticle(level, x, y, z, (int)velocityX, this.provider);
        }
    }
}