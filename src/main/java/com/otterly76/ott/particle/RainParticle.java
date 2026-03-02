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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
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
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.otterly76.ott.Constants.MOD_ID;

public class RainParticle extends WeatherParticle {
    protected RainParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        if (OttConfig.WEATHER.BIOME_TINT.get()) {
            ClientModEvents.applyWaterTint(this, level, this.pos);
        }

        this.quadSize = OttConfig.WEATHER.RAIN.SIZE.get().floatValue();
        this.gravity = OttConfig.WEATHER.RAIN.GRAVITY.get().floatValue();
        this.yd = -this.gravity;
        this.setSprite(Minecraft.getInstance().particleEngine.textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "rain" + this.random.nextInt(4))));

        if (level.isThundering()) {
            this.xd = this.gravity * OttConfig.WEATHER.RAIN.STORM_WIND_STRENGTH.get().floatValue();
        } else {
            this.xd = this.gravity * OttConfig.WEATHER.RAIN.WIND_STRENGTH.get().floatValue();
        }

        if (OttConfig.WEATHER.Y_LEVEL_WIND_ADJUSTMENT.get()) {
            this.xd *= ClientModEvents.yLevelWindAdjustment(y);
        }

        this.zd = this.xd;
        this.lifetime = OttConfig.WEATHER.PARTICLE_RADIUS.get() * 5;
        assert Minecraft.getInstance().cameraEntity != null;
        Vec3 vec3 = Minecraft.getInstance().cameraEntity.position();
        this.roll = (float)(Math.atan2(x - vec3.x, z - vec3.z) + (double)((float)java.lang.Math.PI / 2F));
    }

    public void fadeIn() {
        if (this.age < 20) {
            this.alpha = Math.clamp(0.0F, (float)OttConfig.WEATHER.RAIN.OPACITY.get() / 100.0F, (float) this.age / 20.0F);
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
                Vec3 raycastEnd = new Vec3(this.x + OttConfig.WEATHER.RAIN.WIND_STRENGTH.get(), this.y, this.z + OttConfig.WEATHER.RAIN.WIND_STRENGTH.get());
                BlockHitResult hit = this.level.clip(new ClipContext(raycastStart, raycastEnd, Block.COLLIDER, Fluid.NONE, CollisionContext.empty()));
                if (hit.getType().equals(Type.BLOCK)) {
                    if (OttConfig.WEATHER.DO_STREAK_PARTICLES.get()) {
                        assert Minecraft.getInstance().cameraEntity != null;
                        if (Minecraft.getInstance().cameraEntity.position().distanceTo(this.pos.getCenter()) < (double)OttConfig.WEATHER.PARTICLE_RADIUS.get() * 0.8) {
                            BlockState state = this.level.getBlockState(hit.getBlockPos());
                            if (state.is(BlockTags.IMPERMEABLE) || state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_AXE) || state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)) {
                                for (int i = 0; i < OttConfig.WEATHER.RAIN.STREAK_DENSITY.get(); ++i) {
                                    Minecraft.getInstance().particleEngine.createParticle(ModParticle.STREAK.get(), this.x, this.y, this.z, hit.getDirection().get2DDataValue(), 0.0, 0.0);
                                }
                                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.RAIN, this.x, this.y, this.z, 0.0, 0.0, 0.0);
                            }
                        }
                    }

                    this.remove();
                }
            }
        } else {
            if (OttConfig.WEATHER.DO_SPLASH_PARTICLES.get() || OttConfig.WEATHER.DO_SMOKE_PARTICLES.get() || OttConfig.WEATHER.DO_RIPPLE_PARTICLES.get()) {
                assert Minecraft.getInstance().cameraEntity != null;
                if (Minecraft.getInstance().cameraEntity.position().distanceTo(this.pos.getCenter()) < (double)OttConfig.WEATHER.PARTICLE_RADIUS.get() - (double)OttConfig.WEATHER.PARTICLE_RADIUS.get() / 2.0) {
                    for(int i = 0; i < OttConfig.WEATHER.RAIN.SPLASH_DENSITY.get(); ++i) {
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
                        if (height != 0.0 && (double)raycastHit.distanceToSqr(new Vec2((float)spawnPos.x, (float)spawnPos.z)) < 0.01) {
                            if (OttConfig.WEATHER.DO_RIPPLE_PARTICLES.get() && fluidState.isSourceOfType(Fluids.WATER)) {
                                if (height != 1.0) {
                                    Minecraft.getInstance().particleEngine.createParticle(ModParticle.RIPPLE.get(), spawnPos.x, spawnPos.y + height, spawnPos.z, 0.0, 0.0, 0.0);
                                    if (this.level.isThundering() && OttConfig.WEATHER.DO_SPLASH_PARTICLES.get()) {
                                        double var26 = spawnPos.y + height;
                                        Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.RAIN, spawnPos.x, var26, spawnPos.z, 0.0, 0.0, 0.0);
                                    }
                                }
                            } else if (OttConfig.WEATHER.DO_SMOKE_PARTICLES.get() && (blockState.is(BlockTags.INFINIBURN_OVERWORLD) || blockState.is(BlockTags.STRIDER_WARM_BLOCKS))) {
                                double var24 = spawnPos.y + height;
                                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.SMOKE, spawnPos.x, var24, spawnPos.z, 0.0, 0.0, 0.0);
                                if (this.level.isThundering()) {
                                    var24 = spawnPos.y + height;
                                    Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.LARGE_SMOKE, spawnPos.x, var24, spawnPos.z, 0.0, 0.0, 0.0);
                                }
                            } else if (OttConfig.WEATHER.DO_SPLASH_PARTICLES.get()) {
                                double var10003 = spawnPos.y + height;
                                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.RAIN, spawnPos.x, var10003, spawnPos.z, 0.0, 0.0, 0.0);
                            }
                        }
                    }
                }
            }

            this.remove();
        }
    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float tickPercentage) {
        Vector3f localPos = this.getRelativePosition(camera, tickPercentage);
        Vector3f delta = new Vector3f((float)this.xd, (float)this.yd, (float)this.zd);
        float angle = Math.acos(delta.normalize().y);
        Vector3f axis = (new Vector3f(-delta.z(), 0.0F, delta.x())).normalize();
        Quaternionf quaternion = new Quaternionf(new AxisAngle4f(-angle, axis));
        quaternion.mul(com.mojang.math.Axis.YN.rotation(this.roll));
        quaternion = this.flipItTurnwaysIfBackfaced(quaternion, localPos);
        this.renderRotatedQuad(vertexConsumer, quaternion, localPos.x, localPos.y, localPos.z, tickPercentage);
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        public DefaultFactory(SpriteSet provider) {
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new RainParticle(level, x, y, z);
        }
    }
}
