package com.otterly76.ott.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ModBoatItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final Supplier<? extends EntityType<? extends Boat>> entityType;
    private Boat fakeBoat;

    public ModBoatItemRenderer(Supplier<? extends EntityType<? extends Boat>> entityType) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.entityType = entityType;
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (fakeBoat == null) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                fakeBoat = entityType.get().create(level);
            }
        }
        if (fakeBoat != null) {
            Minecraft.getInstance().getEntityRenderDispatcher()
                    .render(fakeBoat, 0.0, 0.0, 0.0, 0.0f, 1.0f, poseStack, buffer, packedLight);
        }
    }
}
