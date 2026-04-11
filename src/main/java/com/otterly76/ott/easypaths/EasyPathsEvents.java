package com.otterly76.ott.easypaths;

import com.mojang.datafixers.util.Pair;
import com.otterly76.ott.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class EasyPathsEvents {

    private static final Map<String, BlockPos> playerLastPos = new HashMap<>();
    private static final Map<BlockPos, Pair<Date, List<BlockPos>>> lastPath = new HashMap<>();
    private static int currentTick = 6000;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (currentTick != 0) {
            currentTick--;
            return;
        }
        currentTick = 6000;

        Date now = new Date();
        List<BlockPos> toRemove = new ArrayList<>();
        new HashMap<>(lastPath).forEach((key, value) -> {
            if (now.getTime() - value.getFirst().getTime() > 300000) {
                toRemove.add(key);
            }
        });
        toRemove.forEach(lastPath::remove);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide) return;

        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack handstack = player.getItemInHand(hand);

        if (!(handstack.getItem() instanceof ShovelItem)) return;

        BlockPos targetpos = event.getPos();
        Date now = new Date();
        Block block = level.getBlockState(targetpos).getBlock();

        if (block == Blocks.AIR) {
            targetpos = targetpos.below().immutable();
            block = level.getBlockState(targetpos).getBlock();
        }

        if (block == Blocks.DIRT_PATH) {
            if (lastPath.containsKey(targetpos)) {
                int count = 0;
                Pair<Date, List<BlockPos>> pair = lastPath.get(targetpos);

                if (now.getTime() - pair.getFirst().getTime() < 300000) {
                    for (BlockPos pathpos : pair.getSecond()) {
                        if (level.getBlockState(pathpos).getBlock() == Blocks.DIRT_PATH
                                && level.getBlockState(pathpos.above()).getBlock() == Blocks.AIR) {
                            level.setBlockAndUpdate(pathpos, Blocks.GRASS_BLOCK.defaultBlockState());
                            count++;
                        }
                    }
                }

                lastPath.remove(targetpos);
                player.sendSystemMessage(Component.literal("[Easy Paths] " + count + " grass blocks restored.").withStyle(ChatFormatting.AQUA));
                event.setCanceled(true);
            }
            return;
        }

        if (block != Blocks.GRASS_BLOCK) return;

        if (handstack.getDamageValue() >= handstack.getMaxDamage() - 1 && player.isCrouching()) {
            player.sendSystemMessage(Component.literal("[Easy Paths] Your shovel is too damaged to create paths.").withStyle(ChatFormatting.RED));
            event.setCanceled(true);
            return;
        }

        String playername = player.getName().getString();

        if (playerLastPos.containsKey(playername) && !player.isCrouching()) {
            BlockPos lastpos = playerLastPos.get(playername);

            boolean movex = true;
            int difx = lastpos.getX() - targetpos.getX();
            int difz = lastpos.getZ() - targetpos.getZ();
            int begindifx = difx;
            int begindifz = difz;

            List<Pair<Integer, Integer>> xzset = new ArrayList<>();
            List<BlockPos> pathpositions = new ArrayList<>(List.of(lastpos));

            for (int lyd = lastpos.getY() - 10; lyd < lastpos.getY() + 10; lyd++) {
                difx = begindifx;
                difz = begindifz;

                while (difx != 0 || difz != 0) {
                    if (movex) {
                        difx += moveToZero(difx);
                        movex = difz == 0;
                    } else {
                        difz += moveToZero(difz);
                        movex = difx != 0;
                    }
                    Pair<Integer, Integer> xz = new Pair<>(targetpos.getX() + difx, targetpos.getZ() + difz);
                    if (!xzset.contains(xz)) {
                        BlockPos betweenpos = new BlockPos(targetpos.getX() + difx, lyd, targetpos.getZ() + difz);
                        if (level.getBlockState(betweenpos).getBlock() == Blocks.GRASS_BLOCK) {
                            BlockPos abovepos = betweenpos.above();
                            Block aboveblock = level.getBlockState(abovepos).getBlock();
                            if (aboveblock != Blocks.AIR) {
                                if (aboveblock instanceof BushBlock || aboveblock instanceof CropBlock) {
                                    level.destroyBlock(abovepos, true);
                                } else {
                                    return;
                                }
                            }

                            level.setBlockAndUpdate(betweenpos, Blocks.DIRT_PATH.defaultBlockState());
                            pathpositions.add(betweenpos.immutable());
                            xzset.add(xz);

                            if (!player.isCreative()) {
                                handstack.hurtAndBreak(1, (ServerLevel) level, null, item -> {});
                            }
                        }
                    }
                }
            }

            if (handstack.getDamageValue() > handstack.getMaxDamage()) {
                handstack.setDamageValue(handstack.getMaxDamage() - 1);
            }

            lastPath.put(targetpos, new Pair<>(now, pathpositions));
            playerLastPos.remove(playername);
            player.sendSystemMessage(Component.literal("[Easy Paths] Path of " + pathpositions.size() + " blocks created. To undo, right click last clicked block again.").withStyle(ChatFormatting.AQUA));
        } else {
            if (!player.isCrouching()) return;

            level.setBlockAndUpdate(targetpos, Blocks.DIRT_PATH.defaultBlockState());

            if (playerLastPos.containsKey(playername)) {
                BlockPos lastpos = playerLastPos.get(playername);
                if (!lastpos.equals(targetpos) && level.getBlockState(lastpos).getBlock() == Blocks.DIRT_PATH) {
                    level.setBlockAndUpdate(lastpos, Blocks.GRASS_BLOCK.defaultBlockState());
                }
            }

            playerLastPos.put(playername, targetpos);
            player.sendSystemMessage(Component.literal("[Easy Paths] Starting point set to " + targetpos.getX() + ", " + targetpos.getY() + ", " + targetpos.getZ() + ".").withStyle(ChatFormatting.AQUA));
            event.setCanceled(true);
        }
    }

    private static int moveToZero(int n) {
        return -Integer.compare(n, 0);
    }
}