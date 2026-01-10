package com.otterly76.ott.worldgen.poolelement.legacy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.poolelement.DelegatingPoolElement;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GuaranteedPoolElement extends DelegatingPoolElement {
    public static final MapCodec<GuaranteedPoolElement> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(StructurePoolElement.CODEC.fieldOf("delegate").forGetter(DelegatingPoolElement::delegate), ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("min_depth").forGetter(DelegatingPoolElement::minDepth), ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(GuaranteedPoolElement::count)).apply(instance, GuaranteedPoolElement::new));
    public static final StructurePoolElementType<GuaranteedPoolElement> TYPE = () -> CODEC;
    private final int count;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public GuaranteedPoolElement(StructurePoolElement delegate, Optional<Integer> minDepth, int count) {
        super(delegate, minDepth, Optional.of(count), Optional.empty());
        this.count = count;
    }

    private int count() {
        return this.count;
    }

    public @NotNull StructurePoolElementType<?> getType() {
        return TYPE;
    }
}