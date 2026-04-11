package com.otterly76.ott.client.neat;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

import java.text.DecimalFormat;
import java.util.*;

public class HealthBarRenderer {

    private static final TagKey<EntityType<?>> BOSS_TAG =
            TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", "bosses"));

    public static boolean isBoss(Entity entity) {
        return entity.getType().is(BOSS_TAG);
    }

    private static Entity getEntityLookedAt(Entity e) {
        Entity foundEntity = null;
        final double finalDistance = 32;
        HitResult pos = raycast(e);
        Vec3 positionVector = e.getEyePosition();

        double distance = pos.getLocation().distanceTo(positionVector);

        Vec3 lookVector = e.getLookAngle();
        Vec3 reachVector = positionVector.add(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance);

        List<Entity> entitiesInBoundingBox = e.level().getEntities(e,
                e.getBoundingBox().inflate(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance)
                        .expandTowards(1F, 1F, 1F));
        double minDistance = distance;

        for (Entity entity : entitiesInBoundingBox) {
            Entity lookedEntity = null;
            if (entity.isPickable()) {
                AABB collisionBox = entity.getBoundingBoxForCulling();
                Optional<Vec3> interceptPosition = collisionBox.clip(positionVector, reachVector);

                if (collisionBox.contains(positionVector)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if (interceptPosition.isPresent()) {
                    double distanceToEntity = positionVector.distanceTo(interceptPosition.get());

                    if (distanceToEntity < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = distanceToEntity;
                    }
                }
            }

            if (lookedEntity != null && minDistance < distance) {
                foundEntity = lookedEntity;
            }
        }

        return foundEntity;
    }

    private static HitResult raycast(Entity e) {
        Vec3 origin = e.getEyePosition();
        Vec3 ray = e.getLookAngle();
        Vec3 next = origin.add(ray.normalize().scale(32));
        return e.level().clip(new ClipContext(origin, next, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, e));
    }

    private static ItemStack getIcon(LivingEntity entity, boolean boss) {
        if (boss) {
            return new ItemStack(Items.NETHER_STAR);
        }
        EntityType<?> type = entity.getType();
        if (type.is(EntityTypeTags.ARTHROPOD)) {
            return new ItemStack(Items.SPIDER_EYE);
        } else if (type.is(EntityTypeTags.UNDEAD)) {
            return new ItemStack(Items.ROTTEN_FLESH);
        } else if (type.is(EntityTypeTags.ILLAGER)) {
            return new ItemStack(Items.IRON_AXE);
        } else {
            return ItemStack.EMPTY;
        }
    }

    private static int getColor(LivingEntity entity, boolean colorByType, boolean boss) {
        if (colorByType) {
            int r = 0;
            int g = 255;
            int b = 0;
            if (boss) {
                r = 128;
                g = 0;
                b = 128;
            } else if (!entity.getType().getCategory().isFriendly()) {
                r = 255;
                g = 0;
            }
            return 0xff000000 | r << 16 | g << 8 | b;
        } else {
            float health = Mth.clamp(entity.getHealth(), 0.0F, entity.getMaxHealth());
            float hue = Math.max(0.0F, (health / entity.getMaxHealth()) / 3.0F - 0.07F);
            return Mth.hsvToRgb(hue, 1.0F, 1.0F);
        }
    }

    private static boolean shouldShowPlate(LivingEntity living, Entity cameraEntity) {
        if (living == cameraEntity) {
            return false;
        }

        if ((!OttConfig.NEAT.RENDER_IN_F1.get() && !Minecraft.renderNames()) || !OttConfig.NEAT_DRAW) {
            return false;
        }

        var id = BuiltInRegistries.ENTITY_TYPE.getKey(living.getType());
        @SuppressWarnings("unchecked")
        List<String> blacklist = (List<String>) OttConfig.NEAT.BLACKLIST.get();
        if (blacklist.contains(id.toString())) {
            return false;
        }

        float distance = living.distanceTo(cameraEntity);
        if (distance > OttConfig.NEAT.MAX_DISTANCE.get()
                || (distance > OttConfig.NEAT.MAX_DISTANCE_WITHOUT_LOS.get()
                        && !living.hasLineOfSight(cameraEntity))) {
            return false;
        }
        if (!OttConfig.NEAT.SHOW_ON_BOSSES.get() && isBoss(living)) {
            return false;
        }
        if (!OttConfig.NEAT.SHOW_ON_PLAYERS.get() && living instanceof Player) {
            return false;
        }
        if (!OttConfig.NEAT.SHOW_FULL_HEALTH.get() && living.getHealth() >= living.getMaxHealth()) {
            return false;
        }
        if (OttConfig.NEAT.SHOW_ONLY_FOCUSED.get() && getEntityLookedAt(cameraEntity) != living) {
            return false;
        }
        if (!OttConfig.NEAT.SHOW_ON_PASSIVE.get() && living.getType().getCategory().isFriendly()) {
            return false;
        }
        if (!OttConfig.NEAT.SHOW_ON_HOSTILE.get() && (!living.getType().getCategory().isFriendly() && !isBoss(living))) {
            return false;
        }

        if (living.hasPassenger(cameraEntity)) {
            return false;
        }

        boolean visible = true;
        if (cameraEntity instanceof Player cameraPlayer && living.isInvisibleTo(cameraPlayer)) {
            boolean wearingThings = false;
            for (ItemStack armorSlot : living.getArmorSlots()) {
                if (!armorSlot.isEmpty()) {
                    wearingThings = true;
                    break;
                }
            }
            if (!wearingThings) {
                for (ItemStack handSlot : living.getHandSlots()) {
                    if (!handSlot.isEmpty()) {
                        wearingThings = true;
                        break;
                    }
                }
            }
            if (!wearingThings) {
                visible = false;
            }
        }
        Team livingTeam = living.getTeam();
        Team cameraTeam = cameraEntity.getTeam();
        if (livingTeam != null) {
            return switch (livingTeam.getNameTagVisibility()) {
                case ALWAYS -> visible;
                case NEVER -> false;
                case HIDE_FOR_OTHER_TEAMS -> cameraTeam == null ? visible : livingTeam.isAlliedTo(cameraTeam) && (livingTeam.canSeeFriendlyInvisibles() || visible);
                case HIDE_FOR_OWN_TEAM -> cameraTeam == null ? visible : !livingTeam.isAlliedTo(cameraTeam) && visible;
            };
        }

        return visible;
    }

    public static void hookRender(Entity entity, PoseStack poseStack, MultiBufferSource buffers,
            Camera camera, EntityRenderer<? super Entity> entityRenderer,
            float partialTicks, double x, double y, double z) {
        final Minecraft mc = Minecraft.getInstance();
        if (!(entity instanceof LivingEntity living)) {
            return;
        }
        if (!shouldShowPlate(living, camera.getEntity())) {
            return;
        }

        final int light = 0xF000F0;
        final float globalScale = 0.0267F;
        final float textScale = 0.5F;
        final int barHeight = OttConfig.NEAT.BAR_HEIGHT.get();
        final boolean boss = isBoss(living);
        final String name = living.hasCustomName() && living.getCustomName() != null
                ? ChatFormatting.ITALIC + living.getCustomName().getString()
                : living.getDisplayName().getString();
        final float nameLen = mc.font.width(name) * textScale;
        final float halfSize = Math.max(
                boss ? OttConfig.NEAT.PLATE_SIZE_BOSS.get() : OttConfig.NEAT.PLATE_SIZE.get(),
                nameLen / 2.0F + 10.0F);

        Vec3 vec3 = entityRenderer.getRenderOffset(entity, partialTicks);
        double d2 = x + vec3.x();
        double d3 = y + vec3.y();
        double d0 = z + vec3.z();

        Vec3 attachmentPoint = entity.getAttachments().get(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));

        poseStack.pushPose();
        poseStack.translate(d2, d3, d0);
        poseStack.translate(attachmentPoint.x, attachmentPoint.y + OttConfig.NEAT.HEIGHT_ABOVE.get(), attachmentPoint.z);
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.pushPose();
        poseStack.scale(-globalScale, -globalScale, globalScale);

        // Background
        if (OttConfig.NEAT.DRAW_BACKGROUND.get()) {
            float padding = OttConfig.NEAT.BACKGROUND_PADDING.get();
            int bgHeight = OttConfig.NEAT.BACKGROUND_HEIGHT.get();
            if (!OttConfig.NEAT.SHOW_ENTITY_NAME.get()) {
                bgHeight -= (int) 4F;
            }
            VertexConsumer builder = buffers.getBuffer(NeatRenderType.BAR_TEXTURE_TYPE);
            builder.addVertex(poseStack.last().pose(), -halfSize - padding, -bgHeight, 0.01F).setColor(0, 0, 0, 60).setUv(0.0F, 0.0F).setLight(light);
            builder.addVertex(poseStack.last().pose(), -halfSize - padding, barHeight + padding, 0.01F).setColor(0, 0, 0, 60).setUv(0.0F, 0.5F).setLight(light);
            builder.addVertex(poseStack.last().pose(), halfSize + padding, barHeight + padding, 0.01F).setColor(0, 0, 0, 60).setUv(1.0F, 0.5F).setLight(light);
            builder.addVertex(poseStack.last().pose(), halfSize + padding, -bgHeight, 0.01F).setColor(0, 0, 0, 60).setUv(1.0F, 0.0F).setLight(light);
        }

        // Health Bar
        {
            int argb = getColor(living, OttConfig.NEAT.COLOR_BY_TYPE.get(), boss);
            int r = (argb >> 16) & 0xFF;
            int g = (argb >> 8) & 0xFF;
            int b = argb & 0xFF;
            float maxHealth = Math.max(living.getHealth(), living.getMaxHealth());
            float healthHalfSize = halfSize * (living.getHealth() / maxHealth);

            VertexConsumer builder = buffers.getBuffer(NeatRenderType.BAR_TEXTURE_TYPE);
            builder.addVertex(poseStack.last().pose(), -halfSize, 0, 0.001F).setColor(r, g, b, 127).setUv(0.0F, 0.75F).setLight(light);
            builder.addVertex(poseStack.last().pose(), -halfSize, barHeight, 0.001F).setColor(r, g, b, 127).setUv(0.0F, 1.0F).setLight(light);
            builder.addVertex(poseStack.last().pose(), -halfSize + 2 * healthHalfSize, barHeight, 0.001F).setColor(r, g, b, 127).setUv(1.0F, 1.0F).setLight(light);
            builder.addVertex(poseStack.last().pose(), -halfSize + 2 * healthHalfSize, 0, 0.001F).setColor(r, g, b, 127).setUv(1.0F, 0.75F).setLight(light);

            if (healthHalfSize < halfSize) {
                builder.addVertex(poseStack.last().pose(), -halfSize + 2 * healthHalfSize, 0, 0.001F).setColor(0, 0, 0, 127).setUv(0.0F, 0.5F).setLight(light);
                builder.addVertex(poseStack.last().pose(), -halfSize + 2 * healthHalfSize, barHeight, 0.001F).setColor(0, 0, 0, 127).setUv(0.0F, 0.75F).setLight(light);
                builder.addVertex(poseStack.last().pose(), halfSize, barHeight, 0.001F).setColor(0, 0, 0, 127).setUv(1.0F, 0.75F).setLight(light);
                builder.addVertex(poseStack.last().pose(), halfSize, 0, 0.001F).setColor(0, 0, 0, 127).setUv(1.0F, 0.5F).setLight(light);
            }
        }

        // Text
        {
            final int textColor = HexFormat.fromHexDigits(OttConfig.NEAT.TEXT_COLOR.get());
            final int black = 0;

            if (OttConfig.NEAT.SHOW_ENTITY_NAME.get()) {
                poseStack.pushPose();
                poseStack.translate(-halfSize, -4.5F, 0F);
                poseStack.scale(textScale, textScale, textScale);
                mc.font.drawInBatch(name, 0, 0, textColor, false, poseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, black, light);
                poseStack.popPose();
            }

            {
                final float healthValueTextScale = 0.75F * textScale;
                poseStack.pushPose();
                poseStack.translate(-halfSize, -4.5F, 0F);
                poseStack.scale(healthValueTextScale, healthValueTextScale, healthValueTextScale);

                int h = OttConfig.NEAT.HP_TEXT_HEIGHT.get();
                DecimalFormat healthFormat = new DecimalFormat(OttConfig.NEAT.DECIMAL_FORMAT.get());

                if (OttConfig.NEAT.SHOW_CURRENT_HP.get()) {
                    String hpStr = healthFormat.format(living.getHealth());
                    mc.font.drawInBatch(hpStr, 2, h, textColor, false, poseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, black, light);
                }
                if (OttConfig.NEAT.SHOW_MAX_HP.get()) {
                    String maxHpStr = ChatFormatting.BOLD + healthFormat.format(living.getMaxHealth());
                    mc.font.drawInBatch(maxHpStr, (int) (halfSize / healthValueTextScale * 2) - mc.font.width(maxHpStr) - 2, h, textColor, false, poseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, black, light);
                }
                if (OttConfig.NEAT.SHOW_PERCENTAGE.get()) {
                    String percStr = (int) (100 * living.getHealth() / living.getMaxHealth()) + "%";
                    mc.font.drawInBatch(percStr, (int) (halfSize / healthValueTextScale) - mc.font.width(percStr) / 2.0F, h, textColor, false, poseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, black, light);
                }
                if (OttConfig.NEAT.ENABLE_DEBUG_INFO.get() && mc.getDebugOverlay().showDebugScreen()) {
                    var id = BuiltInRegistries.ENTITY_TYPE.getKey(living.getType());
                    mc.font.drawInBatch("ID: \"" + id + "\"", 0, h + 16, textColor, false, poseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, black, light);
                }
                poseStack.popPose();
            }
        }

        poseStack.popPose(); // remove globalScale

        // Icons
        {
            final float zBump = -0.1F;
            poseStack.pushPose();

            float iconOffset = 2.85F;
            float zShift = 0F;
            if (OttConfig.NEAT.SHOW_ATTRIBUTES.get()) {
                var icon = getIcon(living, boss);
                renderIcon(living.level(), icon, poseStack, buffers, halfSize, iconOffset, zShift);
                iconOffset += 5F;
                zShift += zBump;
            }

            int armor = living.getArmorValue();
            if (armor > 0 && OttConfig.NEAT.SHOW_ARMOR.get()) {
                int ironArmor = armor % 5;
                int diamondArmor = armor / 5;
                if (!OttConfig.NEAT.GROUP_ARMOR.get()) {
                    ironArmor = armor;
                    diamondArmor = 0;
                }

                var iron = new ItemStack(Items.IRON_CHESTPLATE);
                for (int i = 0; i < ironArmor; i++) {
                    renderIcon(living.level(), iron, poseStack, buffers, halfSize, iconOffset, zShift);
                    iconOffset += 1F;
                    zShift += zBump;
                }

                var diamond = new ItemStack(Items.DIAMOND_CHESTPLATE);
                for (int i = 0; i < diamondArmor; i++) {
                    renderIcon(living.level(), diamond, poseStack, buffers, halfSize, iconOffset, zShift);
                    iconOffset += 1F;
                    zShift += zBump;
                }
            }

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void renderIcon(Level level, ItemStack icon, PoseStack poseStack,
            MultiBufferSource buffers, float halfSize, float leftShift, float zShift) {
        if (!icon.isEmpty()) {
            final float globalScale = 0.0267F;
            final float iconScale = 0.12F;
            poseStack.pushPose();
            double dx = (halfSize - leftShift) * globalScale + OttConfig.NEAT.ICON_OFFSET_X.get();
            double dy = 3F * globalScale;
            double dz = zShift * globalScale;
            poseStack.translate(-dx, dy + OttConfig.NEAT.ICON_OFFSET_Y.get(), dz);
            poseStack.scale(iconScale, iconScale, iconScale);
            poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            Minecraft.getInstance().getItemRenderer()
                    .renderStatic(icon, ItemDisplayContext.NONE, 0xF000F0,
                            OverlayTexture.NO_OVERLAY, poseStack, buffers, level, 0);
            poseStack.popPose();
        }
    }
}
