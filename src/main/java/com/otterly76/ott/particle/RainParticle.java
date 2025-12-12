package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RainParticle extends WeatherParticle {
    protected RainParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.quadSize = 0.25F;
        this.gravity = 0.5F;
        this.yd = -this.gravity;
        if (level.isThundering()) {
            this.xd = this.gravity * 0.3F;
        } else {
            this.xd = this.gravity * 0.1F;
        }

        this.zd = this.xd;
        this.lifetime = 32 * 5;
        assert Minecraft.getInstance().cameraEntity != null;
        Vec3 vec3 = Minecraft.getInstance().cameraEntity.position();
        this.roll = (float)(Math.atan2(x - vec3.x, z - vec3.z) + (double)((float)java.lang.Math.PI / 2F));
    }

    public void fadeIn() {
        if (this.age < 20) {
            this.alpha = Math.clamp(0.0F, 1.0F, (float) this.age / 20.0F);
        }
    }

    public void tick() {
        super.tick();
        if (!this.level.getFluidState(this.pos.below(2)).isEmpty()) {
            this.alpha = 0.0F;
        }

        if (!this.onGround && this.level.getFluidState(this.pos).isEmpty()) {
            if (this.removeIfObstructed()) {
                Vec3 raycastStart = new Vec3(this.x, this.y, this.z);
                Vec3 raycastEnd = new Vec3(this.x + 0.1D, this.y, this.z + 0.1D);
                BlockHitResult hit = this.level.clip(new ClipContext(raycastStart, raycastEnd, Block.COLLIDER, Fluid.NONE, CollisionContext.empty()));
                if (hit.getType().equals(Type.BLOCK)) {
                    assert Minecraft.getInstance().cameraEntity != null;
                    if (Minecraft.getInstance().cameraEntity.position().distanceTo(this.pos.getCenter()) < (double)32.0F - (double)32.0F / (double)2.0F) {
                        BlockState state = this.level.getBlockState(hit.getBlockPos());
                        if (state.is(BlockTags.IMPERMEABLE) || state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                            Minecraft.getInstance().particleEngine.createParticle(ModParticle.STREAK.get(), this.x, this.y, this.z, hit.getDirection().get2DDataValue(), 0.0F, 0.0F);
                            Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.RAIN, this.x, this.y, this.z, 0.0F, 0.0F, 0.0F);
                        }
                    }

                    this.remove();
                }
            }
        } else {
            assert Minecraft.getInstance().cameraEntity != null;
            if (Minecraft.getInstance().cameraEntity.position().distanceTo(this.pos.getCenter()) < (double)32.0F - (double)32.0F / (double)2.0F) {
                for (int i = 0; i < 3; ++i) {
                    Vec3 spawnPos = Vec3.atLowerCornerWithOffset(this.pos, this.random.nextFloat() * 3.0F - 1.0F, 0.0F, this.random.nextFloat() * 3.0F - 1.0F);
                    double d = this.random.nextDouble();
                    double e = this.random.nextDouble();
                        BlockPos blockPos = BlockPos.containing(spawnPos);
                        BlockState blockState = this.level.getBlockState(blockPos);
                        FluidState fluidState = this.level.getFluidState(blockPos);
                        VoxelShape voxelShape = blockState.getCollisionShape(this.level, blockPos);
                        double voxelHeight = voxelShape.max(Axis.Y, d, e);
                        double fluidHeight = fluidState.getHeight(this.level, blockPos);
                        double height = java.lang.Math.max(voxelHeight, fluidHeight);
                        Vec3 raycastStart = new Vec3(this.x, this.y, this.z);
                    Vec3 raycastEnd = new Vec3(spawnPos.x, this.y, spawnPos.z);
                    BlockHitResult hit = this.level.clip(new ClipContext(raycastStart, raycastEnd, Block.COLLIDER, Fluid.NONE, CollisionContext.empty()));
                    Vec2 raycastHit = new Vec2((float) hit.getLocation().x, (float) hit.getLocation().z);
                    if (height != (double) 0.0F && (double) raycastHit.distanceToSqr(new Vec2((float) spawnPos.x, (float) spawnPos.z)) < 0.01) {
                        if (fluidState.isSourceOfType(Fluids.WATER)) {
                            if (height != (double) 1.0F) {
                                Minecraft.getInstance().particleEngine.createParticle(ModParticle.RIPPLE.get(), spawnPos.x, spawnPos.y + height, spawnPos.z, 0.0F, 0.0F, 0.0F);
                                if (this.level.isThundering()) {
                                    double var26 = spawnPos.y + height;
                                    Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.RAIN, spawnPos.x, var26, spawnPos.z, 0.0F, 0.0F, 0.0F);
                                }
                            }
                        } else if (blockState.is(BlockTags.INFINIBURN_OVERWORLD) || blockState.is(BlockTags.STRIDER_WARM_BLOCKS)) {
                            double var24 = spawnPos.y + height;
                            Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.SMOKE, spawnPos.x, var24, spawnPos.z, 0.0F, 0.0F, 0.0F);
                            if (this.level.isThundering()) {
                                var24 = spawnPos.y + height;
                                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.LARGE_SMOKE, spawnPos.x, var24, spawnPos.z, 0.0F, 0.0F, 0.0F);
                            }
                        } else {
                            double var10003 = spawnPos.y + height;
                            Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.RAIN, spawnPos.x, var10003, spawnPos.z, 0.0F, 0.0F, 0.0F);
                        }
                    }
                }
            }

            this.remove();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float f) {
        Vector3f localPos = this.getInterpolatedRelPos(camera, f);
        float x = localPos.x();
        float y = localPos.y();
        float z = localPos.z();

        Quaternionf quaternion = new Quaternionf(new AxisAngle4d(this.roll, 0.0F, 1.0F, 0.0F));
        this.flipItTurnwaysIfBackfaced(quaternion, new Vector3f(x, y, z));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y + 0.25F, z, f);
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public DefaultFactory(SpriteSet provider) {
            this.spriteSet = provider;
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            RainParticle particle = new RainParticle(level, x, y, z);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}