package com.otterly76.ott.util.data;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;

import java.util.*;

public class BuiltInCoreRegistry<T> {
    private final Map<ResourceLocation, T> hardcodedEntries = new HashMap<>();
    private final Map<ResourceLocation, T> dataDrivenEntries = new HashMap<>();
    private final String modId;
    private final Registry<T> registry;
    protected boolean isPresent = false;

    public BuiltInCoreRegistry(Registry<T> registry, String modId) {
        this.modId = modId;
        this.registry = registry;
        this.registry.keySet().forEach((id) -> this.hardcodedEntries.putIfAbsent(id, this.registry.get(id)));
    }

    public void register(ResourceLocation name, T entry) {
        this.hardcodedEntries.put(name, entry);
    }

    public void register(String name, T entry) {
        this.register(ResourceLocation.fromNamespaceAndPath(this.modId, name), entry);
    }

    public void registerDataDriven(ResourceLocation name, T entry) {
        this.dataDrivenEntries.put(name, entry);
    }

    public <E extends T> ResourceKey<T> resource(String name, E entry) {
        this.register(name, entry);
        return ResourceKey.create(this.registry.key(), ResourceLocation.fromNamespaceAndPath(this.modId, name));
    }

    public T getOrDefault(ResourceLocation name, T fallback) {
        if (this.registry.containsKey(name)) {
            return this.registry.get(name);
        } else if (this.dataDrivenEntries.containsKey(name)) {
            return this.dataDrivenEntries.get(name);
        } else {
            return this.hardcodedEntries.getOrDefault(name, fallback);
        }
    }

    public T get(ResourceLocation name) {
        return this.dataDrivenEntries.containsKey(name) ? this.dataDrivenEntries.get(name) : this.hardcodedEntries.get(name);
    }

    public T get(ResourceKey<T> name) {
        return this.getOrDefault(name.location(), null);
    }

    public T getOrThrow(ResourceKey<T> key) {
        T value = this.get(key);
        if (value == null) {
            ResourceKey<?> resource = this.registry.key();
            throw new IllegalStateException("Missing key in " + resource + ":" + key);
        } else {
            return value;
        }
    }

    public ResourceLocation getKey(T value) {
        return this.findKeyIn(this.dataDrivenEntries, value)
                .or(() -> this.findKeyIn(this.hardcodedEntries, value))
                .orElseThrow(() -> new IllegalArgumentException("Value not found in registry: " + value));
    }

    private Optional<ResourceLocation> findKeyIn(Map<ResourceLocation, T> map, T value) {
        return map.entrySet().stream()
                .filter((entry) -> Objects.equals(entry.getValue(), value))
                .findFirst().map(Map.Entry::getKey);
    }

    public Optional<List<T>> fromTag(TagKey<T> tag) {
        List<T> values = this.getAllEntries().values().stream().filter((value) -> Holder.direct(value).is(tag)).toList();
        return values.isEmpty() ? Optional.empty() : Optional.of(values);
    }

    public Optional<T> getRandomFromTag(TagKey<T> tag, RandomSource random) {
        return this.fromTag(tag).map((values) -> this.getRandomElement(values, random));
    }

    public T getRandomElement(Collection<T> collection, RandomSource random) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Cannot get random element from empty collection");
        }
        List<T> list = collection instanceof List<T> l ? l : new ArrayList<>(collection);
        return list.get(random.nextInt(list.size()));
    }

    public T getRandomElement(RandomSource random) {
        Map<ResourceLocation, T> allEntries = this.getAllEntries();
        if (allEntries.isEmpty()) {
            throw new IllegalStateException("Registry is empty");
        } else {
            return this.getRandomElement(allEntries.values(), random);
        }
    }

    private Map<ResourceLocation, T> getAllEntries() {
        Map<ResourceLocation, T> combined = new HashMap<>();
        this.registry.entrySet().forEach((entry) -> combined.put(entry.getKey().location(), entry.getValue()));
        combined.putAll(this.hardcodedEntries);
        combined.putAll(this.dataDrivenEntries);
        return combined;
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(this.getAllEntries().values());
    }

    public Map<ResourceLocation, T> entries() {
        return Collections.unmodifiableMap(this.getAllEntries());
    }

    public void clearDataDrivenEntries() {
        this.dataDrivenEntries.clear();
    }

    public void register() {
        if (this.isPresent) {
            throw new IllegalStateException("Duplication of BuiltIn-Registry: " + this.registry);
        } else {
            this.isPresent = true;
        }
    }
}