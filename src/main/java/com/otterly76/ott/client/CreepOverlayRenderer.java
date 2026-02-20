package com.otterly76.ott.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.Constants;
import com.otterly76.ott.block.custom.ParticleCreepingHedgeBlock;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
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
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.Iterator;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class CreepOverlayRenderer {
    private CreepOverlayRenderer() {
    }

    private static final int FULLBRIGHT = 0x00F000F0;

    private static final Long2ObjectOpenHashMap<LongSet> HEDGES_BY_CHUNK = new Long2ObjectOpenHashMap<>();

    private static final int RENDER_RADIUS_BLOCKS = 96;

    private static final int RENDER_RADIUS_BUFFER_BLOCKS = 8;
    private static final int FADE_BAND_BLOCKS = 16;

    private static final double FADE_START = RENDER_RADIUS_BLOCKS - FADE_BAND_BLOCKS;
    private static final double FADE_START_SQ = FADE_START * FADE_START;
    private static final double FADE_END_SQ =
            (RENDER_RADIUS_BLOCKS + RENDER_RADIUS_BUFFER_BLOCKS) * (RENDER_RADIUS_BLOCKS + RENDER_RADIUS_BUFFER_BLOCKS);

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

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        VertexConsumer vc = bufferSource.getBuffer(Sheets.translucentCullBlockSheet());

        for (Iterator<Long2ObjectOpenHashMap.Entry<LongSet>> mapIt = HEDGES_BY_CHUNK.long2ObjectEntrySet().fastIterator(); mapIt.hasNext(); ) {
            Long2ObjectOpenHashMap.Entry<LongSet> entry = mapIt.next();
            LongSet set = entry.getValue();

            for (LongIterator it = set.iterator(); it.hasNext(); ) {
                long hedgeLong = it.nextLong();
                BlockPos hedgePos = BlockPos.of(hedgeLong);

                BlockState hedgeState = level.getBlockState(hedgePos);
                if (!(hedgeState.getBlock() instanceof ParticleCreepingHedgeBlock phb)) {
                    it.remove();
                    continue;
                }

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

                if (alpha <= 0.01f) continue;

                @SuppressWarnings("deprecation")
                TextureAtlasSprite creepSprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .apply(phb.getOverlayTexture());

                renderOverlayOnCandidate(level, poseStack, vc, creepSprite, hedgePos.below(), camX, camY, camZ, alpha);
                renderOverlayOnCandidate(level, poseStack, vc, creepSprite, hedgePos.above(), camX, camY, camZ, alpha);

                @SuppressWarnings("deprecation")
                TextureAtlasSprite hedgeSprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .apply(hedgeSpriteIdFromCreepId(phb.getOverlayTexture()));

                renderGlowOnBlock(level, poseStack, vc, hedgeSprite, hedgePos, camX, camY, camZ, alpha * 0.35f);
            }

            if (set.isEmpty()) {
                mapIt.remove();
            }
        }
    }

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
                    if (chunk.getBlockState(mp).getBlock() instanceof ParticleCreepingHedgeBlock phb) {
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

    public static void updateHedgeCache(BlockPos pos, BlockState state) {
        long key = chunkKey(pos.getX() >> 4, pos.getZ() >> 4);
        if (state != null && state.getBlock() instanceof ParticleCreepingHedgeBlock) {
            LongSet set = HEDGES_BY_CHUNK.computeIfAbsent(key, k -> new LongOpenHashSet());
            set.add(pos.asLong());
        } else {
            LongSet set = HEDGES_BY_CHUNK.get(key);
            if (set != null) {
                set.remove(pos.asLong());
                if (set.isEmpty()) {
                    HEDGES_BY_CHUNK.remove(key);
                }
            }
        }
    }

    private static long chunkKey(int chunkX, int chunkZ) {
        return (((long) chunkX) << 32) ^ (chunkZ & 0xffffffffL);
    }

    private static void renderOverlayOnCandidate(Level level, PoseStack poseStack, VertexConsumer vc, TextureAtlasSprite sprite,
                                                 BlockPos pos, double camX, double camY, double camZ,
                                                 float alpha) {
        BlockState state = level.getBlockState(pos);

        if (state.getRenderShape() != RenderShape.MODEL) return;
        if (!state.isCollisionShapeFullBlock(level, pos)) return;
        if (state.getBlock() instanceof ParticleCreepingHedgeBlock phb) return;

        boolean hedgeAbove = level.getBlockState(pos.above()).getBlock() instanceof ParticleCreepingHedgeBlock phba;
        boolean hedgeBelow = level.getBlockState(pos.below()).getBlock() instanceof ParticleCreepingHedgeBlock phbb;
        if (!hedgeAbove && !hedgeBelow) return;

        boolean flip = hedgeAbove && !hedgeBelow;

        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.NORTH, FULLBRIGHT, camX, camY, camZ, flip, flip, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.SOUTH, FULLBRIGHT, camX, camY, camZ, flip, flip, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.WEST, FULLBRIGHT, camX, camY, camZ, flip, flip, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.EAST, FULLBRIGHT, camX, camY, camZ, flip, flip, alpha);
    }

    private static void renderGlowOnBlock(Level level, PoseStack poseStack, VertexConsumer vc, TextureAtlasSprite sprite,
                                          BlockPos pos, double camX, double camY, double camZ,
                                          float alpha) {
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.NORTH, FULLBRIGHT, camX, camY, camZ, false, false, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.SOUTH, FULLBRIGHT, camX, camY, camZ, false, false, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.WEST, FULLBRIGHT, camX, camY, camZ, false, false, alpha);
        renderFaceIfExposed(level, poseStack, vc, sprite, pos, Direction.EAST, FULLBRIGHT, camX, camY, camZ, false, false, alpha);
    }

    private static ResourceLocation hedgeSpriteIdFromCreepId(ResourceLocation creepId) {
        String path = creepId.getPath();
        String hedgePath;

        if (path.endsWith("_creep")) {
            hedgePath = path.substring(0, path.length() - "_creep".length()) + "_hedge";
        } else {
            hedgePath = path;
        }

        return ResourceLocation.fromNamespaceAndPath(creepId.getNamespace(), hedgePath);
    }

    @SuppressWarnings("SameParameterValue")
    private static void renderFaceIfExposed(Level level, PoseStack poseStack, VertexConsumer vc, TextureAtlasSprite s,
                                            BlockPos pos, Direction face, int light,
                                            double camX, double camY, double camZ,
                                            boolean flipV, boolean flipH, float alpha) {
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

        if (flipH) {
            float tmp = u0;
            u0 = u1;
            u1 = tmp;
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
            default -> {
            }
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
        int a = (int) (255.0f * Math.max(0.0f, Math.min(1.0f, alpha)));

        vc.addVertex(pose, x0, y0, z0).setColor(255, 255, 255, a).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x1, y1, z1).setColor(255, 255, 255, a).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x2, y2, z2).setColor(255, 255, 255, a).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x3, y3, z3).setColor(255, 255, 255, a).setUv(u3, v3).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
    }
}