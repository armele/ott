package com.otterly76.ott.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;

public interface ContainerUser {
    boolean hasContainerOpen(ContainerOpenersCounter counter, BlockPos pos);
    double getContainerInteractionRange();
    default LivingEntity getLivingEntity() {
        return (LivingEntity) this;
    }
}
