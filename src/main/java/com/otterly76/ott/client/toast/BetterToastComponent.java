package com.otterly76.ott.client.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Deque;
import java.util.List;

public class BetterToastComponent extends ToastComponent {

    private static final Logger LOGGER = LogManager.getLogger("ott-toasts");

    public static final List<Class<?>> BLOCKED_CLASSES = new ArrayList<>();
    public static final List<BetterToastInstance<?>> tracker = new ArrayList<>();

    private final Deque<BetterToastInstance<?>> topDownList = new ArrayDeque<>();

    public BetterToastComponent() {
        super(Minecraft.getInstance());
        this.queued = new ControlledDeque();
        this.occupiedSlots = new BitSet(OttConfig.TOASTS.COUNT.get());
    }

    @Override
    public void render(@NotNull GuiGraphics gfx) {
        if (!this.minecraft.options.hideGui) {
            int width = gfx.guiWidth();
            this.visible.removeIf(inst -> {
                if (inst != null && inst.render(width, gfx)) {
                    this.occupiedSlots.clear(inst.index, inst.index + inst.slotCount);
                    this.topDownList.removeFirstOccurrence(inst);
                    return true;
                }
                return false;
            });

            if (!this.queued.isEmpty() && this.freeSlots() > 0) {
                this.queued.removeIf(toast -> {
                    int count = toast.slotCount();
                    int freeIdx = this.findFreeIndex(count);
                    if (freeIdx != -1) {
                        var inst = new BetterToastInstance<>(toast, freeIdx, count);
                        this.visible.add(inst);
                        this.occupiedSlots.set(freeIdx, freeIdx + count);
                        this.topDownList.forEach(t -> t.animationTime = -1L);
                        this.topDownList.addFirst(inst);
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.topDownList.clear();
    }

    @Override
    public int findFreeIndex(int slotCount) {
        if (this.freeSlots() >= slotCount) {
            int i = 0;
            for (int j = 0; j < OttConfig.TOASTS.COUNT.get(); ++j) {
                if (this.occupiedSlots.get(j)) {
                    i = 0;
                } else {
                    ++i;
                    if (i == slotCount) {
                        return j + 1 - i;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public int freeSlots() {
        return OttConfig.TOASTS.COUNT.get() - this.occupiedSlots.cardinality();
    }

    // --- Static lifecycle helpers ---

    public static void handleToastReloc() {
        ResourceLocation[] targets = {
                AdvancementToast.BACKGROUND_SPRITE,
                RecipeToast.BACKGROUND_SPRITE,
                SystemToast.BACKGROUND_SPRITE,
                TutorialToast.BACKGROUND_SPRITE
        };

        if (OttConfig.TOASTS.TRANSPARENT.getAsBoolean()) {
            for (ResourceLocation t : targets) {
                mutate(t, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "toast/transparent"));
            }
        } else if (OttConfig.TOASTS.TRANSLUCENT.getAsBoolean()) {
            String[] paths = {"advancement", "recipe", "system", "tutorial"};
            for (int i = 0; i < 4; i++) {
                mutate(targets[i], ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "toast/translucent/" + paths[i]));
            }
        } else {
            String[] paths = {"advancement", "recipe", "system", "tutorial"};
            for (int i = 0; i < 4; i++) {
                mutate(targets[i], ResourceLocation.withDefaultNamespace("toast/" + paths[i]));
            }
        }
    }

    public static void handleBlockedClasses() {
        BLOCKED_CLASSES.clear();
        for (String s : OttConfig.TOASTS.BLOCKED_CLASSES.get()) {
            try {
                BLOCKED_CLASSES.add(Class.forName(s));
            } catch (ClassNotFoundException e) {
                LOGGER.error("Invalid class string in toast config: {}", s);
            }
        }
    }

    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (Constants.MOD_ID.equals(event.getConfig().getModId())) {
            Minecraft.getInstance().execute(() -> {
                Minecraft.getInstance().toast = new BetterToastComponent();
                handleToastReloc();
                handleBlockedClasses();
            });
        }
    }

    private static void mutate(ResourceLocation target, ResourceLocation source) {
        ObfuscationReflectionHelper.setPrivateValue(ResourceLocation.class, target, source.getNamespace(), "namespace");
        ObfuscationReflectionHelper.setPrivateValue(ResourceLocation.class, target, source.getPath(), "path");
    }

    // --- Inner instance class ---

    public class BetterToastInstance<T extends Toast> extends ToastInstance<T> {

        protected int forcedShowTime = 0;

        protected BetterToastInstance(T toast, int index, int slotCount) {
            super(toast, index, slotCount);
            tracker.add(this);
        }

        public boolean tick() {
            return this.forcedShowTime++ > OttConfig.TOASTS.FORCE_TIME.get();
        }

        protected float getVisibility(long sysTime) {
            float f = Mth.clamp((sysTime - this.animationTime) / 600F, 0F, 1F);
            f = f * f;
            if (OttConfig.TOASTS.NO_SLIDE.get()) return 1;
            return this.forcedShowTime > OttConfig.TOASTS.FORCE_TIME.get() && this.visibility == Toast.Visibility.HIDE ? 1F - f : f;
        }

        @Override
        public boolean render(int scaledWidth, @NotNull GuiGraphics gfx) {
            long sysTime = Util.getMillis();
            int trueIdx = 0;

            if (OttConfig.TOASTS.TOP_DOWN.get()) {
                for (BetterToastInstance<?> inst : BetterToastComponent.this.topDownList) {
                    if (inst == this) break;
                    trueIdx++;
                }
            }

            if (this.animationTime == -1L) {
                this.animationTime = sysTime;
                this.visibility.playSound(BetterToastComponent.this.minecraft.getSoundManager());
            }

            if (this.visibility == Toast.Visibility.SHOW && this.getVisibility(sysTime) != 1) {
                this.visibleTime = sysTime;
            }

            PoseStack stack = gfx.pose();
            stack.pushPose();

            if (OttConfig.TOASTS.TOP_DOWN.get()) {
                int x = OttConfig.TOASTS.START_LEFT.get() ? 0 : scaledWidth - this.toast.width();
                stack.translate(x, (trueIdx - 1) * this.toast.height() + this.toast.height() * this.getVisibility(sysTime), 800 + this.index);
            } else if (OttConfig.TOASTS.START_LEFT.get()) {
                stack.translate(-this.toast.width() + this.toast.width() * this.getVisibility(sysTime), this.index * this.toast.height(), 800 + this.index);
            } else {
                stack.translate(scaledWidth - this.toast.width() * this.getVisibility(sysTime), this.index * this.toast.height(), 800 + this.index);
            }

            stack.translate(OttConfig.TOASTS.OFFSET_X.get(), OttConfig.TOASTS.OFFSET_Y.get(), 0);
            RenderSystem.enableBlend();
            Toast.Visibility visibility = Toast.Visibility.SHOW;
            if (this.animationTime != -1) {
                visibility = this.toast.render(gfx, BetterToastComponent.this, sysTime - this.visibleTime);
            }
            RenderSystem.disableBlend();
            stack.popPose();

            if (this.forcedShowTime > OttConfig.TOASTS.FORCE_TIME.get() && visibility != this.visibility) {
                this.animationTime = sysTime - (long) ((1 - this.getVisibility(sysTime)) * 600);
                this.visibility = visibility;
                this.visibility.playSound(BetterToastComponent.this.minecraft.getSoundManager());
            }

            return this.forcedShowTime > OttConfig.TOASTS.FORCE_TIME.get()
                    && this.visibility == Toast.Visibility.HIDE
                    && sysTime - this.animationTime > 600L;
        }
    }
}
