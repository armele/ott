package com.otterly76.ott.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import com.otterly76.ott.registry.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import com.otterly76.ott.inventory.NautilusContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNautilusEntity extends TamableAnimal implements PlayerRideableJumping {

    // ── Food tags (backport — not present in 1.21.1 ItemTags) ────────────────
    public static final TagKey<Item> NAUTILUS_TAMING_ITEMS =
            TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:nautilus_taming_items"));
    public static final TagKey<Item> NAUTILUS_FOOD =
            TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:nautilus_food"));
    public static final TagKey<Item> NAUTILUS_BUCKET_FOOD =
            TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:nautilus_bucket_food"));

    // ── Synced data ───────────────────────────────────────────────────────────
    private static final EntityDataAccessor<Boolean> DASH =
            SynchedEntityData.defineId(AbstractNautilusEntity.class, EntityDataSerializers.BOOLEAN);

    // ── Riding / movement constants ───────────────────────────────────────────
    private static final float IN_WATER_SPEED_MODIFIER      = 0.011F;
    private static final float RIDDEN_SPEED_IN_WATER        = 0.0325F;
    private static final float RIDDEN_SPEED_ON_LAND         = 0.02F;
    private static final int   DASH_COOLDOWN_TICKS          = 40;

    // ── Instance fields ───────────────────────────────────────────────────────
    private int dashCooldown = 0;
    protected float playerJumpPendingScale;
    /** Saddle (slot 0) + body armor (slot 1). */
    protected SimpleContainer inventory;

    protected AbstractNautilusEntity(EntityType<? extends AbstractNautilusEntity> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, IN_WATER_SPEED_MODIFIER, 0.0F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.createInventory();
    }

    // ── Attributes ────────────────────────────────────────────────────────────

    /** Spawn rule: 5–25 blocks below sea level, water above and below. */
    public static boolean checkNautilusSpawnRules(
            EntityType<? extends AbstractNautilusEntity> type,
            LevelAccessor level,
            MobSpawnType reason,
            BlockPos pos,
            RandomSource random) {
        int seaLevel = level.getSeaLevel();
        return pos.getY() >= seaLevel - 25
                && pos.getY() <= seaLevel - 5
                && level.getFluidState(pos.below()).is(net.minecraft.tags.FluidTags.WATER)
                && level.getBlockState(pos.above()).is(Blocks.WATER);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3);
    }

    // ── Synced data ───────────────────────────────────────────────────────────

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DASH, false);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        if (!this.firstTick && DASH.equals(accessor)) {
            this.dashCooldown = this.dashCooldown == 0 ? DASH_COOLDOWN_TICKS : this.dashCooldown;
        }
        super.onSyncedDataUpdated(accessor);
    }

    // ── Navigation / movement ─────────────────────────────────────────────────

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        return 0.0F;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        // nautiluses make no step sounds
    }

    // ── Equipment ─────────────────────────────────────────────────────────────

    /** Saddle and body-armor slots require the nautilus to be alive, adult, and tamed. */
    @Override
    public boolean canUseSlot(@NotNull EquipmentSlot slot) {
        if (slot == EquipmentSlot.BODY) {
            return this.isAlive() && !this.isBaby() && this.isTame();
        }
        return super.canUseSlot(slot);
    }

    public boolean isSaddled() {
        return !this.inventory.getItem(0).isEmpty();
    }

    // ── Riding ────────────────────────────────────────────────────────────────

    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return !this.isVehicle();
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        Entity first = this.getFirstPassenger();
        if (this.isSaddled() && first instanceof Player player) {
            return player;
        }
        return super.getControllingPassenger();
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player controller, @NotNull Vec3 selfInput) {
        float strafe = controller.xxa;
        float forward = 0.0F;
        float up = 0.0F;
        if (controller.zza != 0.0F) {
            float cos = Mth.cos(controller.getXRot() * ((float) Math.PI / 180F));
            float sin = -Mth.sin(controller.getXRot() * ((float) Math.PI / 180F));
            if (controller.zza < 0.0F) {
                cos *= -0.5F;
                sin *= -0.5F;
            }
            forward = cos;
            up = sin;
        }
        return new Vec3(strafe, up, forward);
    }

    protected @NotNull Vec2 getRiddenRotation(@NotNull LivingEntity controller) {
        return new Vec2(controller.getXRot() * 0.5F, controller.getYRot());
    }

    @Override
    protected void tickRidden(@NotNull Player controller, @NotNull Vec3 riddenInput) {
        super.tickRidden(controller, riddenInput);
        Vec2 rotation = this.getRiddenRotation(controller);
        float yRot = this.getYRot();
        yRot += Mth.wrapDegrees(rotation.y - yRot) * 0.5F;
        this.setRot(yRot, rotation.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
        if (!this.level().isClientSide) {
            if (this.playerJumpPendingScale > 0.0F) {
                this.executeRidersJump(this.playerJumpPendingScale, controller);
            }
            this.playerJumpPendingScale = 0.0F;
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player controller) {
        return (float) ((this.isInWater() ? RIDDEN_SPEED_IN_WATER : RIDDEN_SPEED_ON_LAND)
                * this.getAttributeValue(Attributes.MOVEMENT_SPEED));
    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide) {
            player.startRiding(this);
        }
    }

    // ── Dash mechanic ─────────────────────────────────────────────────────────

    public boolean isDashing() {
        return this.entityData.get(DASH);
    }

    public void setDashing(boolean dashing) {
        this.entityData.set(DASH, dashing);
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void onPlayerJump(int jumpAmount) {
        if (this.isSaddled() && this.dashCooldown <= 0) {
            this.playerJumpPendingScale = (float) jumpAmount / 100.0F;
        }
    }

    protected void executeRidersJump(float amount, Player controller) {
        this.addDeltaMovement(controller.getLookAngle()
                .scale((this.isInWater() ? 1.2F : 0.5F) * amount
                        * this.getAttributeValue(Attributes.MOVEMENT_SPEED)
                        * this.getBlockSpeedFactor()));
        this.dashCooldown = DASH_COOLDOWN_TICKS;
        this.setDashing(true);
        this.hasImpulse = true;
    }

    @Override
    public void handleStartJump(int jumpScale) {
        @Nullable SoundEvent sound = this.getDashSound();
        if (sound != null) this.makeSound(sound);
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.setDashing(true);
    }

    @Override
    public int getJumpCooldown() {
        return this.dashCooldown;
    }

    @Override
    public void handleStopJump() {
    }

    @Nullable
    protected SoundEvent getDashSound() {
        return null;
    }

    @Nullable
    protected SoundEvent getDashReadySound() {
        return null;
    }

    // ── Inventory (saddle + body armor) ───────────────────────────────────────

    protected void createInventory() {
        SimpleContainer old = this.inventory;
        this.inventory = new SimpleContainer(2); // slot 0 = saddle, slot 1 = body armor
        if (old != null) {
            for (int i = 0; i < Math.min(old.getContainerSize(), 2); i++) {
                ItemStack item = old.getItem(i);
                if (!item.isEmpty()) {
                    this.inventory.setItem(i, item.copy());
                }
            }
        }
    }

    public boolean hasInventoryChanged(Container other) {
        return this.inventory != other;
    }

    /** Populates the SimpleContainer from current equipment slots before opening the GUI. */
    public void syncInventoryFromEquipment() {
        this.inventory.setItem(1, this.getItemBySlot(EquipmentSlot.BODY).copy());
    }

    // ── Passenger effects ─────────────────────────────────────────────────────

    private void applyPassengerEffects() {
        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof Player player) {
            long time = this.level().getGameTime();
            if (!player.hasEffect(ModEffects.BREATH_OF_NAUTILUS) || time % 40L == 0L) {
                player.addEffect(new MobEffectInstance(ModEffects.BREATH_OF_NAUTILUS, 60, 0, true, true, true));
            }
        }
    }

    private void spawnBubbles() {
        double speed = this.getDeltaMovement().length();
        double prob  = Mth.clamp(speed * 2.0, 0.15, 1.0);
        if (this.random.nextFloat() < prob) {
            float xRot = Mth.clamp(this.getXRot(), -10.0F, 10.0F);
            Vec3 dir = this.calculateViewVector(xRot, this.getYRot());
            double spread = this.random.nextDouble() * 0.8 * (1.0 + speed);
            this.level().addParticle(ParticleTypes.BUBBLE,
                    this.getX() - dir.x * 1.1,
                    this.getY() - dir.y + 0.25,
                    this.getZ() - dir.z * 1.1,
                    (this.random.nextFloat() - 0.5F) * spread,
                    (this.random.nextFloat() - 0.5F) * spread,
                    (this.random.nextFloat() - 0.5F) * spread);
        }
    }

    // ── Tick ─────────────────────────────────────────────────────────────────

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.applyPassengerEffects();
        }
        if (this.isDashing() && this.dashCooldown < 35) {
            this.setDashing(false);
        }
        if (this.dashCooldown > 0) {
            --this.dashCooldown;
            if (this.dashCooldown == 0) {
                @Nullable SoundEvent ready = this.getDashReadySound();
                if (ready != null) this.makeSound(ready);
            }
        }
        if (this.isInWater()) {
            this.spawnBubbles();
        }
    }

    // ── Food / feeding ────────────────────────────────────────────────────────

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        if (!this.isTame() && !this.isBaby()) {
            return itemStack.is(NAUTILUS_TAMING_ITEMS);
        }
        return itemStack.is(NAUTILUS_FOOD);
    }

    /** Called when the player feeds the nautilus. Bucket-food swaps bucket to water bucket. */
    @Override
    protected void usePlayerItem(@NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack itemStack) {
        if (itemStack.is(NAUTILUS_BUCKET_FOOD)) {
            player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
        } else {
            super.usePlayerItem(player, hand, itemStack);
        }
    }

    protected abstract void playEatingSound();

    private void tryToTame(Player player, ItemStack itemStack, InteractionHand hand) {
        this.usePlayerItem(player, hand, itemStack);
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.level().broadcastEntityEvent(this, (byte) 7); // hearts
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6); // smoke
        }
        this.playEatingSound();
    }

    // ── Interaction ───────────────────────────────────────────────────────────

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        this.setPersistenceRequired();
        ItemStack itemStack = player.getItemInHand(hand);

        if (this.isBaby()) {
            return super.mobInteract(player, hand);
        }

        // Shift+right-click opens inventory
        if (this.isTame() && player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                this.syncInventoryFromEquipment();
                player.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new NautilusContainerMenu(id, inv, this.inventory, this),
                        Component.translatable("gui.ott.nautilus_inventory")
                ));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!itemStack.isEmpty()) {
            // Equip saddle
            if (itemStack.is(Items.SADDLE) && !this.isBaby() && this.isTame() && !this.isSaddled()) {
                this.inventory.setItem(0, itemStack.copyWithCount(1));
                itemStack.shrink(1);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            // Equip nautilus armor
            if (itemStack.getItem() instanceof AnimalArmorItem animalArmor
                    && animalArmor.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN
                    && this.canUseSlot(EquipmentSlot.BODY)
                    && this.getItemBySlot(EquipmentSlot.BODY).isEmpty()) {
                this.setItemSlot(EquipmentSlot.BODY, itemStack.copyWithCount(1));
                itemStack.shrink(1);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            // Taming attempt with taming food (untamed)
            if (!this.level().isClientSide && !this.isTame() && this.isFood(itemStack)) {
                this.tryToTame(player, itemStack, hand);
                return InteractionResult.SUCCESS;
            }

            // Healing with food (tamed)
            if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
                this.usePlayerItem(player, hand, itemStack);
                this.playEatingSound();
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            // Item self-interaction (e.g. lead, name tag)
            InteractionResult result = itemStack.interactLivingEntity(player, this, hand);
            if (result.consumesAction()) {
                return result;
            }
        }

        // Mount when tamed and player isn't sneaking
        if (this.isTame() && !player.isSecondaryUseActive()) {
            this.doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    // ── Hurt ─────────────────────────────────────────────────────────────────

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        return super.hurt(source, damage);
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effectInstance) {
        // Nautiluses are immune to poison
        return effectInstance.getEffect() == MobEffects.POISON ? false : super.canBeAffected(effectInstance);
    }

    // ── Spawn ─────────────────────────────────────────────────────────────────

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level,
                                                  @NotNull DifficultyInstance difficulty,
                                                  @NotNull MobSpawnType reason,
                                                  @Nullable SpawnGroupData groupData) {
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return !this.isTame();
    }

    // ── Misc ──────────────────────────────────────────────────────────────────

    protected boolean isMobControlled() {
        return this.getFirstPassenger() instanceof Mob;
    }

    // ── Save / Load ───────────────────────────────────────────────────────────

    @Override
    public void addAdditionalSaveData(@NotNull net.minecraft.nbt.CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddle", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(@NotNull net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.createInventory();
        if (tag.getBoolean("Saddle")) {
            this.inventory.setItem(0, new net.minecraft.world.item.ItemStack(Items.SADDLE));
        }
        this.inventory.setItem(1, this.getItemBySlot(EquipmentSlot.BODY).copy());
    }
}
