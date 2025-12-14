package com.otterly76.ott.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.particle.TrailParticleOption;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EyeblossomBlock extends FlowerBlock {
    public static final MapCodec<EyeblossomBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.BOOL.fieldOf("open").forGetter((eyeblossomBlock) -> eyeblossomBlock.type.open), propertiesCodec()).apply(instance, EyeblossomBlock::new));
    private static final int EYEBLOSSOM_XZ_RANGE = 3;
    private static final int EYEBLOSSOM_Y_RANGE = 2;
    private final Type type;

    public EyeblossomBlock(Type type, BlockBehaviour.Properties properties) {
        super(type.effect, type.effectDuration, properties);
        this.type = type;
    }

    public EyeblossomBlock(boolean bl, BlockBehaviour.Properties properties) {
        super(EyeblossomBlock.Type.fromBoolean(bl).effect, EyeblossomBlock.Type.fromBoolean(bl).effectDuration, properties);
        this.type = EyeblossomBlock.Type.fromBoolean(bl);
    }

    public @NotNull MapCodec<? extends EyeblossomBlock> codec() {
        return CODEC;
    }

    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (this.type.emitSounds() && randomSource.nextInt(700) == 0) {
            BlockState blockState2 = level.getBlockState(blockPos.below());
            if (blockState2.is(ModBlocks.PALE_MOSS_BLOCK.get())) {
                level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSounds.EYEBLOSSOM_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }

    }

    protected void randomTick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (this.tryChangingState(blockState, serverLevel, blockPos, randomSource)) {
            serverLevel.playSound(null, blockPos, this.type.transform().longSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.randomTick(blockState, serverLevel, blockPos, randomSource);
    }

    protected void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (this.tryChangingState(blockState, serverLevel, blockPos, randomSource)) {
            serverLevel.playSound(null, blockPos, this.type.transform().shortSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.tick(blockState, serverLevel, blockPos, randomSource);
    }

    private boolean tryChangingState(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (!serverLevel.dimensionType().natural()) {
            return false;
        } else if (serverLevel.isDay() != this.type.open) {
            return false;
        } else {
            Type type = this.type.transform();
            serverLevel.setBlock(blockPos, type.state(), 3);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, Context.of(blockState));
            type.spawnTransformParticle(serverLevel, blockPos, randomSource);
            BlockPos.betweenClosed(blockPos.offset(-3, -2, -3), blockPos.offset(3, 2, 3)).forEach((blockPos2) -> {
                BlockState blockState2 = serverLevel.getBlockState(blockPos2);
                if (blockState2 == blockState) {
                    double d = Math.sqrt(blockPos.distSqr(blockPos2));
                    int i = randomSource.nextIntBetweenInclusive((int)(d * (double)5.0F), (int)(d * (double)10.0F));
                    serverLevel.scheduleTick(blockPos2, blockState.getBlock(), i);
                }

            });
            return true;
        }
    }

    protected void entityInside(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Entity entity) {
        if (!level.isClientSide() && level.getDifficulty() != Difficulty.PEACEFUL && entity instanceof Bee bee) {
            if (bee.mayInteract(level, blockPos) && !bee.hasEffect(MobEffects.POISON)) {
                bee.addEffect(this.getBeeInteractionEffect());
            }
        }

    }

    public MobEffectInstance getBeeInteractionEffect() {
        return new MobEffectInstance(MobEffects.POISON, 25);
    }

    public enum Type {
        OPEN(true, MobEffects.BLINDNESS, 11.0F, ModSounds.EYEBLOSSOM_OPEN_LONG.get(), ModSounds.EYEBLOSSOM_OPEN.get(), 16545810),
        CLOSED(false, MobEffects.CONFUSION, 7.0F, ModSounds.EYEBLOSSOM_CLOSE_LONG.get(), ModSounds.EYEBLOSSOM_CLOSE.get(), 6250335);

        final boolean open;
        final Holder<MobEffect> effect;
        final float effectDuration;
        final SoundEvent longSwitchSound;
        final SoundEvent shortSwitchSound;
        private final int particleColor;

        Type(final boolean bl, final Holder<MobEffect> holder, final float f, final SoundEvent soundEvent, final SoundEvent soundEvent2, final int j) {
            this.open = bl;
            this.effect = holder;
            this.effectDuration = f;
            this.longSwitchSound = soundEvent;
            this.shortSwitchSound = soundEvent2;
            this.particleColor = j;
        }

        public static Type fromBoolean(boolean bl) {
            return bl ? OPEN : CLOSED;
        }

        public Block block() {
            return this.open ? ModBlocks.OPEN_EYEBLOSSOM.get() : ModBlocks.CLOSED_EYEBLOSSOM.get();
        }

        public BlockState state() {
            return this.block().defaultBlockState();
        }

        public Type transform() {
            return fromBoolean(!this.open);
        }

        public boolean emitSounds() {
            return this.open;
        }

        public void spawnTransformParticle(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
            Vec3 vec3 = blockPos.getCenter();
            double d = (double)0.5F + randomSource.nextDouble();
            Vec3 vec32 = new Vec3(randomSource.nextDouble() - (double)0.5F, randomSource.nextDouble() + (double)1.0F, randomSource.nextDouble() - (double)0.5F);
            Vec3 vec33 = vec3.add(vec32.scale(d));
            TrailParticleOption trailParticleOption = new TrailParticleOption(vec33, this.particleColor, (int)((double)20.0F * d));
            serverLevel.sendParticles(trailParticleOption, vec3.x, vec3.y, vec3.z, 1, 0.0F, 0.0F, 0.0F, 0.0F);
        }

        public SoundEvent getLongSwitchSound() {
            return this.longSwitchSound;
        }
    }
}