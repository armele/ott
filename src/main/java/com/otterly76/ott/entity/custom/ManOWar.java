package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ManOWar extends Animal implements GeoEntity, Bucketable {
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ManOWar.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(ManOWar.class, EntityDataSerializers.BOOLEAN);
    public float xBodyRot;
    public float xBodyRotO;
    public float zBodyRot;
    public float zBodyRotO;
    public float tentacleMovement;
    public float old_tentacleMovement;
    public float tentacleAngle;
    public float old_tentacleAngle;
    private float speed;
    private float tentacleSpeed;
    private float rotateSpeed;
    private float tx;
    private float ty;
    private float tz;
    public boolean glowLayer = false;
    private static final TagKey<Biome> DRY_BIOMES = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", "is_dry"));
    private static final RawAnimation SWIM_ANIMATION = RawAnimation.begin().thenPlay("animation.man_o_war.swim");
    private static final RawAnimation BEACHED_ANIMATION = RawAnimation.begin().thenPlay("animation.man_o_war.beached");

    public ManOWar(EntityType<? extends ManOWar> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.isUnobstructed(this);
    }

    protected void handleAirSupply(int air) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(air - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(this.getMaxAirSupply());
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.TROPICAL_FISH);
    }

    @Override
    public void baseTick() {
        int air = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(air);
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @SuppressWarnings("deprecation")
    public static boolean checkManOWarSpawnRules(EntityType<? extends ManOWar> entity, LevelAccessor world, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return pos.getY() <= world.getSeaLevel() - 2 && world.getFluidState(pos.below()).is(FluidTags.WATER);
    }

    @Override
    public int getMaxAirSupply() {
        int base = 6000;
        return this.level().getBiome(this.blockPosition()).is(DRY_BIOMES) ? base / this.getRandom().nextInt(1, 4) : base;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 1.2).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return this.isBaby() ? Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand)) : super.mobInteract(player, hand);
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        if (player instanceof ServerPlayer && player.hurt(player.damageSources().mobAttack(this), 1.0F)) {
            RandomSource rand = player.getRandom();
            int i = rand.nextInt(4);
            if (i <= 2) {
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 2), this);
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 200), this);
            }

            if (player.hasEffect(MobEffects.UNLUCK)) {
                player.kill();
            }
        }
    }

    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0, 1.0));
        this.goalSelector.addGoal(2, new ManOWarRandomMovementGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 19) {
            this.tentacleMovement = 0.0F;
        } else {
            super.handleEntityEvent(b);
        }
    }

    public void setMovementVector(float f, float g, float h) {
        this.tx = f;
        this.ty = g;
        this.tz = h;
    }

    public boolean hasMovementVector() {
        return this.tx != 0.0F || this.ty != 0.0F || this.tz != 0.0F;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (spawnType == MobSpawnType.BUCKET) {
            this.setBaby(true);
        } else {
            this.setColor(getRandColor(this.random));
        }
        return spawnGroupData;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        ManOWar manOWar = ModEntities.MAN_O_WAR.get().create(serverLevel);
        if (manOWar != null) {
            manOWar.setColor(getRandColor(serverLevel.getRandom()));
        }
        return manOWar;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, 0);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Color", this.getRawColor());
        compoundTag.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setColor(compoundTag.getInt("Color"));
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
    }

    @Override
    public void aiStep() {
        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
            this.setOnGround(false);
            this.hasImpulse = true;
            this.playSound(SoundEvents.SALMON_FLOP, this.getSoundVolume(), this.getVoicePitch());
        }

        super.aiStep();
        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;
        this.old_tentacleMovement = this.tentacleMovement;
        this.old_tentacleAngle = this.tentacleAngle;
        this.tentacleMovement += this.tentacleSpeed;
        if ((double)this.tentacleMovement > (Math.PI * 2.0)) {
            if (this.level().isClientSide) {
                this.tentacleMovement = (float)(Math.PI * 2.0);
            } else {
                this.tentacleMovement = (float)((double)this.tentacleMovement - (Math.PI * 2.0));
                if (this.random.nextInt(10) == 0) {
                    this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }

                this.level().broadcastEntityEvent(this, (byte)19);
            }
        }

        if (this.isInWaterOrBubble()) {
            if (this.tentacleMovement < (float)Math.PI) {
                float f = this.tentacleMovement / (float)Math.PI;
                this.tentacleAngle = Mth.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;
                if ((double)f > 0.75D) {
                    this.speed = 1.0F;
                    this.rotateSpeed = 1.0F;
                } else {
                    this.rotateSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.speed *= 0.9F;
                this.rotateSpeed *= 0.99F;
            }

            if (!this.level().isClientSide) {
                this.setDeltaMovement(new Vec3(this.tx * this.speed, this.ty * this.speed, this.tz * this.speed));
            }

            Vec3 vec3 = this.getDeltaMovement();
            double d = vec3.horizontalDistance();
            this.yBodyRot += (-((float)Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float)Math.PI) - this.yBodyRot) * 0.1F;
            this.setYRot(this.yBodyRot);
            this.zBodyRot = (float)((double)this.zBodyRot + Math.PI * (double)this.rotateSpeed * 1.5);
            this.xBodyRot += (-((float)Mth.atan2(d, vec3.y)) * (180.0F / (float)Math.PI) - this.xBodyRot) * 0.1F;
        } else {
            this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * (float)Math.PI * 0.25F;
            if (!this.level().isClientSide) {
                double e = this.getDeltaMovement().y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    MobEffectInstance levitation = this.getEffect(MobEffects.LEVITATION);
                    if (levitation != null) {
                        e = 0.05 * (double)(levitation.getAmplifier() + 1);
                    }
                } else if (!this.isNoGravity()) {
                    e -= 0.08;
                }

                this.setDeltaMovement(new Vec3(0.0, e * 0.98, 0.0));
            }

            this.xBodyRot = (float)((double)this.xBodyRot + (double)(-90.0F - this.xBodyRot) * 0.02);
        }
    }

    public void setColor(Colors color) {
        this.setColor((color.ordinal() & 127) << 16);
    }

    public int getRawColor() {
        return this.entityData.get(COLOR);
    }

    public void setColor(int color) {
        this.entityData.set(COLOR, color);
    }

    public Colors getColor() {
        return ManOWar.Colors.byIndex(this.getRawColor() >> 16 & 127);
    }

    private static Colors getRandColor(RandomSource rand) {
        return switch (rand.nextInt(5)) {
            case 0 -> Colors.MAGENTA;
            case 1 -> Colors.PURPLE;
            case 2 -> Colors.RAINBOW;
            default -> Colors.BLUE;
        };
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    private <E extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.animation.AnimationState<E> event) {
        event.getController().transitionLength(0);
        return event.setAndContinue(this.isInWater() ? SWIM_ANIMATION : BEACHED_ANIMATION);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel level, @NotNull Animal animal) {
        int i = level.getRandom().nextIntBetweenInclusive(1, 3);
        for(int j = 0; j < i; ++j) {
            super.spawnChildFromBreeding(level, animal);
        }
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        stack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, (tag) -> {
            if (this.isNoAi()) tag.putBoolean("NoAI", this.isNoAi());
            if (this.isSilent()) tag.putBoolean("Silent", this.isSilent());
            if (this.isNoGravity()) tag.putBoolean("NoGravity", this.isNoGravity());
            if (this.hasGlowingTag()) tag.putBoolean("Glowing", true);
            if (this.isInvulnerable()) tag.putBoolean("Invulnerable", this.isInvulnerable());
            tag.putFloat("Health", this.getHealth());
            tag.putInt("Variant", this.getRawColor());
            tag.putInt("Age", this.getAge());
        });
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        if (tag.contains("NoAI")) this.setNoAi(tag.getBoolean("NoAI"));
        if (tag.contains("Silent")) this.setSilent(tag.getBoolean("Silent"));
        if (tag.contains("NoGravity")) this.setNoGravity(tag.getBoolean("NoGravity"));
        if (tag.contains("Glowing")) this.setGlowingTag(tag.getBoolean("Glowing"));
        if (tag.contains("Invulnerable")) this.setInvulnerable(tag.getBoolean("Invulnerable"));
        if (tag.contains("Health", 99)) this.setHealth(tag.getFloat("Health"));
        this.setColor(ManOWar.Colors.byIndex(tag.getInt("Variant")));
        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return ModItems.MAN_O_WAR_BUCKET.get().getDefaultInstance();
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    public static <T> T makeIndex(T[] array, int index) {
        return array[index >= array.length ? 0 : index];
    }

    public enum Colors implements StringRepresentable {
        BLUE("blue"),
        PURPLE("purple"),
        MAGENTA("magenta"),
        RAINBOW("rainbow");

        private final String name;

        Colors(String name) {
            this.name = name;
        }

        public static Colors byIndex(int index) {
            return ManOWar.makeIndex(values(), index);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    private static class ManOWarRandomMovementGoal extends Goal {
        private final ManOWar mano;

        private ManOWarRandomMovementGoal(ManOWar mano) {
            this.mano = mano;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int i = this.mano.getNoActionTime();
            if (i > 100) {
                this.mano.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.mano.getRandom().nextInt(50) == 0 || !this.mano.wasTouchingWater || !this.mano.hasMovementVector()) {
                float f = this.mano.getRandom().nextFloat() * ((float)Math.PI * 2F);
                float g = Mth.cos(f) * 0.2F;
                float h = -0.1F + this.mano.getRandom().nextFloat() * 0.2F;
                float j = Mth.sin(f) * 0.2F;
                this.mano.setMovementVector(g, h, j);
            }
        }
    }
}
