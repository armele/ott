package com.otterly76.ott.polymorph;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record CraftingRecipePair(ResourceLocation id, ItemStack output)
        implements Comparable<CraftingRecipePair> {

    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingRecipePair> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    CraftingRecipePair::id,
                    ItemStack.STREAM_CODEC,
                    CraftingRecipePair::output,
                    CraftingRecipePair::new);

    @Override
    public int compareTo(@NotNull CraftingRecipePair other) {
        ItemStack output1 = this.output();
        ItemStack output2 = other.output();
        DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        int compare = registry.getKey(output1.getItem()).compareTo(registry.getKey(output2.getItem()));

        if (compare == 0) {
            compare = output1.getCount() - output2.getCount();
            if (compare == 0) {
                DataComponentMap c1 = output1.getComponents();
                DataComponentMap c2 = output2.getComponents();
                if (Objects.equals(c1, c2)) return 0;
                return c1.hashCode() - c2.hashCode();
            }
            return compare;
        }
        // Sort modded before vanilla
        boolean thisVanilla = this.id().getNamespace().equals("minecraft");
        boolean otherVanilla = other.id().getNamespace().equals("minecraft");
        if (thisVanilla && !otherVanilla) return 1;
        if (!thisVanilla && otherVanilla) return -1;
        return compare;
    }
}
