package com.otterly76.ott.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PoolAliasLookup.class})
public interface PoolAliasLookupMixin {
    @WrapOperation(
            method = {"create(Ljava/util/List;Lnet/minecraft/core/BlockPos;J)Lnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;"},
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"
            )
    )
    private static ImmutableMap<?, ?> ott$allowDuplicateEntries(ImmutableMap.Builder<?, ?> instance, Operation<ImmutableMap<?, ?>> original) {
        return instance.buildKeepingLast();
    }
}
