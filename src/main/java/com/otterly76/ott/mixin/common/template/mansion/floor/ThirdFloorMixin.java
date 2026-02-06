package com.otterly76.ott.mixin.common.template.mansion.floor;

import com.otterly76.ott.duck.MansionRoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces$ThirdFloorRoomCollection")
public abstract class ThirdFloorMixin implements MansionRoom {
    @Unique
    public int ott$floorNumber() {
        return 3;
    }
}
