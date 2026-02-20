package com.otterly76.ott.entity.ai.goal;

import com.otterly76.ott.block.custom.CopperButtonBlock;
import com.otterly76.ott.entity.custom.CopperGolem;
import com.otterly76.ott.entity.custom.CopperGolemState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PressCopperButtonGoal extends Goal {
    private final CopperGolem golem;
    private final double speedModifier;
    private final int searchRange;
    private BlockPos targetPos;
    private int timer;

    public PressCopperButtonGoal(CopperGolem golem, double speedModifier, int searchRange) {
        this.golem = golem;
        this.speedModifier = speedModifier;
        this.searchRange = searchRange;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.golem.getGolemState() != CopperGolemState.IDLE) {
            return false;
        }
        if (this.golem.getRandom().nextInt(100) != 0) {
            return false;
        }
        this.targetPos = this.findButton();
        return this.targetPos != null;
    }

    @Override
    public void start() {
        this.golem.getNavigation().moveTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), this.speedModifier);
        this.timer = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetPos != null && this.timer < 200 && !this.golem.getNavigation().isDone();
    }

    @Override
    public void tick() {
        this.timer++;
        this.golem.getLookControl().setLookAt(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos.getZ() + 0.5D);
        if (this.golem.distanceToSqr(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos.getZ() + 0.5D) < 2.0D) {
            this.pressButton();
            this.targetPos = null;
        }
    }

    private void pressButton() {
        BlockState state = this.golem.level().getBlockState(this.targetPos);
        if (state.getBlock() instanceof ButtonBlock button) {
            this.golem.setGolemState(CopperGolemState.PRESSING_BUTTON);
            button.press(state, this.golem.level(), this.targetPos, null);
            // In a real implementation, we'd wait for animation to finish
            this.golem.setGolemState(CopperGolemState.IDLE);
        }
    }

    private @Nullable BlockPos findButton() {
        Level level = this.golem.level();
        BlockPos pos = this.golem.blockPosition();
        List<BlockPos> buttons = new ArrayList<>();
        for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-searchRange, -searchRange / 2, -searchRange), pos.offset(searchRange, searchRange / 2, searchRange))) {
            BlockState state = level.getBlockState(blockpos);
            if (state.getBlock() instanceof CopperButtonBlock) {
                buttons.add(blockpos.immutable());
            }
        }
        if (buttons.isEmpty()) return null;
        return buttons.get(this.golem.getRandom().nextInt(buttons.size()));
    }
}