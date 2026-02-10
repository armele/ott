package com.otterly76.ott.mixin.common;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin({Holder.Reference.class})
public interface HolderReferenceAccessor<T> {
    @Accessor("value")
    void setValue(T object);

    @Invoker("bindTags")
    void callBindTags(Collection<TagKey<T>> tags);
}
