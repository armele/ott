package com.otterly76.ott.worldgen.structure;

import com.google.common.collect.Lists;
import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.duck.StructurePoolAccess;
import com.otterly76.ott.worldgen.poolelement.DelegatingConfig;
import com.otterly76.ott.worldgen.poolelement.DelegatingPoolElement;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SequencedPriorityIterator;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AlternateJigsawGenerator {
    private static final TagKey<Structure> VILLAGE_TAG = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath("minecraft", "village"));
    private static final Map<Structure, Boolean> IS_VILLAGE_CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    public static Optional<Structure.GenerationStub> generate(Structure.GenerationContext context, Structure structure, AlternateJigsawConfig config, boolean vanilla, int size, BlockPos pos, PoolAliasLookup aliasLookup) {
        RegistryAccess registries = context.registryAccess();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        StructureTemplateManager structureTemplateManager = context.structureTemplateManager();
        LevelHeightAccessor heightLimitView = context.heightAccessor();
        WorldgenRandom random = context.random();
        Registry<StructureTemplatePool> registry = registries.registryOrThrow(Registries.TEMPLATE_POOL);
        Rotation rotation = config.fixedRotation() ? Rotation.NONE : Rotation.getRandom(random);
        StructurePoolElement startingElement = config.startPool().unwrapKey().flatMap((resourceKey) -> registry.getOptional(aliasLookup.lookup(resourceKey))).orElse(config.startPool().value()).getRandomTemplate(random);

        boolean isVillage = isVillage(registries, structure, config);
        if (startingElement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            Optional<ResourceLocation> startJigsawName = config.startJigsawName();
            BlockPos startPos;
            if (startJigsawName.isPresent()) {
                ResourceLocation identifier = startJigsawName.get();
                Optional<BlockPos> optional = findNamedJigsaw(startingElement, identifier, pos, rotation, structureTemplateManager, random);
                if (optional.isEmpty()) {
                    return Optional.empty();
                }
                startPos = optional.get();
            } else {
                startPos = pos;
            }

            Vec3i vec3i = startPos.subtract(pos);
            BlockPos blockPos2 = pos.subtract(vec3i);
            PoolElementStructurePiece piece = new PoolElementStructurePiece(structureTemplateManager, startingElement, blockPos2, startingElement.getGroundLevelDelta(), rotation, startingElement.getBoundingBox(structureTemplateManager, blockPos2, rotation), config.liquidSettings());
            BoundingBox blockBox = piece.getBoundingBox();

            if (isVillage) {
                if (blockBox.getXSpan() <= 1 && blockBox.getZSpan() <= 1) {
                    return Optional.empty();
                }
                Types heightmap = Types.MOTION_BLOCKING_NO_LEAVES;
                if (OttConfig.WORLDGEN.VILLAGE_STABILITY_ENABLED.get() && !isAnchorStable(context, blockBox, heightmap)) {
                    return Optional.empty();
                }
            }

            int originX = (blockBox.maxX() + blockBox.minX()) / 2;
            int originZ = (blockBox.maxZ() + blockBox.minZ()) / 2;

            int computedY = config.startProjection().flatMap((either) -> either.map(
                    (snap) -> snap.findY(new BlockPos(originX, blockPos2.getY(), originZ), context, heightLimitView, context.randomState()),
                    (type) -> Optional.of(pos.getY() + chunkGenerator.getFirstFreeHeight(originX, originZ, isVillage ? Types.MOTION_BLOCKING_NO_LEAVES : type, heightLimitView, context.randomState()))
            )).orElse(blockPos2.getY());

            int l = blockBox.minY() + piece.getGroundLevelDelta();
            piece.move(0, computedY - l, 0);

            if (pieceWithinPaddingBounds(heightLimitView, config.dimensionPadding(), piece.getBoundingBox())) {
                return Optional.empty();
            }

            int originY = computedY + vec3i.getY();
            return Optional.of(new Structure.GenerationStub(new BlockPos(originX, originY, originZ), (collector) -> {
                    List<PoolElementStructurePiece> list = Lists.newArrayList();
                    list.add(piece);
                    if (size > 0) {
                        AlternateJigsawConfig.MaxDistance maxDistance = config.maxDistanceFromCenter();
                        AABB box = new AABB(pos.getX() - maxDistance.horizontal(), Math.max(originY - maxDistance.vertical(), heightLimitView.getMinBuildHeight() + config.dimensionPadding().bottom()), pos.getZ() - maxDistance.horizontal(), pos.getX() + maxDistance.horizontal() + 1, Math.min(originY + maxDistance.vertical() + 1, heightLimitView.getMaxBuildHeight() - config.dimensionPadding().top()), pos.getZ() + maxDistance.horizontal() + 1);
                        BoxOctree boxOctree = new BoxOctree(box);
                        if (!getConfig(startingElement).otherPiecesCanIntersect()) {
                            getCollisionBox(blockBox).ifPresent(boxOctree::addBox);
                        }

                        generatePieces(context, structure, vanilla, size, config.useExpansionHack(), chunkGenerator, structureTemplateManager, heightLimitView, random, registry, piece, list, boxOctree, aliasLookup, config.liquidSettings(), isVillage ? Types.MOTION_BLOCKING_NO_LEAVES : config.startProjection().flatMap(e -> e.right()).orElse(Types.WORLD_SURFACE_WG), isVillage);
                    }
                    list.forEach(collector::addPiece);
                }));
        }
    }

    private static boolean pieceWithinPaddingBounds(LevelHeightAccessor levelHeightAccessor, DimensionPadding dimensionPadding, BoundingBox boundingBox) {
        if (dimensionPadding == DimensionPadding.ZERO) {
            return false;
        } else {
            int minY = levelHeightAccessor.getMinBuildHeight() + dimensionPadding.bottom();
            int maxY = levelHeightAccessor.getMaxBuildHeight() - dimensionPadding.top();
            return boundingBox.minY() < minY || boundingBox.maxY() > maxY;
        }
    }

    private static boolean isVillage(RegistryAccess registries, Structure structure, AlternateJigsawConfig config) {
        if (config.startPool().unwrapKey().map(k -> k.location().getPath().contains("village")).orElse(false)) {
            return true;
        }
        return IS_VILLAGE_CACHE.computeIfAbsent(structure, s -> {
            Registry<Structure> registry = registries.registryOrThrow(Registries.STRUCTURE);

            // 1. Unwrap DelegatingStructure to find the base structure
            Structure temp = s;
            while (temp instanceof DelegatingStructure delegating) {
                temp = delegating.delegate();
            }

            // 2. Check if the base structure is in the village tag
            Optional<ResourceKey<Structure>> key = registry.getResourceKey(temp);
            if (key.isPresent()) {
                if (registry.getHolder(key.get()).map(h -> h.is(VILLAGE_TAG)).orElse(false)) {
                    return true;
                }
            }

            // 3. Fallback: Check all holders (expensive, but only once per structure type)
            for (Holder.Reference<Structure> holder : registry.holders().toList()) {
                Structure value = holder.value();
                while (value instanceof DelegatingStructure delegating) {
                    value = delegating.delegate();
                }
                if (value == temp && holder.is(VILLAGE_TAG)) {
                    return true;
                }
            }

            return false;
        });
    }

    private static boolean isAnchorStable(Structure.GenerationContext context, BoundingBox box, Types heightmap) {
        ChunkGenerator generator = context.chunkGenerator();
        LevelHeightAccessor heightLimitView = context.heightAccessor();
        RandomState randomState = context.randomState();

        int x1 = box.minX();
        int z1 = box.minZ();
        int x2 = box.maxX();
        int z2 = box.maxZ();
        int xm = (x1 + x2) / 2;
        int zm = (z1 + z2) / 2;

        int h1 = generator.getFirstFreeHeight(x1, z1, heightmap, heightLimitView, randomState);
        int h2 = generator.getFirstFreeHeight(x2, z2, heightmap, heightLimitView, randomState);
        int h3 = generator.getFirstFreeHeight(x1, z2, heightmap, heightLimitView, randomState);
        int h4 = generator.getFirstFreeHeight(x2, z1, heightmap, heightLimitView, randomState);
        int hm = generator.getFirstFreeHeight(xm, zm, heightmap, heightLimitView, randomState);

        int minH = Math.min(Math.min(Math.min(h1, h2), Math.min(h3, h4)), hm);
        int maxH = Math.max(Math.max(Math.max(h1, h2), Math.max(h3, h4)), hm);

        return (maxH - minH) <= 128;
    }


    private static boolean isPieceStable(Structure.GenerationContext context, BoundingBox box, Types heightmap) {
        ChunkGenerator generator = context.chunkGenerator();
        LevelHeightAccessor heightLimitView = context.heightAccessor();
        RandomState randomState = context.randomState();

        int x1 = box.minX();
        int z1 = box.minZ();
        int x2 = box.maxX();
        int z2 = box.maxZ();
        int xm = (x1 + x2) / 2;
        int zm = (z1 + z2) / 2;

        int[] xs = {x1, x2, x1, x2, xm, xm, x1, x2, xm};
        int[] zs = {z1, z2, z2, z1, z1, z2, zm, zm, zm};

        int minH = Integer.MAX_VALUE;
        int maxH = Integer.MIN_VALUE;
        int pieceY = box.minY();
        int stableCount = 0;

        for (int i = 0; i < 9; i++) {
            int h = generator.getFirstFreeHeight(xs[i], zs[i], heightmap, heightLimitView, randomState);
            minH = Math.min(minH, h);
            maxH = Math.max(maxH, h);
            if (pieceY >= h - 256 && pieceY <= h + 128) {
                stableCount++;
            }
        }

        if (stableCount < 1) return false;

        return (maxH - minH) <= 256;
    }

    private static Optional<AABB> getCollisionBox(BoundingBox box) {
        AABB aabb = AABB.of(box).inflate(-1.0);
        if (aabb.minX >= aabb.maxX || aabb.minY >= aabb.maxY || aabb.minZ >= aabb.maxZ) {
            return Optional.empty();
        }
        return Optional.of(aabb);
    }

    private static Optional<BlockPos> findNamedJigsaw(StructurePoolElement pool, ResourceLocation id, BlockPos pos, Rotation rotation, StructureTemplateManager structureManager, WorldgenRandom random) {
        List<StructureTemplate.StructureBlockInfo> list = pool.getShuffledJigsawBlocks(structureManager, pos, rotation, random);
        for (StructureTemplate.StructureBlockInfo structureBlockInfo : list) {
            if (structureBlockInfo.nbt() != null) {
                ResourceLocation identifier = ResourceLocation.tryParse(structureBlockInfo.nbt().getString("name"));
                if (id.equals(identifier)) {
                    return Optional.of(structureBlockInfo.pos());
                }
            }
        }
        return Optional.empty();
    }

    private static void generatePieces(Structure.GenerationContext context, Structure structure, boolean vanilla, int maxSize, boolean useExpansionHack, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, LevelHeightAccessor heightLimitView, RandomSource random, Registry<StructureTemplatePool> structurePoolRegistry, PoolElementStructurePiece firstPiece, List<PoolElementStructurePiece> pieces, BoxOctree boxOctree, PoolAliasLookup aliasLookup, LiquidSettings liquidSettings, Types heightmap, boolean isVillage) {
        StructurePoolGenerator generator = new StructurePoolGenerator(context, structure, vanilla, structurePoolRegistry, maxSize, chunkGenerator, structureTemplateManager, pieces, random, heightmap, isVillage);
        generator.generatePiece(firstPiece, boxOctree, 0, useExpansionHack, heightLimitView, aliasLookup, liquidSettings);

        while (generator.pieces.hasNext()) {
            PieceState pieceState = generator.pieces.next();
            generator.generatePiece(pieceState.piece, pieceState.octree, pieceState.currentSize, useExpansionHack, heightLimitView, aliasLookup, liquidSettings);
        }
    }

    private static DelegatingConfig getConfig(StructurePoolElement element) {
        if (element instanceof DelegatingPoolElement delegating) {
            return delegating.config();
        }
        return new DelegatingConfig(element);
    }

    static final class StructurePoolGenerator {
        private final Structure.GenerationContext context;
        private final boolean isVillage;
        private final boolean vanilla;
        private final Registry<StructureTemplatePool> registry;
        private final int maxSize;
        private final ChunkGenerator chunkGenerator;
        private final StructureTemplateManager structureTemplateManager;
        private final List<? super PoolElementStructurePiece> piecesToPlace;
        private final RandomSource random;
        private final Types heightmap;
        private final Map<ResourceLocation, Integer> groupCounts = new HashMap<>();
        final SequencedPriorityIterator<PieceState> pieces = new SequencedPriorityIterator<>();

        private StructurePoolGenerator(Structure.GenerationContext context, Structure structure, boolean vanilla, Registry<StructureTemplatePool> registry, int maxSize, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, List<? super PoolElementStructurePiece> children, RandomSource random, Types heightmap, boolean isVillage) {
            this.context = context;
            this.isVillage = isVillage;
            this.vanilla = vanilla;
            this.registry = registry;
            this.maxSize = maxSize;
            this.chunkGenerator = chunkGenerator;
            this.structureTemplateManager = structureTemplateManager;
            this.piecesToPlace = children;
            this.random = random;
            this.heightmap = heightmap;
        }

        private void generatePiece(PoolElementStructurePiece parentPiece, BoxOctree parentOctree, int depth, boolean useExpansionHack, LevelHeightAccessor world, PoolAliasLookup aliasLookup, LiquidSettings liquidSettings) {
            StructurePoolElement anchorElement = parentPiece.getElement();

            for (StructureTemplate.StructureBlockInfo anchorJigsawInfo : anchorElement.getShuffledJigsawBlocks(this.structureTemplateManager, parentPiece.getPosition(), parentPiece.getRotation(), this.random)) {
                Holder<StructureTemplatePool> poolEntry = this.getTemplatePoolHolder(getTemplatePoolKey(anchorJigsawInfo, aliasLookup));
                if (poolEntry != null) {
                    MutableObject<List<ResourceKey<StructureTemplatePool>>> checkedPools = new MutableObject<>(new ArrayList<>());
                    this.findAndTestChildCandidates(poolEntry, checkedPools, parentPiece, anchorJigsawInfo, parentOctree, -1, depth, useExpansionHack, world, true, aliasLookup, liquidSettings);
                }
            }
        }

        private void findAndTestChildCandidates(Holder<StructureTemplatePool> entry, MutableObject<List<ResourceKey<StructureTemplatePool>>> checkedPools, PoolElementStructurePiece parentPiece, StructureTemplate.StructureBlockInfo anchorJigsawInfo, BoxOctree octree, int k, int depth, boolean useExpansionHack, LevelHeightAccessor world, boolean firstIteration, PoolAliasLookup aliasLookup, LiquidSettings liquidSettings) {
            List<StructurePoolElement> childCandidates = this.getPoolElements(entry.unwrapKey().orElse(Pools.EMPTY), checkedPools, depth, firstIteration);
            if (!childCandidates.isEmpty()) {
                boolean foundChild = this.findValidChildPiece(childCandidates, parentPiece, anchorJigsawInfo, octree, k, depth, useExpansionHack, world, aliasLookup, liquidSettings);
                if (!foundChild) {
                    this.findAndTestChildCandidates(entry.value().getFallback(), checkedPools, parentPiece, anchorJigsawInfo, octree, k, depth, useExpansionHack, world, false, aliasLookup, liquidSettings);
                }
            } else if (firstIteration) {
                this.findAndTestChildCandidates(entry.value().getFallback(), checkedPools, parentPiece, anchorJigsawInfo, octree, k, depth, useExpansionHack, world, false, aliasLookup, liquidSettings);
            }
        }

        private List<StructurePoolElement> getPoolElements(ResourceKey<StructureTemplatePool> poolKey, MutableObject<List<ResourceKey<StructureTemplatePool>>> checkedPools, int depth, boolean firstIteration) {
            if (poolKey == Pools.EMPTY) {
                return List.of();
            } else if (!ConfigHandler.getConfig().breaksSeedParity() && this.vanilla) {
                if (!firstIteration) {
                    return List.of();
                } else {
                    Holder<StructureTemplatePool> pool = this.registry.getHolder(poolKey).orElseThrow();
                    Holder<StructureTemplatePool> fallback = pool.value().getFallback();
                    List<StructurePoolElement> elements = new ArrayList<>();
                    if (depth != this.maxSize) {
                        elements.addAll(pool.value().getShuffledTemplates(this.random));
                    }
                    elements.addAll(fallback.value().getShuffledTemplates(this.random));
                    return elements;
                }
            } else if (!checkedPools.getValue().contains(poolKey)) {
                checkedPools.getValue().add(poolKey);
                Holder<StructureTemplatePool> pool = this.registry.getHolder(poolKey).orElseThrow();
                if (depth == this.maxSize && firstIteration) {
                    pool = pool.value().getFallback();
                }
                return ((StructurePoolAccess) pool.value()).ott$getTemplates().shuffle(this.random);
            } else {
                return List.of();
            }
        }

        private boolean findValidChildPiece(List<StructurePoolElement> elements, PoolElementStructurePiece parentPiece, StructureTemplate.StructureBlockInfo anchorJigsawInfo, BoxOctree octree, int k, int depth, boolean useExpansionHack, LevelHeightAccessor world, PoolAliasLookup aliasLookup, LiquidSettings liquidSettings) {
            BlockPos anchorPos = anchorJigsawInfo.pos();
            BlockPos candidateConnectorPos = anchorPos.relative(JigsawBlock.getFrontFacing(anchorJigsawInfo.state()));
            int parentMinY = parentPiece.getBoundingBox().minY();
            int anchorDistanceToFloor = anchorPos.getY() - parentMinY;
            StructureTemplatePool.Projection parentProjection = parentPiece.getElement().getProjection();
            boolean parentRigid = parentProjection == Projection.RIGID;
            Iterator<StructurePoolElement> var17 = elements.iterator();

            while (true) {
                StructurePoolElement element;
                DelegatingConfig config;
                boolean isDelegating;
                do {
                    if (!var17.hasNext()) {
                        return false;
                    }

                    element = var17.next();
                    if (element == EmptyPoolElement.INSTANCE) {
                        return true;
                    }

                    isDelegating = false;
                    if (element instanceof DelegatingPoolElement delegating) {
                        config = delegating.config();
                        isDelegating = true;
                    } else {
                        config = new DelegatingConfig(element);
                    }
                } while (config.shouldCancelPlacement(this.context, candidateConnectorPos, depth, this.groupCounts.getOrDefault(config.getName(), 0)));

                for (Rotation rotation : Rotation.getShuffled(this.random)) {
                    List<StructureTemplate.StructureBlockInfo> connectorJigsaws = element.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, rotation, this.random);
                    BoundingBox connectorBoundingBox = element.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, rotation);
                    int l;
                    if (useExpansionHack && connectorBoundingBox.getYSpan() <= 16) {
                        l = connectorJigsaws.stream().mapToInt((blockInfo) -> {
                            if (!connectorBoundingBox.isInside(blockInfo.pos().relative(JigsawBlock.getFrontFacing(blockInfo.state())))) {
                                return 0;
                            } else {
                                ResourceKey<StructureTemplatePool> registryKey2 = getTemplatePoolKey(blockInfo, aliasLookup);
                                Optional<? extends Holder<StructureTemplatePool>> optional1 = this.registry.getHolder(registryKey2);
                                Optional<Holder<StructureTemplatePool>> optional2 = optional1.map((entry) -> entry.value().getFallback());
                                int i2 = optional1.map((entry) -> entry.value().getMaxSize(this.structureTemplateManager)).orElse(0);
                                int j2 = optional2.map((entry) -> entry.value().getMaxSize(this.structureTemplateManager)).orElse(0);
                                return Math.max(i2, j2);
                            }
                        }).max().orElse(0);
                    } else {
                        l = 0;
                    }

                    for (StructureTemplate.StructureBlockInfo connectorJigsawInfo : connectorJigsaws) {
                        if (JigsawBlock.canAttach(anchorJigsawInfo, connectorJigsawInfo)) {
                            BlockPos connectorPos = connectorJigsawInfo.pos();
                            BlockPos blockPos5 = candidateConnectorPos.subtract(connectorPos);
                            BoundingBox blockBox3 = element.getBoundingBox(this.structureTemplateManager, blockPos5, rotation);
                            int m = blockBox3.minY();
                            StructureTemplatePool.Projection connectorProjection = element.getProjection();
                            boolean connectorProjectionRigid = connectorProjection == Projection.RIGID;
                            int connectorY = connectorPos.getY();
                            int o = anchorDistanceToFloor - connectorY + JigsawBlock.getFrontFacing(anchorJigsawInfo.state()).getStepY();
                            int p;
                            if (parentRigid && connectorProjectionRigid) {
                                p = parentMinY + o;
                            } else {
                                if (k == -1) {
                                    k = this.chunkGenerator.getFirstFreeHeight(anchorPos.getX(), anchorPos.getZ(), this.heightmap, world, this.context.randomState());
                                }
                                p = k - connectorY;
                            }

                            int q = p - m;
                            BoundingBox blockBox4 = blockBox3.moved(0, q, 0);
                            BlockPos blockPos6 = blockPos5.offset(0, q, 0);
                            if (l > 0) {
                                int r = Math.max(l + 1, blockBox4.maxY() - blockBox4.minY());
                                blockBox4 = new BoundingBox(
                                        blockBox4.minX(), blockBox4.minY(), blockBox4.minZ(),
                                        blockBox4.maxX(), Math.max(blockBox4.maxY(), blockBox4.minY() + r), blockBox4.maxZ()
                                );
                            }

                            boolean allowed;
                            if (!OttConfig.WORLDGEN.JIGSAW_PLACEMENT_RESTRICTIONS_ENABLED.get()) {
                                allowed = true;
                            } else {
                                allowed = config.allowBoundingBoxCollisions() || octree.withinBoundsButNotIntersectingChildren(AABB.of(blockBox4));
                            }

                            if (allowed) {
                                if (this.isVillage && OttConfig.WORLDGEN.VILLAGE_STABILITY_ENABLED.get() && !isPieceStable(this.context, blockBox4, this.heightmap)) {
                                    continue;
                                }
                                if (isDelegating) {
                                    this.groupCounts.put(config.getName(), this.groupCounts.getOrDefault(config.getName(), 0) + 1);
                                }

                                if (!config.otherPiecesCanIntersect()) {
                                    getCollisionBox(blockBox4).ifPresent(octree::addBox);
                                }

                                int r = parentPiece.getGroundLevelDelta();
                                int s;
                                if (connectorProjectionRigid) {
                                    s = r - o;
                                } else {
                                    s = element.getGroundLevelDelta();
                                }

                                PoolElementStructurePiece poolStructurePiece = new PoolElementStructurePiece(this.structureTemplateManager, element, blockPos6, s, rotation, blockBox4, liquidSettings);
                                int t;
                                if (parentRigid) {
                                    t = parentMinY + anchorDistanceToFloor;
                                } else if (connectorProjectionRigid) {
                                    t = p + connectorY;
                                } else {
                                    if (k == -1) {
                                        k = this.chunkGenerator.getFirstFreeHeight(anchorPos.getX(), anchorPos.getZ(), this.heightmap, world, this.context.randomState());
                                    }
                                    t = k + o / 2;
                                }

                                parentPiece.addJunction(new JigsawJunction(candidateConnectorPos.getX(), t - anchorDistanceToFloor + r, candidateConnectorPos.getZ(), o, connectorProjection));
                                poolStructurePiece.addJunction(new JigsawJunction(anchorPos.getX(), t - connectorY + s, anchorPos.getZ(), -o, parentProjection));
                                this.piecesToPlace.add(poolStructurePiece);
                                if (depth + 1 <= this.maxSize) {
                                    int priority = anchorJigsawInfo.nbt() != null ? anchorJigsawInfo.nbt().getInt("placement_priority") : 0;
                                    this.pieces.add(new PieceState(poolStructurePiece, octree, depth + 1), priority);
                                }

                                return true;
                            }
                        }
                    }
                }
            }
        }

        private @Nullable Holder<StructureTemplatePool> getTemplatePoolHolder(ResourceKey<StructureTemplatePool> key) {
            Optional<? extends Holder<StructureTemplatePool>> optional = this.registry.getHolder(key);
            if (optional.isPresent()) {
                Holder<StructureTemplatePool> regularPool = optional.get();
                if (!((StructurePoolAccess)regularPool.value()).ott$getTemplates().isEmpty()) {
                    return regularPool;
                }
            }
            return null;
        }

        private static ResourceKey<StructureTemplatePool> getTemplatePoolKey(StructureTemplate.StructureBlockInfo info, PoolAliasLookup aliasLookup) {
            CompoundTag compoundTag = Objects.requireNonNull(info.nbt(), () -> info + " nbt was null");
            ResourceKey<StructureTemplatePool> resourceKey = Pools.parseKey(compoundTag.getString("pool"));
            return aliasLookup.lookup(resourceKey);
        }
    }

    private record PieceState(PoolElementStructurePiece piece, BoxOctree octree, int currentSize) {
    }
}