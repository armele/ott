package com.otterly76.ott.entity.custom;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CopperGolemStatueBlock;
import com.otterly76.ott.entity.ai.goal.CopperGolemChestGoal;
import com.otterly76.ott.entity.ai.goal.PressCopperButtonGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CopperGolem extends AbstractGolem implements ContainerUser, HasCustomInventoryScreen {
    private static final EntityDataAccessor<Integer> DATA_WEATHER_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GOLEM_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_HAS_POPPY = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState pressingButtonAnimationState = new AnimationState();

    private final SimpleContainer inventory = new SimpleContainer(1);
    private long nextWeatheringTick = -1L;

    public CopperGolem(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CopperGolemChestGoal(this, 0.8D));
        this.goalSelector.addGoal(2, new PressCopperButtonGoal(this, 1.0D, 16));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_WEATHER_STATE, 0);
        builder.define(DATA_GOLEM_STATE, CopperGolemState.IDLE.id());
        builder.define(DATA_HAS_POPPY, false);
    }

    public WeatheringCopper.WeatherState getWeatherState() {
        return WeatheringCopper.WeatherState.values()[this.entityData.get(DATA_WEATHER_STATE)];
    }

    public void setWeatherState(WeatheringCopper.WeatherState state) {
        this.entityData.set(DATA_WEATHER_STATE, state.ordinal());
    }

    public CopperGolemState getGolemState() {
        return CopperGolemState.values()[this.entityData.get(DATA_GOLEM_STATE)];
    }

    public void setGolemState(CopperGolemState state) {
        this.entityData.set(DATA_GOLEM_STATE, state.id());
    }

    public boolean hasPoppy() {
        return this.entityData.get(DATA_HAS_POPPY);
    }

    public void setHasPoppy(boolean hasPoppy) {
        this.entityData.set(DATA_HAS_POPPY, hasPoppy);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.isInWater()) {
            this.setAirSupply(this.getMaxAirSupply());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        } else {
            this.updateWeathering();
        }
    }

    private void setupAnimationStates() {
        CopperGolemState state = this.getGolemState();
        if (state == CopperGolemState.PRESSING_BUTTON || state == CopperGolemState.GETTING_ITEM || state == CopperGolemState.DROPPING_ITEM) {
            this.pressingButtonAnimationState.startIfStopped(this.tickCount);
        } else {
            this.pressingButtonAnimationState.stop();
        }
        
        if (this.onGround() && this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-7D && state == CopperGolemState.IDLE) {
            this.idleAnimationState.startIfStopped(this.tickCount);
        } else {
            this.idleAnimationState.stop();
        }
    }

    private void updateWeathering() {
        if (this.isAlive() && this.getWeatherState() != WeatheringCopper.WeatherState.OXIDIZED) {
            long gameTime = this.level().getGameTime();
            if (this.nextWeatheringTick == -1L) {
                // Average time for copper to weather is 3.5 - 5 days.
                // 3.5 days = 84,000 ticks. 5 days = 120,000 ticks.
                this.nextWeatheringTick = gameTime + (long)(this.random.nextInt(40000) + 80000);
            }

            if (gameTime >= this.nextWeatheringTick) {
                this.nextWeatheringTick = -1L;
                this.setWeatherState(WeatheringCopper.WeatherState.values()[this.getWeatherState().ordinal() + 1]);
            }
        }
        
        if (this.getWeatherState() == WeatheringCopper.WeatherState.OXIDIZED && !this.level().isClientSide) {
            if (this.random.nextFloat() < 0.0058F) { // 0.58% chance per tick
                this.turnToStatue();
            }
        }
    }

    public void turnToStatue() {
        if (!this.level().isClientSide && this.isAlive()) {
            BlockPos pos = this.blockPosition();
            if (this.level().isEmptyBlock(pos)) {
                WeatheringCopper.WeatherState state = this.getWeatherState();
                Block statueBlock = getStatueBlock(state);
                
                BlockState blockState = statueBlock.defaultBlockState()
                        .setValue(CopperGolemStatueBlock.FACING, this.getDirection())
                        .setValue(CopperGolemStatueBlock.POSE, CopperGolemStatueBlock.Pose.values()[this.random.nextInt(CopperGolemStatueBlock.Pose.values().length)]);
                
                this.level().setBlockAndUpdate(pos, blockState);
                this.inventory.removeAllItems().forEach(this::spawnAtLocation);
                if (this.hasPoppy()) this.spawnAtLocation(Items.POPPY);
                this.discard();
            }
        }
    }

    private Block getStatueBlock(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED -> ModBlocks.COPPER_GOLEM_STATUES.get("").get();
            case EXPOSED -> ModBlocks.COPPER_GOLEM_STATUES.get("exposed_").get();
            case WEATHERED -> ModBlocks.COPPER_GOLEM_STATUES.get("weathered_").get();
            case OXIDIZED -> ModBlocks.COPPER_GOLEM_STATUES.get("oxidized_").get();
        };
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WeatherState", this.getWeatherState().ordinal());
        tag.putInt("GolemState", this.getGolemState().id());
        tag.put("Inventory", this.inventory.createTag(this.registryAccess()));
        tag.putBoolean("HasPoppy", this.hasPoppy());
        tag.putLong("NextWeatheringTick", this.nextWeatheringTick);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WeatherState")) {
            this.setWeatherState(WeatheringCopper.WeatherState.values()[tag.getInt("WeatherState")]);
        }
        if (tag.contains("GolemState")) {
            this.setGolemState(CopperGolemState.values()[tag.getInt("GolemState")]);
        }
        if (tag.contains("Inventory")) {
            this.inventory.fromTag(tag.getList("Inventory", 10), this.registryAccess());
        }
        this.setHasPoppy(tag.getBoolean("HasPoppy"));
        if (tag.contains("NextWeatheringTick")) {
            this.nextWeatheringTick = tag.getLong("NextWeatheringTick");
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if (itemstack.is(Items.SHEARS) && this.hasPoppy()) {
            this.setHasPoppy(false);
            if (!this.level().isClientSide) {
                this.spawnAtLocation(Items.POPPY);
                itemstack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (itemstack.isEmpty() && !this.inventory.getItem(0).isEmpty()) {
            if (!this.level().isClientSide) {
                ItemStack held = this.inventory.removeItem(0, 64);
                if (!player.getInventory().add(held)) {
                    player.drop(held, false);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (itemstack.is(Items.COPPER_INGOT) && this.getHealth() < this.getMaxHealth()) {
            this.heal(5.0F);
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {}

    @Override
    public boolean hasContainerOpen(@NotNull ContainerOpenersCounter counter, @NotNull BlockPos pos) {
        return false;
    }

    @Override
    public double getContainerInteractionRange() {
        return 2.5D;
    }
}
