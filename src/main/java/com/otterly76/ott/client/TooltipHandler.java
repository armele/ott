package com.otterly76.ott.client;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.Constants;
import com.otterly76.ott.client.tooltip.FoodTooltipComponent;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.util.item.FoodUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof BlockItem bi) {
            if (bi.getBlock().defaultBlockState().is(ModTags.Blocks.CTM_BLOCKS)) {
                event.getToolTip().add(Component.translatable("tooltip.ott.connecting")
                        .withStyle(ChatFormatting.GRAY));
            }
        }

        appendDurabilityTooltip(event);
    }

    private static void appendDurabilityTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();
        OttConfig.DurabilityTooltip cfg = OttConfig.DURABILITY_TOOLTIP;

        if (!cfg.ENABLED.get()) return;
        if (!stack.isDamageableItem()) return;
        if (!cfg.SHOW_WHEN_FULL.get() && !stack.isDamaged()) return;
        // Skip when advanced tooltip is active and the item is already damaged
        // (vanilla already shows durability in that case)
        if (event.getFlags().isAdvanced() && stack.isDamaged()) return;

        int max = stack.getMaxDamage();
        int remaining = max - stack.getDamageValue();

        ChatFormatting reactive = switch (cfg.COLOR_STYLE.get()) {
            case GOLD -> ChatFormatting.GOLD;
            case GRAY -> ChatFormatting.GRAY;
            case VARYING -> remaining >= 0.4f * max ? ChatFormatting.GREEN
                          : remaining >= 0.1f * max ? ChatFormatting.GOLD
                          : ChatFormatting.RED;
        };
        boolean hint = cfg.SHOW_HINT.get();

        Component line = switch (cfg.STYLE.get()) {
            case NUMBERS -> {
                MutableComponent value = remaining == max
                        ? Component.literal(String.valueOf(max)).withStyle(reactive)
                        : Component.literal(String.valueOf(remaining)).withStyle(reactive)
                          .append(Component.literal(" / " + max).withStyle(ChatFormatting.GRAY));
                yield hint
                        ? Component.translatable("tooltip.ott.durability.hint").withStyle(ChatFormatting.GRAY).append(value)
                        : value;
            }
            case BAR -> {
                int filled = Math.round(10f * remaining / max);
                MutableComponent bar = Component.literal("[").withStyle(ChatFormatting.GRAY);
                for (int i = 0; i < 10; i++)
                    bar.append(Component.literal(i < filled ? "█" : "▒").withStyle(reactive));
                bar.append(Component.literal("]").withStyle(ChatFormatting.GRAY));
                yield hint
                        ? Component.translatable("tooltip.ott.durability.hint").withStyle(ChatFormatting.GRAY).append(bar)
                        : bar;
            }
            case TEXT -> {
                String key = remaining == max                        ? "tooltip.ott.durability.pristine"
                           : remaining >= 0.4f * max ? "tooltip.ott.durability.slightly_damaged"
                           : remaining >= 0.1f * max ? "tooltip.ott.durability.severely_damaged"
                           : "tooltip.ott.durability.nearly_broken";
                Component text = Component.translatable(key).withStyle(reactive);
                yield hint
                        ? Component.translatable("tooltip.ott.durability.hint").withStyle(ChatFormatting.GRAY).append(text)
                        : text;
            }
        };
        event.getToolTip().add(line);
    }

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        FoodUtil.FoodValues values = FoodUtil.getFoodValues(event.getItemStack(), player);
        if (values != null) {
            event.getTooltipElements().add(Either.right(new FoodTooltipComponent(values.hunger(), values.saturation())));
        }
    }
}
