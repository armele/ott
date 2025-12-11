package com.otterly76.ott.events;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber(
        modid = "ott"
)
public class ModWanderingTraderTrades {
    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        addTrade(event, ((Block)ModBlocks.PALE_HANGING_MOSS.get()).asItem(), 1, 1, 8);
        addTrade(event, ((Block)ModBlocks.PALE_MOSS_BLOCK.get()).asItem(), 1, 2, 5);
        addTrade(event, ((Block)ModBlocks.OPEN_EYEBLOSSOM.get()).asItem(), 2, 1, 8);
        addTrade(event, ((Block)ModBlocks.PALE_OAK_SAPLING.get()).asItem(), 5, 1, 8);
    }

    private static void addTrade(WandererTradesEvent event, ItemLike item, int emeralds, int count, int maxUses) {
        event.getGenericTrades().add((VillagerTrades.ItemListing)(trader, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, emeralds), new ItemStack(item, count), maxUses, 1, 0.05F));
    }
}