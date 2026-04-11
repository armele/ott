package com.otterly76.ott.polymorph;

import com.otterly76.ott.network.polymorph.ClientboundCraftingRecipesPacket;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

public class CraftingRecipeManager {

    private static final Map<UUID, RecipeHolder<?>> SELECTED = new HashMap<>();
    private static final Map<UUID, CraftingRecipeCache> CACHES = new HashMap<>();
    // Per-player tick-based cache for crafting remainders (same tick, reuse selection)
    private static final Map<UUID, Integer> LAST_ACCESS_TICK = new HashMap<>();
    private static final Map<UUID, RecipeHolder<?>> CACHED_SELECTION = new HashMap<>();

    public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> resolveRecipe(
            AbstractContainerMenu menu, RecipeType<T> type, I input, Level level, Player player) {

        UUID uuid = player.getUUID();
        CraftingRecipeCache cache = CACHES.computeIfAbsent(uuid, k -> new CraftingRecipeCache(10));
        List<RecipeHolder<T>> recipes = cache.get(level, type, input);

        if (recipes.isEmpty()) {
            sendToPlayer(player, List.of(), null);
            return Optional.empty();
        }

        // Tick-based cached selection for crafting remainders workaround
        int tick = player.tickCount;
        Integer lastTick = LAST_ACCESS_TICK.get(uuid);
        if (lastTick != null && lastTick == tick) {
            RecipeHolder<?> cachedSel = CACHED_SELECTION.get(uuid);
            if (cachedSel != null) {
                SELECTED.put(uuid, cachedSel);
            }
        } else {
            CACHED_SELECTION.remove(uuid);
        }

        RegistryAccess registryAccess = level.registryAccess();
        RecipeHolder<T> first = null;
        RecipeHolder<T> selected = null;
        RecipeHolder<?> currentSelected = SELECTED.get(uuid);
        SortedSet<CraftingRecipePair> pairs = new TreeSet<>();

        for (RecipeHolder<T> holder : recipes) {
            T recipe = holder.value();
            ItemStack output = recipe.getResultItem(registryAccess);
            if (output.isEmpty() || recipe instanceof CustomRecipe) {
                output = recipe.assemble(input, registryAccess);
            }
            if (output.isEmpty()) continue;

            if (first == null) first = holder;
            boolean isSelected = selected == null && currentSelected != null
                    && currentSelected.id().equals(holder.id());
            if (isSelected) selected = holder;

            if (pairs.size() < 15 || isSelected) {
                pairs.add(new CraftingRecipePair(holder.id(), output));
            }
        }

        if (selected == null) {
            selected = first;
            SELECTED.put(uuid, selected);
        }

        ResourceLocation selectedId = selected != null ? selected.id() : null;
        sendToPlayer(player, new ArrayList<>(pairs), selectedId);

        // Update tick cache (only on server main thread)
        if (isServerThread(level)) {
            LAST_ACCESS_TICK.put(uuid, tick);
            CACHED_SELECTION.put(uuid, selected);
        }

        return Optional.ofNullable(selected);
    }

    private static boolean isServerThread(Level level) {
        var server = level.getServer();
        return server != null && server.getRunningThread() == Thread.currentThread();
    }

    private static void sendToPlayer(Player player, List<CraftingRecipePair> recipes,
                                     @Nullable ResourceLocation selected) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer,
                    new ClientboundCraftingRecipesPacket(recipes, Optional.ofNullable(selected)));
        }
    }

    public static void setSelected(UUID uuid, RecipeHolder<?> recipe) {
        CACHED_SELECTION.remove(uuid);
        SELECTED.put(uuid, recipe);
    }

    public static void clearPlayer(UUID uuid) {
        SELECTED.remove(uuid);
        CACHES.remove(uuid);
        LAST_ACCESS_TICK.remove(uuid);
        CACHED_SELECTION.remove(uuid);
    }
}
