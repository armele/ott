package com.otterly76.ott.handler;

import com.ldtteam.structurize.api.RotationMirror;
import com.ldtteam.structurize.blueprints.v1.Blueprint;
import com.ldtteam.structurize.storage.ISurvivalBlueprintHandler;
import com.minecolonies.api.IMinecoloniesAPI;
import com.otterly76.ott.worldgen.WorldTemplateHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

public class DimensionAwareSurvivalHandler implements ISurvivalBlueprintHandler {
    private final ISurvivalBlueprintHandler original;

    public DimensionAwareSurvivalHandler(ISurvivalBlueprintHandler original) {
        this.original = original;
    }

    @Override
    public String getId() {
        return original.getId();
    }

    @Override
    public Component getDisplayName() {
        return original.getDisplayName();
    }

    @Override
    public boolean canHandle(Blueprint blueprint, ClientLevel level, Player player, BlockPos pos, RotationMirror rotationMirror) {
        if (level != null && level.dimension().equals(WorldTemplateHandler.TARGET_DIMENSION)) {
            return true;
        }
        return original.canHandle(blueprint, level, player, pos, rotationMirror);
    }

    @Override
    public void handle(Blueprint blueprint, String blueprintName, String packName, boolean isOwn, Level level, Player player, BlockPos pos, RotationMirror rotationMirror) {
        if (level.dimension().equals(WorldTemplateHandler.TARGET_DIMENSION)) {
            ModConfigSpec.BooleanValue blueprintBuildMode = IMinecoloniesAPI.getInstance().getConfig().getServer().blueprintBuildMode;
            boolean originalValue = blueprintBuildMode.get();
            blueprintBuildMode.set(true);
            try {
                original.handle(blueprint, blueprintName, packName, isOwn, level, player, pos, rotationMirror);
            } finally {
                blueprintBuildMode.set(originalValue);
            }
        } else {
            original.handle(blueprint, blueprintName, packName, isOwn, level, player, pos, rotationMirror);
        }
    }
}
