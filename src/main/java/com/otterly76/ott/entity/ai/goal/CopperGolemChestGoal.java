package com.otterly76.ott.entity.ai.goal;

import com.otterly76.ott.block.custom.CopperChestBlock;
import com.otterly76.ott.entity.custom.CopperGolem;
import com.otterly76.ott.entity.custom.CopperGolemState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CopperGolemChestGoal extends Goal {
    private final CopperGolem golem;
    private final double speed;
    private final List<BlockPos> visitedChests = new ArrayList<>();
    private BlockPos targetPos;
    private int navigationTick;
    private int interactionTimer;

    public CopperGolemChestGoal(CopperGolem golem, double speed) {
        this.golem = golem;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.golem.getRandom().nextInt(20) != 0) return false;
        
        this.targetPos = this.findTargetChest();
        return this.targetPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetPos != null && this.interactionTimer < 100;
    }

    @Override
    public void start() {
        this.navigationTick = 0;
        this.interactionTimer = 0;
        this.golem.getNavigation().moveTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), this.speed);
    }

    @Override
    public void stop() {
        this.targetPos = null;
        this.golem.getNavigation().stop();
        this.golem.setGolemState(CopperGolemState.IDLE);
    }

    @Override
    public void tick() {
        if (this.targetPos == null) return;

        this.golem.getLookControl().setLookAt(this.targetPos.getX() + 0.5, this.targetPos.getY() + 0.5, this.targetPos.getZ() + 0.5);
        
        double dist = this.golem.distanceToSqr(this.targetPos.getX() + 0.5, this.targetPos.getY() + 0.5, this.targetPos.getZ() + 0.5);
        
        if (dist < 6.0) { // Increased interaction range
            this.golem.getNavigation().stop();
            this.interactionTimer++;
            
            boolean hasItem = !this.golem.getInventory().getItem(0).isEmpty();
            if (this.interactionTimer == 1) {
                this.golem.setGolemState(hasItem ? CopperGolemState.DROPPING_ITEM : CopperGolemState.GETTING_ITEM);
                this.golem.level().blockEvent(this.targetPos, this.golem.level().getBlockState(this.targetPos).getBlock(), 1, 1);
            }

            if (this.interactionTimer >= 60) { // 3 seconds
                this.interactWithChest();
                this.golem.level().blockEvent(this.targetPos, this.golem.level().getBlockState(this.targetPos).getBlock(), 1, 0);
                this.interactionTimer = 0;
                this.stop();
            }
        } else {
            this.navigationTick++;
            if (this.navigationTick % 20 == 0) {
                this.golem.getNavigation().moveTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), this.speed);
            }
            if (this.navigationTick > 400) {
                this.stop();
            }
        }
    }

    private void interactWithChest() {
        Level level = this.golem.level();
        BlockEntity be = level.getBlockEntity(this.targetPos);
        if (!(be instanceof ChestBlockEntity chest)) {
            // System.out.println("[DEBUG_LOG] Interaction failed: Block at " + this.targetPos + " is not a ChestBlockEntity");
            return;
        }

        ItemStack heldItem = this.golem.getInventory().getItem(0);
        
        if (heldItem.isEmpty()) {
            // Try to take from copper chest
            if (level.getBlockState(this.targetPos).getBlock() instanceof CopperChestBlock) {
                for (int i = 0; i < chest.getContainerSize(); i++) {
                    ItemStack stack = chest.getItem(i);
                    if (!stack.isEmpty()) {
                        ItemStack taken = chest.removeItem(i, 16);
                        this.golem.getInventory().setItem(0, taken);
                        // System.out.println("[DEBUG_LOG] Golem took " + taken + " from copper chest at " + this.targetPos);
                        this.visitedChests.clear();
                        return;
                    }
                }
                // System.out.println("[DEBUG_LOG] Copper chest at " + this.targetPos + " was empty");
            }
        } else {
            // Try to deposit in wooden/trapped chest
            BlockState state = level.getBlockState(this.targetPos);
            if (state.getBlock() instanceof ChestBlock && !(state.getBlock() instanceof CopperChestBlock)) {
                // Deposit logic
                for (int i = 0; i < chest.getContainerSize(); i++) {
                    ItemStack slotStack = chest.getItem(i);
                    if (slotStack.isEmpty() || (ItemStack.isSameItemSameComponents(slotStack, heldItem) && slotStack.getCount() < slotStack.getMaxStackSize())) {
                        int toAdd = Math.min(heldItem.getCount(), slotStack.getMaxStackSize() - slotStack.getCount());
                        if (slotStack.isEmpty()) {
                            chest.setItem(i, heldItem.split(toAdd));
                        } else {
                            slotStack.grow(toAdd);
                            heldItem.shrink(toAdd);
                        }
                        
                        // System.out.println("[DEBUG_LOG] Golem deposited item in wooden chest at " + this.targetPos + ". Remaining: " + heldItem.getCount());
                        if (heldItem.isEmpty()) {
                            this.visitedChests.clear();
                        }
                        return;
                    }
                }
                // System.out.println("[DEBUG_LOG] Wooden chest at " + this.targetPos + " was full or incompatible");
            }
        }
        
        // If we reached here, we couldn't interact successfully
        this.markVisited(this.targetPos);
    }

    private void markVisited(BlockPos pos) {
        this.visitedChests.add(pos.immutable());
        if (this.visitedChests.size() > 9) {
            this.visitedChests.removeFirst();
        }
    }

    private @Nullable BlockPos findTargetChest() {
        BlockPos pos = this.golem.blockPosition();
        Level level = this.golem.level();
        boolean hasItem = !this.golem.getInventory().getItem(0).isEmpty();
        
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        
        for (int r = 1; r <= 32; r++) {
            for (int y = -2; y <= 1; y++) {
                for (int x = -r; x <= r; x++) {
                    for (int z = -r; z <= r; z++) {
                        if (Math.abs(x) != r && Math.abs(z) != r) continue;
                        
                        mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        if (this.visitedChests.contains(mutable)) continue;
                        
                        BlockState state = level.getBlockState(mutable);
                        if (state.getBlock() instanceof ChestBlock) {
                            boolean isCopper = state.getBlock() instanceof CopperChestBlock;
                            if (hasItem && !isCopper) return mutable.immutable();
                            if (!hasItem && isCopper) return mutable.immutable();
                        }
                    }
                }
            }
        }
        
        if (this.visitedChests.size() >= 10) {
            this.visitedChests.clear();
        }
        
        return null;
    }
}