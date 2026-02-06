package com.otterly76.ott.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.otterly76.ott.Constants.MOD_ID;

public class RippleParticle extends WeatherParticle {
    private RippleParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSprite(Minecraft.getInstance().particleEngine.textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ripple0")));
        this.quadSize = 0.25F;
        this.alpha = 0.1F;
        this.x = (float)Math.round(this.x / (double)0.0625F) * 0.0625F;
        this.z = (float)Math.round(this.z / (double)0.0625F) * 0.0625F;
    }

    public void tick() {
        super.tick();
        this.alpha = Mth.lerp((float)this.age / 9.0F, 0.3F, 0.0F);
        if (this.age > 8) {
            this.remove();
        }

        this.setSprite(Minecraft.getInstance().particleEngine.textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ripple" + (this.age - 1))));
    }

    public void fadeIn() {
    }

    public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float f) {
        Vec3 camPos = camera.getPosition();
        float x = (float)(Mth.lerp(f, this.xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(f, this.yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(f, this.zo, this.z) - camPos.z());
        Quaternionf quaternion = new Quaternionf(new AxisAngle4d((float)java.lang.Math.PI / 2F, -1.0F, 0.0F, 0.0F));
        this.flipItTurnwaysIfBackfaced(quaternion, new Vector3f(x, y, z));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y, z, f);
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        public DefaultFactory(SpriteSet provider) {
        }

        public Particle createParticle(@NotNull SimpleParticleType parameters, @NotNull ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new RippleParticle(level, x, y, z);
        }
    }
}
