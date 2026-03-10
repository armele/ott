package com.otterly76.ott.item;

import com.otterly76.ott.util.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ModToolMaterials implements Tier {
    COPPER(ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 1.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)),
    EXPOSED_COPPER(ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 1.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)),
    WEATHERED_COPPER(ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 1.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)),
    OXIDIZED_COPPER(ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 1.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterials(TagKey<Block> incorrectBlocksForDrops, int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamageBonus;
    }

    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}