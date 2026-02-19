package com.otterly76.ott.client.render.entity;

import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LeashState {
    public Vec3 offset;
    public Vec3 start;
    public Vec3 end;
    public int startBlockLight;
    public int endBlockLight;
    public int startSkyLight;
    public int endSkyLight;
    public boolean slack;

    public LeashState() {
        this.offset = Vec3.ZERO;
        this.start = Vec3.ZERO;
        this.end = Vec3.ZERO;
        this.startBlockLight = 0;
        this.endBlockLight = 0;
        this.startSkyLight = 15;
        this.endSkyLight = 15;
        this.slack = true;
    }
}
