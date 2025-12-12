package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class StreakParticle extends WeatherParticle {
    Direction direction;

    private StreakParticle(ClientLevel level, double x, double y, double z, int direction2D, SpriteSet provider) {
        super(level, x, y, z);
        this.setColor(0.2F, 0.3F, 1.0F);

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
        if (!this.shouldFadeOut && (this.onGround || !state.is(BlockTags.IMPERMEABLE) && !state.is(BlockTags.MINEABLE_WITH_PICKAXE) || !fluidState.isEmpty())) {
            if (state.isAir()) {
                double var10003 = this.y - (double)0.25F;
                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.DRIPPING_WATER, this.x, var10003, this.z, 0.0F, 0.0F, 0.0F);
            }

            this.gravity = 0.0F;
            this.yd = 0.0F;
            this.shouldFadeOut = true;
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float f) {
        Vector3f localPos = this.getInterpolatedRelPos(camera, f);
        float x = localPos.x();
        float y = localPos.y();
        float z = localPos.z();

        Quaternionf quaternion = Axis.YP.rotation((float)Math.atan2(x, z) + (float)Math.PI);
        float yAngle = (float)Math.asin(y / localPos.length());
        quaternion.rotateX(yAngle);
        quaternion.rotateZ((float)Math.atan2(x, z));
        if (yAngle < -1.0F) {
            this.shouldFadeOut = true;
        }

        quaternion.rotateZ(Mth.lerp(f, this.oRoll, this.roll));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y, z, f);
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