package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ShrubParticle extends WeatherParticle {
    protected ShrubParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.quadSize = 0.5F;
        this.gravity = OttConfig.WEATHER.SHRUB.GRAVITY.get().floatValue();
        this.xd = OttConfig.WEATHER.SAND.WIND_STRENGTH.get();
        this.zd = OttConfig.WEATHER.SAND.WIND_STRENGTH.get();
        if (OttConfig.WEATHER.SAND.SPAWN_ON_GROUND.get()) {
            this.yd = 0.1;
        }

        ItemStack itemStack = new ItemStack(Items.DEAD_BUSH);
        BlockState blockState = level.getBlockState(level.getHeightmapPos(Types.MOTION_BLOCKING, this.pos));
        if (blockState.is(BlockTags.SWORD_EFFICIENT)) {
            if (!blockState.is(BlockTags.CROPS)) {
                itemStack = blockState.getBlock().asItem().getDefaultInstance();
                BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(itemStack, level, null, 0);
                TextureAtlasSprite particleIcon = model.getParticleIcon(ModelData.EMPTY);

                try {
                    String namespace = particleIcon.contents().name().getNamespace();
                    ResourceLocation resourceLocation = ResourceLocation.parse(namespace + ":models/" + particleIcon.contents().name().getPath() + ".json");
                    Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(resourceLocation);

                    String string;
                    try (InputStream inputStream = resource.open()) {
                        string = new String(inputStream.readAllBytes());
                    }

                    if (string.contains("tint")) {
                        int colorInt = BiomeColors.getAverageFoliageColor(level, this.pos);
                        Color color = new Color(colorInt);
                        this.setColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if ((double)level.random.nextFloat() < 0.9) {
            this.remove();
        }

        BakedModel finalModel = Minecraft.getInstance().getItemRenderer().getModel(itemStack, level, null, 0);
        this.setSprite(finalModel.getParticleIcon(ModelData.EMPTY));
    }

    public void tick() {
        super.tick();
        this.removeIfObstructed();
        if (!this.level.getFluidState(this.pos).isEmpty()) {
            this.shouldFadeOut = true;
            this.gravity = 0.0F;
        } else {
            this.xd = 0.2;
            this.zd = 0.2;
        }

        this.oRoll = this.roll;
        this.roll += OttConfig.WEATHER.SHRUB.ROTATION_AMOUNT.get().floatValue();
        if (this.onGround) {
            this.yd = OttConfig.WEATHER.SHRUB.BOUNCINESS.get();
        }
    }

    public void fadeIn() {
        if (this.age < 10) {
            this.alpha = (float)this.age / 10.0F;
        }
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float tickPercentage) {
        Vector3f localPos = this.getRelativePosition(camera, tickPercentage);
        float angle = (float)Math.atan2(this.xd, this.zd);
        Quaternionf quaternion = new Quaternionf();
        quaternion.rotateY(angle);
        Quaternionf quat1 = new Quaternionf(new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F));
        Quaternionf quat2 = new Quaternionf(new AxisAngle4f(((float)java.lang.Math.PI / 2F), 0.0F, 1.0F, 0.0F));
        quat1.mul(quaternion).rotateX(Mth.lerp(tickPercentage, this.oRoll, this.roll));
        quat2.mul(quaternion).rotateZ(Mth.lerp(tickPercentage, this.oRoll, this.roll));
        quat1 = this.flipItTurnwaysIfBackfaced(quat1, localPos);
        quat2 = this.flipItTurnwaysIfBackfaced(quat2, localPos);
        this.renderRotatedQuad(vertexConsumer, quat1, localPos.x, localPos.y, localPos.z, tickPercentage);
        this.renderRotatedQuad(vertexConsumer, quat2, localPos.x, localPos.y, localPos.z, tickPercentage);
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        public DefaultFactory(SpriteSet provider) {
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ShrubParticle(level, x, y, z);
        }
    }
}
