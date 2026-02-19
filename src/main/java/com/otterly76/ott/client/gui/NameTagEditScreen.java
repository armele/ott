package com.otterly76.ott.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.otterly76.ott.Ott;
import com.otterly76.ott.client.gui.components.FormattableEditBox;
import com.otterly76.ott.client.gui.components.FormattingGuideWidget;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.network.C2SNameTagUpdateMessage;
import com.otterly76.ott.util.data.ComponentDecomposer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class NameTagEditScreen extends Screen {
    public static final String KEY_NAME_TAG_EDIT = "ott.name_tag.edit";
    private static final ResourceLocation EDIT_NAME_TAG_LOCATION = Ott.resource("textures/gui/edit_name_tag.png");
    private final int imageWidth = 176;
    private final int imageHeight = 48;
    private int leftPos;
    private int topPos;
    private final int titleLabelX = 60;
    private final int titleLabelY = 8;
    private final InteractionHand hand;
    private String itemName;
    private EditBox name;

    public NameTagEditScreen(InteractionHand hand, Component title) {
        super(Component.translatable("ott.name_tag.edit", Items.NAME_TAG.getDescription()));
        this.hand = hand;
        this.itemName = ComponentDecomposer.toFormattedString(title);
    }

    protected void init() {
        int var10001 = this.width;
        this.leftPos = (var10001 - 176) / 2;
        this.topPos = this.height / 4;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            PacketDistributor.sendToServer(new C2SNameTagUpdateMessage(this.hand, this.itemName));
            this.onClose();
        }).bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build());
        if (OttConfig.ANVILS.MISC.RENAMING_SUPPORTS_FORMATTING.get()) {
            this.name = new FormattableEditBox(this.font, this.leftPos + 62, this.topPos + 26, 103, 12, Component.translatable("container.repair"));
        } else {
            this.name = new EditBox(this.font, this.leftPos + 62, this.topPos + 26, 103, 12, Component.translatable("container.repair"));
        }

        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder((s) -> this.itemName = s);
        this.name.setValue(this.itemName);
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        int var10003 = this.leftPos;
        var10003 = var10003 + 176 - 7;
        int var10004 = this.topPos;
        this.addRenderableWidget(new FormattingGuideWidget(var10003, var10004 + 8, this.font));
    }

    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        String s = this.name.getValue();
        this.init(minecraft, width, height);
        this.name.setValue(s);
    }

    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        Font var10001 = this.font;
        int var10003 = this.leftPos;
        var10003 += 60;
        int var10004 = this.topPos;
        guiGraphics.drawString(var10001, this.title, var10003, var10004 + 8, 4210752, false);
        this.name.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int var10002 = this.leftPos;
        int var10003 = this.topPos;
        guiGraphics.blit(EDIT_NAME_TAG_LOCATION, var10002, var10003, 0, 0, 176, 48);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
        guiGraphics.renderItem(new ItemStack(Items.NAME_TAG), (this.leftPos + 17) / 2, (this.topPos + 8) / 2);
        guiGraphics.pose().popPose();
    }

    public boolean isPauseScreen() {
        return false;
    }
}
