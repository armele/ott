package com.otterly76.ott.entity.ai.goal;

import com.otterly76.ott.entity.custom.OtterEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class OtterPanicGoal extends PanicGoal {

    private final OtterEntity otter;

    public OtterPanicGoal(OtterEntity otter, double speedModifier) {
        super(otter, speedModifier);
        this.otter = otter;
    }

    @Override
    public void start() {
        super.start();
        otter.rejectFood();
    }
}