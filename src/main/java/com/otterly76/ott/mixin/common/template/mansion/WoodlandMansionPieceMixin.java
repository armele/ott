package com.otterly76.ott.mixin.common.template.mansion;

import com.otterly76.ott.duck.RegistryHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WoodlandMansionPieces.WoodlandMansionPiece.class)
public abstract class WoodlandMansionPieceMixin implements RegistryHolder {
    @Unique
    private RegistryAccess ott$registries;

    @Override
    public RegistryAccess ott$getRegistries() {
        return this.ott$registries;
    }

    @Override
    public void ott$setRegistries(RegistryAccess registries) {
        this.ott$registries = registries;
    }
}