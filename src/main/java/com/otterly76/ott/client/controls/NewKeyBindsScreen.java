package com.otterly76.ott.client.controls;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import com.otterly76.ott.mixin.client.KeyBindsScreenAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NewKeyBindsScreen extends KeyBindsScreen {

    private EditBox search;
    private DisplayMode displayMode;
    private SortOrder sortOrder = SortOrder.NONE;
    private Button buttonNone;
    private Button buttonConflicting;
    private Button buttonSort;
    private final DisplayableBoolean confirmingReset = new DisplayableBoolean(
            false,
            Component.translatable("options.confirmReset"),
            Component.translatable("controls.resetAll"));
    private boolean showFree;
    private Supplier<NewKeyBindsList> newKeyList;
    private Supplier<FreeKeysList> freeKeyList;

    public NewKeyBindsScreen(Screen screen, net.minecraft.client.Options settings) {
        super(screen, settings);
        this.layout.setHeaderHeight(48);
        this.layout.setFooterHeight(56);
    }

    @Override
    protected void init() {
        super.init();
        this.search.moveCursor(0, false);
    }

    @Override
    protected void addTitle() {
        int searchWidth = 340;
        int centerX = this.width / 2;
        this.search = new EditBox(font, centerX - searchWidth / 2, 20, searchWidth, Button.DEFAULT_HEIGHT,
                Component.translatable("selectWorld.search"));
        this.search.setResponder(this::filterKeys);
        this.search.setHint(Component.translatable("selectWorld.search"));

        LinearLayout header = this.layout.addToHeader(LinearLayout.vertical(),
                layoutSettings -> layoutSettings.paddingVertical(8));
        header.addChild(new StringWidget(this.title, this.font), LayoutSettings::alignHorizontallyCenter);
        header.addChild(this.search, layoutSettings -> layoutSettings.paddingVertical(4));
        setInitialFocus(this.search);
    }

    @Override
    protected void addContents() {
        this.newKeyList = Suppliers.memoize(() -> new NewKeyBindsList(this, this.minecraft));
        this.freeKeyList = Suppliers.memoize(() -> new FreeKeysList(this, this.minecraft));
        getAccess().ott$setKeyBindsList(showFree ? this.freeKeyList.get() : this.newKeyList.get());
        this.layout.addToContents(getKeyBindsList());
        displayMode = DisplayMode.ALL;
    }

    @Override
    protected void addFooter() {
        int btnWidth = Button.DEFAULT_WIDTH / 2 - 1;

        this.resetButton(Button.builder(confirmingReset.currentDisplay(), PRESS_RESET).build());
        resetButton().active = canReset();

        Button toggleFreeButton = Button.builder(Component.translatable("options.toggleFree"), PRESS_FREE)
                .size(btnWidth, Button.DEFAULT_HEIGHT).build();

        this.buttonSort = Button.builder(sortOrder.getDisplay(), PRESS_SORT)
                .size(btnWidth, Button.DEFAULT_HEIGHT).build();

        this.buttonNone = Button.builder(Component.translatable("options.showNone"), PRESS_NONE)
                .size(btnWidth, Button.DEFAULT_HEIGHT).build();

        this.buttonConflicting = Button.builder(Component.translatable("options.showConflicts"), PRESS_CONFLICTING)
                .size(btnWidth, Button.DEFAULT_HEIGHT).build();

        GridLayout grid = this.layout.addToFooter(new GridLayout());
        grid.rowSpacing(4);
        grid.columnSpacing(8);
        GridLayout.RowHelper rowHelper = grid.createRowHelper(2);

        LinearLayout topLeft = rowHelper.addChild(LinearLayout.horizontal());
        topLeft.spacing(4);
        topLeft.addChild(toggleFreeButton);
        topLeft.addChild(this.buttonSort);

        LinearLayout topRight = rowHelper.addChild(LinearLayout.horizontal());
        topRight.spacing(4);
        topRight.addChild(this.buttonNone);
        topRight.addChild(this.buttonConflicting);

        rowHelper.addChild(resetButton());
        rowHelper.addChild(Button.builder(CommonComponents.GUI_DONE, $$0x -> this.onClose()).build());
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        resetButton().active = canReset();
    }

    public Button resetButton() {
        return this.getAccess().ott$getResetButton();
    }

    public void resetButton(Button button) {
        this.getAccess().ott$setResetButton(button);
    }

    public void filterKeys() {
        filterKeys(search.getValue());
    }

    public void filterKeys(String lastSearch) {
        CustomList list = getCustomList();
        List<KeyBindsList.Entry> entries = buildFilteredList(list, lastSearch);
        if (list instanceof NewKeyBindsList) {
            sortOrder.sort(entries);
        }
        getKeyBindsList().children().clear();
        getKeyBindsList().setScrollAmount(0);
        getKeyBindsList().children().addAll(entries);
    }

    private List<KeyBindsList.Entry> buildFilteredList(CustomList list, String search) {
        String lowerSearch = search.toLowerCase(Locale.ROOT);
        Predicate<KeyBindsList.Entry> displayPredicate = (list instanceof NewKeyBindsList)
                ? displayMode.getPredicate() : e -> true;

        List<KeyBindsList.Entry> result = new ArrayList<>();
        NewKeyBindsList.CategoryEntry pendingCategory = null;

        for (KeyBindsList.Entry entry : list.getAllEntries()) {
            if (entry instanceof NewKeyBindsList.CategoryEntry cat) {
                pendingCategory = cat;
            } else {
                boolean textMatch = search.isEmpty() || entryMatchesSearch(entry, lowerSearch);
                boolean displayMatch = displayPredicate.test(entry);
                if (textMatch && displayMatch) {
                    if (pendingCategory != null) {
                        result.add(pendingCategory);
                        pendingCategory = null;
                    }
                    result.add(entry);
                }
            }
        }
        return result;
    }

    private boolean entryMatchesSearch(KeyBindsList.Entry entry, String lowerSearch) {
        if (entry instanceof IKeyEntry keyEntry) {
            return keyEntry.getKeyDesc().getString().toLowerCase(Locale.ROOT).contains(lowerSearch)
                    || keyEntry.categoryName().getString().toLowerCase(Locale.ROOT).contains(lowerSearch)
                    || keyEntry.getKey().getTranslatedKeyMessage().getString().toLowerCase(Locale.ROOT).contains(lowerSearch);
        }
        if (entry instanceof FreeKeysList.InputEntry inputEntry) {
            return inputEntry.getInput().getName().toLowerCase(Locale.ROOT).contains(lowerSearch);
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double xpos, double ypos, int buttonId) {
        boolean b = super.mouseClicked(xpos, ypos, buttonId);
        if (!b && search.isFocused()) {
            search.setFocused(false);
            clearFocus();
            b = true;
        }
        return b;
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        if (!search.isFocused() && this.selectedKey == null) {
            if (hasControlDown()) {
                if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_F)) {
                    search.setFocused(true);
                    return true;
                }
            }
        }
        if (search.isFocused()) {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                search.setFocused(false);
                return true;
            }
        }
        return super.keyPressed(key, scancode, mods);
    }

    private CustomList getCustomList() {
        if (this.getKeyBindsList() instanceof CustomList cl) {
            return cl;
        }
        throw new IllegalStateException("keyBindsList('%s') was not an instance of CustomList!".formatted(
                this.getKeyBindsList().getClass()));
    }

    public KeyBindsList getKeyBindsList() {
        return getAccess().ott$getKeyBindsList();
    }

    private void setKeyBindsList(KeyBindsList newList) {
        getAccess().ott$setKeyBindsList(newList);
        repositionElements();
    }

    private KeyBindsScreenAccessor getAccess() {
        return (KeyBindsScreenAccessor) this;
    }

    private boolean canReset() {
        for (KeyMapping key : this.options.keyMappings) {
            if (!key.isDefault()) {
                return true;
            }
        }
        return false;
    }

    private final Button.OnPress PRESS_RESET = btn -> {
        Minecraft minecraft = Objects.requireNonNull(NewKeyBindsScreen.this.minecraft);
        if (!confirmingReset.toggle()) {
            for (KeyMapping keybinding : minecraft.options.keyMappings) {
                keybinding.setToDefault();
            }
            minecraft.options.save();
            getKeyBindsList().resetMappingAndUpdateButtons();
        }
        btn.setMessage(confirmingReset.currentDisplay());
    };

    private final Button.OnPress PRESS_NONE = btn -> {
        if (displayMode == DisplayMode.NONE) {
            buttonNone.setMessage(Component.translatable("options.showNone"));
            displayMode = DisplayMode.ALL;
        } else {
            displayMode = DisplayMode.NONE;
            buttonNone.setMessage(Component.translatable("options.showAll"));
            buttonConflicting.setMessage(Component.translatable("options.showConflicts"));
        }
        filterKeys();
    };

    private final Button.OnPress PRESS_SORT = btn -> {
        sortOrder = sortOrder.cycle();
        btn.setMessage(sortOrder.getDisplay());
        filterKeys();
    };

    private final Button.OnPress PRESS_CONFLICTING = btn -> {
        if (displayMode == DisplayMode.CONFLICTING) {
            buttonConflicting.setMessage(Component.translatable("options.showConflicts"));
            displayMode = DisplayMode.ALL;
        } else {
            displayMode = DisplayMode.CONFLICTING;
            buttonConflicting.setMessage(Component.translatable("options.showAll"));
            buttonNone.setMessage(Component.translatable("options.showNone"));
        }
        filterKeys();
    };

    private final Button.OnPress PRESS_FREE = btn -> {
        removeWidget(getKeyBindsList());
        if (showFree) {
            buttonSort.active = true;
            buttonNone.active = true;
            buttonConflicting.active = true;
            resetButton().active = canReset();
            setKeyBindsList(newKeyList.get());
        } else {
            freeKeyList.get().recalculate();
            buttonSort.active = false;
            buttonNone.active = false;
            buttonConflicting.active = false;
            resetButton().active = false;
            setKeyBindsList(freeKeyList.get());
        }
        filterKeys();
        addRenderableWidget(getKeyBindsList());
        setFocused(getKeyBindsList());
        showFree = !showFree;
    };
}