package com.otterly76.ott.entity.ai.navigation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class SmartBodyHelper extends BodyRotationControl {
    private static final int HISTORY_SIZE = 10;
    private static final double MOVE_THRESHOLD = 2.5E-7;
    public float bodyLagMoving;
    public float headLag;
    public float bodyLagStill;
    public float bodyMax;
    public float headMax;
    private final double[] histPosX = new double[10];
    private final double[] histPosZ = new double[10];
    protected final Mob entity;

    public SmartBodyHelper(Mob entity) {
        super(entity);
        this.entity = entity;
        this.bodyLagMoving = 0.3F;
        this.headLag = 0.2F;
        this.bodyLagStill = 0.05F;
        this.bodyMax = 45.0F;
        this.headMax = 22.5F;
    }

    @Override
    public void clientTick() {
        for (int i = 9; i > 0; --i) {
            this.histPosX[i] = this.histPosX[i - 1];
            this.histPosZ[i] = this.histPosZ[i - 1];
        }

        this.histPosX[0] = this.entity.getX();
        this.histPosZ[0] = this.entity.getZ();
        double dx = this.avgDelta(this.histPosX);
        double dz = this.avgDelta(this.histPosZ);
        double distSq = dx * dx + dz * dz;
        if (this.entity.getTarget() != null) {
            double tx = this.entity.getTarget().getX() - this.entity.getX();
            double tz = this.entity.getTarget().getZ() - this.entity.getZ();
            float targetAngle = (float) (Mth.atan2(tz, tx) * (180.0 / Math.PI)) - 90.0F;
            this.entity.yBodyRot = this.approachAngle(this.entity.yBodyRot, targetAngle, this.bodyLagMoving, this.bodyMax);
            this.entity.yHeadRot = this.approachAngle(this.entity.yHeadRot, targetAngle, this.headLag, this.headMax);
            this.clampHeadBodyDifference();
        } else if (distSq > MOVE_THRESHOLD) {
            float moveAngle = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
            this.entity.yBodyRot = this.approachAngle(this.entity.yBodyRot, moveAngle, this.bodyLagMoving, this.bodyMax);
            this.entity.yHeadRot = this.approachAngle(this.entity.yHeadRot, this.entity.yBodyRot, this.headLag, this.headMax);
            this.clampHeadBodyDifference();
        } else {
            this.entity.yBodyRot = this.approachAngle(this.entity.yBodyRot, this.entity.yHeadRot, this.bodyLagStill, this.bodyMax);
            this.clampHeadBodyDifference();
        }
    }

    private double avgDelta(double[] arr) {
        return this.mean(arr, 0) - this.mean(arr, 5);
    }

    private double mean(double[] arr, int start) {
        double s = 0.0;
        int half = 5;

        for (int i = 0; i < half; ++i) {
            s += arr[start + i];
        }

        return s / (double) half;
    }

    private float approachAngle(float current, float target, float factor, float maxDelta) {
        float d = Mth.wrapDegrees(target - current);
        if (d < -maxDelta) {
            d = -maxDelta;
        } else if (d > maxDelta) {
            d = maxDelta;
        }

        return current + d * factor;
    }

    private void clampHeadBodyDifference() {
        float diff = Mth.wrapDegrees(this.entity.yHeadRot - this.entity.yBodyRot);
        float clamped = Mth.clamp(diff, -this.headMax, this.headMax);
        this.entity.yHeadRot = this.entity.yBodyRot + clamped;
    }
}
