package com.otterly76.ott.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class StarlightCreepOverlayRenderer {
    private StarlightCreepOverlayRenderer() {
    }

    private static final ResourceLocation CREEP_SPRITE_ID =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/starlight_creep");

    /**
     * chunkKey -> set of hedge BlockPos as long (BlockPos#asLong).
     */
    private static final Long2ObjectOpenHashMap<LongSet> HEDGES_BY_CHUNK = new Long2ObjectOpenHashMap<>();

    /**
     * Render only hedges within this distance of the camera (in blocks).
     */
    private static final int RENDER_RADIUS_BLOCKS = 96;
    private static final int RENDER_RADIUS_SQ = RENDER_RADIUS_BLOCKS * RENDER_RADIUS_BLOCKS;

    // 8-block buffer to reduce popping at the edge
    private static final int RENDER_RADIUS_BUFFER_BLOCKS = 8;
    private static final int RENDER_RADIUS_SQ_BUFFERED =
            (RENDER_RADIUS_BLOCKS + RENDER_RADIUS_BUFFER_BLOCKS) * (RENDER_RADIUS_BLOCKS + RENDER_RADIUS_BUFFER_BLOCKS);

    private static final int FADE_BAND_BLOCKS = 16; // fade over last 16 blocks
    private static final double FADE_START = RENDER_RADIUS_BLOCKS - FADE_BAND_BLOCKS;
    private static final double FADE_START_SQ = FADE_START * FADE_START;
    private static final double FADE_END_SQ = RENDER_RADIUS_SQ_BUFFERED;

    // ---------------------------
    // Render
    // ---------------------------

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) return;

        var camVec = event.getCamera().getPosition();
        double camX = camVec.x;
        double camY = camVec.y;
        double camZ = camVec.z;

        BlockPos camPos = BlockPos.containing(camVec);

        @SuppressWarnings("deprecation")
        TextureAtlasSprite creep = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(CREEP_SPRITE_ID);

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        // IMPORTANT: Use translucent so fractional alpha actually blends.
        VertexConsumer vc = bufferSource.getBuffer(Sheets.translucentCullBlockSheet());

        // Iterate cached hedges
        for (LongSet set : HEDGES_BY_CHUNK.values()) {
            for (var it = set.iterator(); it.hasNext(); ) {
                long hedgeLong = it.nextLong();
                BlockPos hedgePos = BlockPos.of(hedgeLong);

                // ... existing code ...
                double ddx = (hedgePos.getX() + 0.5) - camX;
                double ddy = (hedgePos.getY() + 0.5) - camY;
                double ddz = (hedgePos.getZ() + 0.5) - camZ;
                double distSq = ddx * ddx + ddy * ddy + ddz * ddz;

                if (distSq > FADE_END_SQ) continue;

                float alpha;
                if (distSq <= FADE_START_SQ) {
                    alpha = 1.0f;
                } else {
                    double t = (distSq - FADE_START_SQ) / (FADE_END_SQ - FADE_START_SQ);
                    alpha = (float) (1.0 - Math.max(0.0, Math.min(1.0, t)));
                }

                // If it's extremely faint, skip to save a bit of fill-rate
                if (alpha <= 0.01f) continue;

                if (!level.getBlockState(hedgePos).is(ModBlocks.STARLIGHT_CREEPING_HEDGE.get())) continue;

                renderOverlayOnCandidate(level, poseStack, vc, creep, hedgePos.below(), camX, camY, camZ, alpha);
                renderOverlayOnCandidate(level, poseStack, vc, creep, hedgePos.above(), camX, camY, camZ, alpha);
            }
        }

        // Don’t endBatch here; vanilla manages flushing for level rendering.
    }

    // ---------------------------
    // Cache maintenance
    // ---------------------------

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (!(event.getLevel() instanceof Level level)) return;
        if (!level.isClientSide()) return;

        HEDGES_BY_CHUNK.clear();
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof Level level)) return;
        if (!level.isClientSide()) return;
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;

        long key = chunkKey(chunk.getPos().x, chunk.getPos().z);

        LongOpenHashSet set = new LongOpenHashSet();

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();

        BlockPos.MutableBlockPos mp = new BlockPos.MutableBlockPos();
        int baseX = chunk.getPos().getMinBlockX();
        int baseZ = chunk.getPos().getMinBlockZ();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int wx = baseX + x;
                int wz = baseZ + z;

                for (int y = minY; y < maxY; y++) {
                    mp.set(wx, y, wz);
                    if (chunk.getBlockState(mp).is(ModBlocks.STARLIGHT_CREEPING_HEDGE.get())) {
                        set.add(mp.asLong());
                    }
                }
            }
        }

        if (set.isEmpty()) {
            HEDGES_BY_CHUNK.remove(key);
        } else {
            HEDGES_BY_CHUNK.put(key, set);
        }
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        if (!(event.getLevel() instanceof Level level)) return;
        if (!level.isClientSide()) return;
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;

        long key = chunkKey(chunk.getPos().x, chunk.getPos().z);
        HEDGES_BY_CHUNK.remove(key);
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof Level level)) return;
        if (!level.isClientSide()) return;

        BlockPos pos = event.getPos();
        if (!event.getPlacedBlock().is(ModBlocks.STARLIGHT_CREEPING_HEDGE.get())) return;

        LongSet set = HEDGES_BY_CHUNK.computeIfAbsent(chunkKey(pos.getX() >> 4, pos.getZ() >> 4), k -> new LongOpenHashSet());
        set.add(pos.asLong());
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof Level level)) return;
        if (!level.isClientSide()) return;

        BlockPos pos = event.getPos();

        // If it wasn't a hedge, ignore
        if (!event.getState().is(ModBlocks.STARLIGHT_CREEPING_HEDGE.get())) return;

        long key = chunkKey(pos.getX() >> 4, pos.getZ() >> 4);
        LongSet set = HEDGES_BY_CHUNK.get(key);
        if (set == null) return;

        set.remove(pos.asLong());
        if (set.isEmpty()) {
            HEDGES_BY_CHUNK.remove(key);
        }
    }

    private static long chunkKey(int chunkX, int chunkZ) {
        return (((long) chunkX) << 32) ^ (chunkZ & 0xffffffffL);
    }

    // ---------------------------
    // Overlay render (same logic you already have)
    // ---------------------------

    private static void renderOverlayOnCandidate(Level level, PoseStack poseStack, VertexConsumer vc, TextureAtlasSprite sprite,
                                                 BlockPos pos, double camX, double camY, double camZ,
                                                 float alpha) {
        BlockState state = level.getBlockState(pos);

        if (state.getRenderShape() != RenderShape.MODEL) return;
        if (!state.isCollisionShapeFullBlock(level, pos)) return;
        if (state.is(ModBlocks.STARLIGHT_CREEPING_HEDGE.get())) return;

        boolean hedgeAbove = level.getBlockState(pos.above()).is(ModBlocks.STARLIGHT_CREEPING_HEDGE.get());
        boolean hedgeBelow = level.getBlockState(pos.below()).is(ModBlocks.STARLIGHT_CREEPING_HEDGE.get());
        if (!hedgeAbove && !hedgeBelow) return;

        int light = 0x00F000F0;
        boolean flipV = hedgeAbove && !hedgeBelow;

        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.NORTH, light, camX, camY, camZ, flipV, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.SOUTH, light, camX, camY, camZ, flipV, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.WEST, light, camX, camY, camZ, flipV, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.EAST, light, camX, camY, camZ, flipV, alpha);
    }

    private static void renderFaceIfExposed(Level level, PoseStack poseStack, VertexConsumer vc, TextureAtlasSprite s,
                                            BlockPos pos, Direction face, int light,
                                            double camX, double camY, double camZ,
                                            boolean flipV, float alpha) {
        BlockPos neighborPos = pos.relative(face);
        BlockState neighbor = level.getBlockState(neighborPos);

        if (neighbor.isCollisionShapeFullBlock(level, neighborPos)) return;

        float u0 = s.getU0();
        float u1 = s.getU1();
        float v0 = s.getV0();
        float v1 = s.getV1();

        if (flipV) {
            float tmp = v0;
            v0 = v1;
            v1 = tmp;
        }

        float eps = 0.001f;

        poseStack.pushPose();
        poseStack.translate(pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ);
        PoseStack.Pose pose = poseStack.last();

        switch (face) {
            case NORTH -> quad(vc, pose, light, 0, 0, -1, alpha,
                    1.0f, 0.0f, -eps, u0, v1,
                    0.0f, 0.0f, -eps, u1, v1,
                    0.0f, 1.0f, -eps, u1, v0,
                    1.0f, 1.0f, -eps, u0, v0
            );
            case SOUTH -> quad(vc, pose, light, 0, 0, 1, alpha,
                    0.0f, 0.0f, 1.0f + eps, u0, v1,
                    1.0f, 0.0f, 1.0f + eps, u1, v1,
                    1.0f, 1.0f, 1.0f + eps, u1, v0,
                    0.0f, 1.0f, 1.0f + eps, u0, v0
            );
            case WEST -> quad(vc, pose, light, -1, 0, 0, alpha,
                    -eps, 0.0f, 0.0f, u0, v1,
                    -eps, 0.0f, 1.0f, u1, v1,
                    -eps, 1.0f, 1.0f, u1, v0,
                    -eps, 1.0f, 0.0f, u0, v0
            );
            case EAST -> quad(vc, pose, light, 1, 0, 0, alpha,
                    1.0f + eps, 0.0f, 1.0f, u0, v1,
                    1.0f + eps, 0.0f, 0.0f, u1, v1,
                    1.0f + eps, 1.0f, 0.0f, u1, v0,
                    1.0f + eps, 1.0f, 1.0f, u0, v0
            );
        }

        poseStack.popPose();
    }

    @SuppressWarnings({"SameParameterValue", "DuplicatedCode"})
    private static void quad(VertexConsumer vc, PoseStack.Pose pose, int light, float nx, float ny, float nz,
                             float alpha,
                             float x0, float y0, float z0, float u0, float v0,
                             float x1, float y1, float z1, float u1, float v1,
                             float x2, float y2, float z2, float u2, float v2,
                             float x3, float y3, float z3, float u3, float v3) {

        int overlay = OverlayTexture.NO_OVERLAY;

        // Clamp + convert 0..1 -> 0..255 once
        int a = (int) (255.0f * Math.max(0.0f, Math.min(1.0f, alpha)));

        vc.addVertex(pose, x0, y0, z0).setColor(255, 255, 255, a).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x1, y1, z1).setColor(255, 255, 255, a).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x2, y2, z2).setColor(255, 255, 255, a).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x3, y3, z3).setColor(255, 255, 255, a).setUv(u3, v3).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
    }
}