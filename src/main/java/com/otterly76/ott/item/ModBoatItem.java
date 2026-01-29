package com.otterly76.ott.item;


import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    private final Supplier<? extends EntityType<? extends Boat>> type;
    private final Consumer<Boat> initializer;

    public ModBoatItem(Supplier<? extends EntityType<? extends Boat>> type, Properties properties) {
        this(type, properties, boat -> {});
    }

    public ModBoatItem(Supplier<? extends EntityType<? extends Boat>> type, Properties properties, Consumer<Boat> initializer) {
        super(properties);
        this.type = type;
        this.initializer = initializer;
    }

    @SuppressWarnings({"DuplicatedCode", "IfStatementWithIdenticalBranches"})
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 vec3 = player.getViewVector(1.0F);
            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 vec31 = player.getEyePosition();
                for (Entity entity : list) {
                    if (entity.getBoundingBox().inflate(entity.getPickRadius()).contains(vec31)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }

            if (hitresult.getType() == HitResult.Type.BLOCK) {
                Boat boat = this.type.get().create(level);
                if (boat != null) {
                    this.initializer.accept(boat);

                    boat.setPos(hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z);
                    boat.setYRot(player.getYRot());
                    if (!level.noCollision(boat, boat.getBoundingBox())) {
                        return InteractionResultHolder.fail(itemstack);
                    } else {
                        if (!level.isClientSide) {
                            level.addFreshEntity(boat);
                            level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.getLocation());
                            if (!player.getAbilities().instabuild) {
                                itemstack.shrink(1);
                            }
                        }
                        player.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                    }
                }
            }
            return InteractionResultHolder.pass(itemstack);
        }
    }
}
