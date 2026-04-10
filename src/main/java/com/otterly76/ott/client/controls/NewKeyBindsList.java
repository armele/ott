package com.otterly76.ott.client.controls;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.CommonColors;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NewKeyBindsList extends CustomList {

    private final KeyBindsScreen controlsScreen;
    private final Minecraft mc;
    private int maxListLabelWidth;

    public NewKeyBindsList(KeyBindsScreen controls, Minecraft mcIn) {
        super(controls, mcIn);
        this.height -= 52;
        this.setY(48);
        this.controlsScreen = controls;
        this.mc = mcIn;
        children().clear();
        allEntries = new ArrayList<>();
        KeyMapping[] bindings = ArrayUtils.clone(mcIn.options.keyMappings);
        Arrays.sort(bindings);
        String lastCategory = null;

        for (KeyMapping keybinding : bindings) {
            String category = keybinding.getCategory();
            if (!category.equals(lastCategory)) {
                lastCategory = category;
                if (!category.endsWith(".hidden")) {
                    addEntry(new CategoryEntry(Component.translatable(category)));
                }
            }

            Component component = Component.translatable(keybinding.getName());
            int width = mcIn.font.width(component);
            if (width > this.maxListLabelWidth) {
                this.maxListLabelWidth = width;
            }
            if (!category.endsWith(".hidden")) {
                addEntry(new KeyEntry(keybinding, component));
            }
        }
    }

    @Override
    public int getBottom() {
        return this.controlsScreen.height - 56;
    }

    public class CategoryEntry extends Entry {

        private final Component name;
        private final int labelWidth;

        public CategoryEntry(Component name) {
            this.name = name;
            this.labelWidth = NewKeyBindsList.this.mc.font.width(this.name);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int slotIndex, int y, int x, int rowLeft, int rowWidth, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            guiGraphics.drawString(NewKeyBindsList.this.mc.font, this.name,
                    Objects.requireNonNull(minecraft.screen).width / 2 - this.labelWidth / 2,
                    y + rowWidth - 9 - 1, 16777215);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                public @NotNull NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;
                }
                public void updateNarration(@NotNull NarrationElementOutput neo) {
                    neo.add(NarratedElementType.TITLE, name);
                }
            });
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Nullable
        @Override
        public ComponentPath nextFocusPath(@NotNull FocusNavigationEvent event) {
            return null;
        }

        @Override
        protected void refreshEntry() {}

        public Component name() {
            return name;
        }
    }

    public class KeyEntry extends KeyBindsList.Entry implements IKeyEntry {

        private final KeyMapping key;
        private final Component keyDesc;
        private final Button btnChangeKeyBinding;
        private final Button btnResetKeyBinding;
        private boolean hasCollision;
        private final Component categoryName;

        public KeyEntry(KeyMapping key, Component keyDesc) {
            this.key = key;
            this.keyDesc = keyDesc;
            this.btnChangeKeyBinding = Button.builder(this.keyDesc, btn -> {
                        NewKeyBindsList.this.controlsScreen.selectedKey = key;
                        NewKeyBindsList.this.resetMappingAndUpdateButtons();
                    })
                    .bounds(0, 0, 75, 20)
                    .createNarration(supp -> key.isUnbound()
                            ? Component.translatable("narrator.controls.unbound", keyDesc)
                            : Component.translatable("narrator.controls.bound", keyDesc, supp.get()))
                    .build();

            this.btnResetKeyBinding = Button.builder(Component.translatable("controls.reset"), btn -> {
                        key.setToDefault();
                        minecraft.options.save();
                        NewKeyBindsList.this.resetMappingAndUpdateButtons();
                    })
                    .bounds(0, 0, 50, 20)
                    .createNarration(supp -> Component.translatable("narrator.controls.reset", keyDesc))
                    .build();

            this.categoryName = Component.translatable(this.key.getCategory());
            refreshEntry();
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int slotIndex, int y, int x, int rowLeft, int rowWidth, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            int resetKeyX = NewKeyBindsList.this.getScrollbarPosition() - this.btnResetKeyBinding.getWidth() - 10;
            this.btnResetKeyBinding.setX(resetKeyX);
            int top = y - 2;
            this.btnResetKeyBinding.setY(top);
            this.btnResetKeyBinding.render(guiGraphics, mouseX, mouseY, partialTicks);

            this.btnChangeKeyBinding.setX(resetKeyX - 5 - this.btnChangeKeyBinding.getWidth());
            this.btnChangeKeyBinding.setY(top);

            guiGraphics.drawString(NewKeyBindsList.this.mc.font, this.keyDesc, x, (y + rowWidth / 2) - (9 / 2), CommonColors.WHITE);

            if (this.hasCollision) {
                int markerWidth = 3;
                int minX = this.btnChangeKeyBinding.getX() - 6;
                guiGraphics.fill(minX, y + 2, minX + markerWidth, y + rowWidth + 2, CommonColors.RED);
            }
            this.btnChangeKeyBinding.render(guiGraphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public @NotNull List<GuiEventListener> children() {
            return ImmutableList.of(this.btnChangeKeyBinding, this.btnResetKeyBinding);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.btnChangeKeyBinding, this.btnResetKeyBinding);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
            return super.mouseClicked(mouseX, mouseY, buttonId);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int buttonId) {
            return super.mouseReleased(mouseX, mouseY, buttonId);
        }

        @Override
        public KeyMapping getKey() {
            return key;
        }

        @Override
        public Component getKeyDesc() {
            return keyDesc;
        }

        @Override
        public Component categoryName() {
            return categoryName;
        }

        public Button getBtnResetKeyBinding() {
            return btnResetKeyBinding;
        }

        public Button getBtnChangeKeyBinding() {
            return btnChangeKeyBinding;
        }

        @Override
        protected void refreshEntry() {
            this.btnChangeKeyBinding.setMessage(this.key.getTranslatedKeyMessage());
            this.btnResetKeyBinding.active = !this.key.isDefault();
            this.hasCollision = false;
            MutableComponent duplicates = Component.empty();
            if (!this.key.isUnbound()) {
                KeyMapping[] mappings = NewKeyBindsList.this.minecraft.options.keyMappings;
                for (KeyMapping mapping : mappings) {
                    if (mapping != this.key && (this.key.same(mapping) || this.key.hasKeyModifierConflict(mapping))) {
                        if (this.hasCollision) {
                            duplicates.append(", ");
                        }
                        this.hasCollision = true;
                        duplicates.append(mapping.getTranslatedKeyMessage());
                    }
                }
            }
            MutableComponent tooltip = Component.translatable(key.getCategory());
            if (this.hasCollision) {
                this.btnChangeKeyBinding.setMessage(Component.literal("[ ")
                        .append(this.btnChangeKeyBinding.getMessage().copy().withStyle(ChatFormatting.WHITE))
                        .append(" ]")
                        .withStyle(ChatFormatting.RED));
                tooltip.append(CommonComponents.NEW_LINE);
                tooltip.append(Component.translatable("controls.keybinds.duplicateKeybinds", duplicates));
            }
            this.btnChangeKeyBinding.setTooltip(Tooltip.create(tooltip));

            if (NewKeyBindsList.this.controlsScreen.selectedKey == this.key) {
                this.btnChangeKeyBinding.setMessage(Component.literal("> ")
                        .append(this.btnChangeKeyBinding.getMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
                        .append(" <")
                        .withStyle(ChatFormatting.YELLOW));
            }
        }
    }
}