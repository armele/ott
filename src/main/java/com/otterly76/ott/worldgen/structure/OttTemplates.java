package com.otterly76.ott.worldgen.structure;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.otterly76.ott.worldgen.poolelement.DelegatingPoolElement;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class OttTemplates implements Iterable<StructurePoolElement> {
    protected final List<WeightedEntry> entries = Lists.newArrayList();

    public OttTemplates add(StructurePoolElement element, int weight) {
        this.entries.add(new WeightedEntry(element, this.entries.size(), weight));
        return this;
    }

    public List<StructurePoolElement> shuffle(RandomSource random) {
        List<WeightedEntry> shuffled = Lists.newArrayList(this.entries.stream().map(WeightedEntry::copy).toList());
        shuffled.forEach((entry) -> entry.setRandom(random.nextFloat()));
        shuffled.sort(Comparator.comparingDouble(WeightedEntry::getRandWeight));
        return shuffled.stream().map(WeightedEntry::getElement).toList();
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public Stream<StructurePoolElement> stream() {
        return this.entries.stream().map(WeightedEntry::getElement);
    }

    public @NotNull Iterator<StructurePoolElement> iterator() {
        return Iterators.transform(this.entries.iterator(), WeightedEntry::getElement);
    }

    public static class WeightedEntry {
        final StructurePoolElement element;
        final int index;
        final int weight;
        private double randWeight;
        private final boolean prioritized;

        WeightedEntry(StructurePoolElement element, int index, int weight) {
            this.element = element;
            this.index = index;
            this.weight = weight;

            if (element instanceof DelegatingPoolElement delegating) {
                this.prioritized = delegating.prioritized();
            } else {
                this.prioritized = false;
            }
        }

        private WeightedEntry copy() {
            return new WeightedEntry(this.element, this.index, this.weight);
        }

        private double getRandWeight() {
            return this.randWeight;
        }

        void setRandom(float value) {
            this.randWeight = -Math.pow(value, 1.0F / (float)this.weight) + (double)(this.prioritized ? -2 : 0);
        }

        public StructurePoolElement getElement() {
            return this.element;
        }

        public int getIndex() {
            return this.index;
        }

        public String toString() {
            return this.weight + ":" + this.element;
        }
    }
}