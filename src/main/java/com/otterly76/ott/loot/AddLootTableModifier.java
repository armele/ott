package com.otterly76.ott.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

/**
 * GLM modifier that rolls a secondary loot table and appends its results
 * to the current loot drop. Useful for injecting themed item pools into
 * many vanilla chest tables without duplicating entries.
 */
public class AddLootTableModifier extends LootModifier {

    public static final MapCodec<AddLootTableModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(
                    ResourceLocation.CODEC.fieldOf("loot_table").forGetter(m -> m.lootTableId)
            ).apply(inst, AddLootTableModifier::new)
    );

    private final ResourceLocation lootTableId;

    public AddLootTableModifier(LootItemCondition[] conditions, ResourceLocation lootTableId) {
        super(conditions);
        this.lootTableId = lootTableId;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot,
                                                          @NotNull LootContext context) {
        LootTable table = context.getLevel().getServer().reloadableRegistries()
                .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, lootTableId));
        LootParams params = new LootParams.Builder(context.getLevel())
                .create(LootContextParamSets.EMPTY);
        table.getRandomItems(params).forEach(generatedLoot::add);
        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}