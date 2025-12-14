package com.otterly76.ott.particle;

import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class WeatherParticle extends TextureSheetParticle {
    protected BlockPos.MutableBlockPos pos;
    boolean shouldFadeOut = false;
    float temperature;

    protected WeatherParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSize(0.01F, 0.01F);
        this.lifetime = 32 * 10;
        this.alpha = 0.0F;
        this.pos = new BlockPos.MutableBlockPos(x, y, z);
        this.temperature = level.getBiome(this.pos).value().getBaseTemperature();
    }

    public void tick() {
        super.tick();
        this.pos.set(this.x, this.y - 0.2, this.z);
        this.removeIfOOB();
        if (this.shouldFadeOut) {
            this.fadeOut();
        } else if (this.age % 10 == 0) {
            if ((double)Mth.abs(this.level.getBiome(this.pos).value().getBaseTemperature() - this.temperature) > 0.4) {
                this.shouldFadeOut = true;
            }
        } else {
            this.fadeIn();
        }
    }

    public void fadeIn() {
        if (this.age < 20) {
            this.alpha = (float) this.age / 20.0F;
        }
    }

    public void fadeOut() {
        if ((double)this.alpha < 0.01) {
            this.remove();
        } else {
            this.alpha -= 0.05F;
        }
    }

    public void remove() {
        super.remove();
    }

    void removeIfOOB() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity == null || cameraEntity.distanceToSqr(this.x, this.y, this.z) > Mth.square(32.0)) {
            this.shouldFadeOut = true;
        }
    }

    protected boolean removeIfObstructed() {
        if (this.x != this.xo && this.z != this.zo) {
            return false;
        } else {
            this.remove();
            return true;
        }
    }

    public void flipItTurnwaysIfBackfaced(Quaternionf quaternion, Vector3f toCamera) {
        Vector3f normal = new Vector3f(0.0F, 0.0F, 1.0F);
        normal.rotate(quaternion).normalize();
        float dot = normal.dot(toCamera);
        if (dot > 0.0F) {
            quaternion.mul(Axis.YP.rotation((float) Math.PI));
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    protected Vector3f getInterpolatedRelPos(Camera camera, float tickPercent) {
        Vec3 camPos = camera.getPosition();
        return new Vector3f(
                (float)(Mth.lerp(tickPercent, this.xo, this.x) - camPos.x()),
                (float)(Mth.lerp(tickPercent, this.yo, this.y) - camPos.y()),
                (float)(Mth.lerp(tickPercent, this.zo, this.z) - camPos.z())
        );
    }
}