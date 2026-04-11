package com.otterly76.ott.client.toast;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;

public class ControlledDeque extends ArrayDeque<Toast> {

    private static final Logger LOGGER = LogManager.getLogger("ott-toasts");

    public boolean isAllowed(Toast toast) {
        if (OttConfig.TOASTS.PRINT_CLASSES.get()) {
            LOGGER.info(toast.getClass());
        }

        if (OttConfig.TOASTS.BLOCK_ALL.get()) return false;
        if (OttConfig.TOASTS.BLOCK_GLOBAL_VANILLA.get() && isVanillaToast(toast)) return false;
        if (OttConfig.TOASTS.BLOCK_GLOBAL_MODDED.get() && !isVanillaToast(toast)) return false;
        if (BetterToastComponent.BLOCKED_CLASSES.contains(toast.getClass())) return false;

        return switch (toast) {
            case AdvancementToast ignored -> !OttConfig.TOASTS.BLOCK_ADVANCEMENTS.get();
            case RecipeToast ignored -> !OttConfig.TOASTS.BLOCK_RECIPES.get();
            case SystemToast ignored -> !OttConfig.TOASTS.BLOCK_SYSTEM.get();
            case TutorialToast ignored -> !OttConfig.TOASTS.BLOCK_TUTORIAL.get();
            default -> true;
        };
    }

    @Override
    public void addFirst(@NotNull Toast t) {
        if (isAllowed(t)) super.addFirst(t);
    }

    @Override
    public void addLast(@NotNull Toast t) {
        if (isAllowed(t)) super.addLast(t);
    }

    @Override
    public boolean add(@NotNull Toast t) {
        addLast(t);
        return isAllowed(t);
    }

    @Override
    public boolean offerFirst(@NotNull Toast t) {
        addFirst(t);
        return isAllowed(t);
    }

    @Override
    public boolean offerLast(@NotNull Toast t) {
        addLast(t);
        return isAllowed(t);
    }

    private boolean isVanillaToast(Toast toast) {
        return toast instanceof AdvancementToast || toast instanceof RecipeToast
                || toast instanceof SystemToast || toast instanceof TutorialToast;
    }
}
