package com.otterly76.ott.entity.ai.navigation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class SmoothSwimmingMoveControlButNotBad extends MoveControl {
    private final int maxTurnX;
    private final int maxTurnY;
    private final float inWaterSpeedModifier;
    private final float outsideWaterSpeedModifier;

    public SmoothSwimmingMoveControlButNotBad(Mob pMob, int pMaxTurnX, int pMaxTurnY, float pInWaterSpeedModifier, float pOutsideWaterSpeedModifier, boolean pApplyGravity) {
        super(pMob);
        this.maxTurnX = pMaxTurnX;
        this.maxTurnY = pMaxTurnY;
        this.inWaterSpeedModifier = pInWaterSpeedModifier;
        this.outsideWaterSpeedModifier = pOutsideWaterSpeedModifier;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double dx = this.wantedX - this.mob.getX();
            double dy = this.wantedY - this.mob.getY();
            double dz = this.wantedZ - this.mob.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;
            if (distSq < 2.5000003E-7F) {
                this.mob.setZza(0.0F);
            } else {
                float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYaw, (float) this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                this.mob.yHeadRot = this.mob.getYRot();
                float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.mob.isInWater()) {
                    this.mob.setSpeed(speed * this.inWaterSpeedModifier);
                    double horizontalDist = Math.sqrt(dx * dx + dz * dz);
                    if (Math.abs(dy) > 1.0E-5 || Math.abs(horizontalDist) > 1.0E-5) {
                        float targetPitch = -((float) (Mth.atan2(dy, horizontalDist) * (180.0 / Math.PI)));
                        targetPitch = Mth.clamp(Mth.wrapDegrees(targetPitch), (float) (-this.maxTurnX), (float) this.maxTurnX);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), targetPitch, 5.0F));
                    }

                    float cosPitch = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180F));
                    float sinPitch = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180F));
                    this.mob.zza = cosPitch * speed;
                    this.mob.yya = -sinPitch * speed;
                } else {
                    float yawDiff = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - targetYaw));
                    float turningFactor = getTurningSpeedFactor(yawDiff);
                    this.mob.setSpeed(speed * this.outsideWaterSpeedModifier * turningFactor);
                }
            }
        } else {
            this.mob.setSpeed(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setXxa(0.0F);
            this.mob.setZza(0.0F);
        }
    }

    private static float getTurningSpeedFactor(float yawDiff) {
        return 1.0F - Mth.clamp((yawDiff - 10.0F) / 50.0F, 0.0F, 1.0F);
    }
}
