package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class BlockRunnerHandler {
    private static final ResourceLocation SPEED_BOOST_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block_runner_speed");
    
    private static final AttributeModifier PATH_SPEED_MODIFIER = new AttributeModifier(SPEED_BOOST_ID, 0.35, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final AttributeModifier STONE_SPEED_MODIFIER = new AttributeModifier(SPEED_BOOST_ID, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || !player.onGround()) {
            removeModifier(player);
            return;
        }

        BlockState state = player.getBlockStateOn();
        AttributeModifier modifier = null;

        if (state.is(ModTags.Blocks.PATHS)) {
            modifier = PATH_SPEED_MODIFIER;
        } else if (state.is(ModTags.Blocks.STONE)) {
            modifier = STONE_SPEED_MODIFIER;
        }

        applyModifier(player, modifier);
    }

    private static void applyModifier(Player player, AttributeModifier modifier) {
        var movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            if (modifier != null) {
                movementSpeed.addOrUpdateTransientModifier(modifier);
            } else {
                movementSpeed.removeModifier(SPEED_BOOST_ID);
            }
        }
    }

    private static void removeModifier(Player player) {
        var movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            movementSpeed.removeModifier(SPEED_BOOST_ID);
        }
    }
}
