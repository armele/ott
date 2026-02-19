package com.otterly76.ott.client.gui;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class BundledTabs {
    private final Component tooltip;
    private final ItemStack icon;
    private final List<ItemStack> displayItems;
    private final @Nullable BiConsumer<HolderLookup.Provider, Output> provider;
    private boolean populated;
    private BundledTabSelector.@Nullable Tab tab;
    private boolean selected;

    private BundledTabs(Component tooltip, ItemStack icon, List<ItemStack> staticItems, @Nullable BiConsumer<HolderLookup.Provider, Output> provider) {
        this.tooltip = tooltip;
        this.icon = icon;
        this.displayItems = staticItems;
        this.provider = provider;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Component getTooltip() {
        return this.tooltip;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public List<ItemStack> getDisplayItems() {
        return Collections.unmodifiableList(this.displayItems);
    }

    public boolean contains(ItemStack stack) {
        return this.displayItems.stream().anyMatch(s -> ItemStack.isSameItemSameComponents(s, stack));
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setContentTab(BundledTabSelector.@Nullable Tab tab) {
        this.tab = tab;
    }

    public void setVisible(boolean visible) {
        if (this.tab != null) {
            this.tab.visible = visible;
        }
    }

    public void setY(int y) {
        if (this.tab != null) {
            this.tab.setY(y);
        }
    }

    public void populate(HolderLookup.Provider provider) {
        if (!this.populated && this.provider != null) {
            this.provider.accept(provider, new Output() {
                public void accept(ItemLike item) {
                    BundledTabs.this.displayItems.add(new ItemStack(item));
                }

                public void accept(ItemStack stack) {
                    BundledTabs.this.displayItems.add(stack);
                }
            });
            this.populated = true;
        }
    }

    public static class Builder {
        private Component title;
        private ItemStack icon;
        private BiConsumer<HolderLookup.Provider, Output> provider;

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder icon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public Builder displayItems(BiConsumer<HolderLookup.Provider, Output> provider) {
            this.provider = provider;
            return this;
        }

        public BundledTabs build() {
            if (this.title == null) {
                this.title = Component.empty();
            }

            if (this.icon == null) {
                this.icon = ItemStack.EMPTY;
            }

            return new BundledTabs(this.title, this.icon, new ArrayList<>(), this.provider);
        }
    }

    public interface Output {
        void accept(ItemLike var1);

        void accept(ItemStack var1);
    }
}
