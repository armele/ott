package com.otterly76.ott.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Queue;

/**
 * Post-terrain cleanup feature that removes small floating solid blocks
 * (density-function interpolation artifacts near steep cliffs/spikes).
 * BFS from bedrock level marks every solid block reachable from the ground;
 * remaining disconnected groups up to MAX_FLOATER_SIZE blocks are removed.
 */
public class FloaterCleanerFeature extends Feature<NoneFeatureConfiguration> {

    /** Disconnected solid groups with this many blocks or fewer are erased. */
    private static final int MAX_FLOATER_SIZE = 384;

    /** Scan width: chunk 16 + 1-block border on each side. */
    private static final int W  = 18;
    private static final int WW = W * W; // 324

    public FloaterCleanerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level  = context.level();
        ChunkPos      chunk  = new ChunkPos(context.origin());
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int baseX = chunk.getMinBlockX();
        int baseZ = chunk.getMinBlockZ();

        // Local coords: lx ∈ [0,17], lz ∈ [0,17], ly ∈ [0,H-1]
        // World:  wx = baseX + lx - 1,  wz = baseZ + lz - 1,  wy = minY + 1 + ly
        final int H    = maxY - minY - 1;
        final int SIZE = H * WW;

        BitSet solid     = new BitSet(SIZE);
        BitSet connected = new BitSet(SIZE);
        BitSet processed = new BitSet(SIZE);

        // ---- Pass 1: record solid blocks ----------------------------------------
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (int lx = 0; lx < W; lx++) {
            int wx = baseX + lx - 1;
            for (int lz = 0; lz < W; lz++) {
                int wz = baseZ + lz - 1;
                for (int ly = 0; ly < H; ly++) {
                    mpos.set(wx, minY + 1 + ly, wz);
                    if (!FallingBlock.isFree(level.getBlockState(mpos))) {
                        solid.set(ly * WW + lx * W + lz);
                    }
                }
            }
        }

        // ---- Pass 2: BFS from ly=0 (just above bedrock at minY) -----------------
        Queue<Integer> queue = new ArrayDeque<>();
        for (int lx = 0; lx < W; lx++) {
            for (int lz = 0; lz < W; lz++) {
                int i = lx * W + lz; // ly = 0
                if (solid.get(i)) {
                    connected.set(i);
                    queue.add(i);
                }
            }
        }

        while (!queue.isEmpty()) {
            int i  = queue.poll();
            int ly = i / WW;
            int lx = (i % WW) / W;
            int lz = i % W;
            tryConnect(lx + 1, ly,     lz,     H, solid, connected, queue);
            tryConnect(lx - 1, ly,     lz,     H, solid, connected, queue);
            tryConnect(lx,     ly + 1, lz,     H, solid, connected, queue);
            tryConnect(lx,     ly - 1, lz,     H, solid, connected, queue);
            tryConnect(lx,     ly,     lz + 1, H, solid, connected, queue);
            tryConnect(lx,     ly,     lz - 1, H, solid, connected, queue);
        }

        // ---- Pass 3: collect and erase small disconnected components -------------
        boolean removed = false;
        for (int lx = 1; lx <= 16; lx++) {
            for (int lz = 1; lz <= 16; lz++) {
                for (int ly = 0; ly < H; ly++) {
                    int start = ly * WW + lx * W + lz;
                    if (!solid.get(start) || connected.get(start) || processed.get(start)) continue;

                    List<Integer> component = new ArrayList<>();
                    Queue<Integer> cq = new ArrayDeque<>();
                    cq.add(start);
                    processed.set(start);
                    component.add(start);
                    boolean tooLarge = false;

                    while (!cq.isEmpty()) {
                        int ci  = cq.poll();
                        int cy  = ci / WW;
                        int cx  = (ci % WW) / W;
                        int cz  = ci % W;
                        // Once too large, pass null to skip collecting; BFS continues to mark processed
                        List<Integer> col = tooLarge ? null : component;
                        tryCollect(cx + 1, cy,     cz,     H, solid, connected, processed, col, cq);
                        tryCollect(cx - 1, cy,     cz,     H, solid, connected, processed, col, cq);
                        tryCollect(cx,     cy + 1, cz,     H, solid, connected, processed, col, cq);
                        tryCollect(cx,     cy - 1, cz,     H, solid, connected, processed, col, cq);
                        tryCollect(cx,     cy,     cz + 1, H, solid, connected, processed, col, cq);
                        tryCollect(cx,     cy,     cz - 1, H, solid, connected, processed, col, cq);
                        if (!tooLarge && component.size() > MAX_FLOATER_SIZE) tooLarge = true;
                    }

                    if (tooLarge) continue;

                    for (int ci : component) {
                        int py = ci / WW;
                        int px = (ci % WW) / W;
                        int pz = ci % W;
                        if (px < 1 || px > 16 || pz < 1 || pz > 16) continue;
                        mpos.set(baseX + px - 1, minY + 1 + py, baseZ + pz - 1);
                        level.setBlock(mpos, Blocks.AIR.defaultBlockState(), 3);
                    }
                    removed = true;
                }
            }
        }
        return removed;
    }

    private static void tryConnect(int lx, int ly, int lz, int H,
                                   BitSet solid, BitSet connected, Queue<Integer> queue) {
        if (lx < 0 || lx >= W || ly < 0 || ly >= H || lz < 0 || lz >= W) return;
        int i = ly * WW + lx * W + lz;
        if (solid.get(i) && !connected.get(i)) {
            connected.set(i);
            queue.add(i);
        }
    }

    private static void tryCollect(int lx, int ly, int lz, int H,
                                   BitSet solid, BitSet connected, BitSet processed,
                                   @Nullable List<Integer> component, Queue<Integer> cq) {
        if (lx < 0 || lx >= W || ly < 0 || ly >= H || lz < 0 || lz >= W) return;
        int i = ly * WW + lx * W + lz;
        if (solid.get(i) && !connected.get(i) && !processed.get(i)) {
            processed.set(i);
            if (component != null) component.add(i);
            cq.add(i);
        }
    }
}
