package com.otterly76.ott.mixin.access;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeDecoratorType.class)
public interface TreeDecoratorTypeAccessor {
    @Invoker("<init>")
    static <T extends TreeDecorator> TreeDecoratorType<T> createTreeDecorator(MapCodec<T> codec) {
        throw new UnsupportedOperationException();
    }
}
