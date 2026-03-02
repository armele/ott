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

    public Type getType() {
        return this.type;
    }

    public EyeblossomBlock(Type type, BlockBehaviour.Properties properties) {
        super(type.effect, (float)type.effectDuration, properties);
        this.type = type;
    }

    public EyeblossomBlock(boolean open, BlockBehaviour.Properties properties) {
        super(EyeblossomBlock.Type.fromBoolean(open).effect, (float)EyeblossomBlock.Type.fromBoolean(open).effectDuration, properties);
        this.type = EyeblossomBlock.Type.fromBoolean(open);
    }

    public @NotNull MapCodec<? extends EyeblossomBlock> codec() {
        return CODEC;
    }

    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (this.type.emitSounds() && random.nextInt(700) == 0) {
            BlockState floorState = level.getBlockState(pos.below());
            if (floorState.is(ModBlocks.PALE_MOSS_BLOCK.get())) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.EYEBLOSSOM_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (this.tryChangingState(state, level, pos, random)) {
            level.playSound(null, pos, this.type.transform().longSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.randomTick(state, level, pos, random);
    }

    @Override
    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (this.tryChangingState(state, level, pos, random)) {
            level.playSound(null, pos, this.type.transform().shortSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.tick(state, level, pos, random);
    }

    private boolean tryChangingState(BlockState state, ServerLevel level, BlockPos origin, RandomSource random) {
        if (!level.dimensionType().natural()) {
            return false;
        } else if (CreakingHeartBlock.isNaturalNight(level) == this.type.open) {
            return false;
        } else {
            Type type = this.type.transform();
            level.setBlock(origin, type.state(), 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, origin, Context.of(state));
            type.spawnTransformParticle(level, origin, random);
            BlockPos.betweenClosed(origin.offset(-3, -2, -3), origin.offset(3, 2, 3)).forEach((pos) -> {
                BlockState closeState = level.getBlockState(pos);
                if (closeState == state) {
                    double distance = Math.sqrt(origin.distSqr(pos));
                    int ticks = random.nextIntBetweenInclusive((int)(distance * 5.0), (int)(distance * 10.0));
                    level.scheduleTick(pos, state.getBlock(), ticks);
                }

            });
            return true;
        }
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide() && level.getDifficulty() != Difficulty.PEACEFUL && entity instanceof Bee bee) {
            if (!bee.hasEffect(MobEffects.POISON)) {
                bee.addEffect(new MobEffectInstance(MobEffects.POISON, 25));
            }
        }
    }

    public enum Type {
        OPEN(true, MobEffects.BLINDNESS, 11, ModSounds.EYEBLOSSOM_OPEN_LONG, ModSounds.EYEBLOSSOM_OPEN, 16545810),
        CLOSED(false, MobEffects.CONFUSION, 7, ModSounds.EYEBLOSSOM_CLOSE_LONG, ModSounds.EYEBLOSSOM_CLOSE, 6250335);

        final boolean open;
        final Holder<MobEffect> effect;
        final int effectDuration;
        private final java.util.function.Supplier<SoundEvent> longSwitchSound;
        private final java.util.function.Supplier<SoundEvent> shortSwitchSound;
        final int particleColor;

        Type(boolean open, Holder<MobEffect> effect, int effectDuration, java.util.function.Supplier<SoundEvent> longSwitchSound, java.util.function.Supplier<SoundEvent> shortSwitchSound, int particleColor) {
            this.open = open;
            this.effect = effect;
            this.effectDuration = effectDuration;
            this.longSwitchSound = longSwitchSound;
            this.shortSwitchSound = shortSwitchSound;
            this.particleColor = particleColor;
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

        public void spawnTransformParticle(ServerLevel level, BlockPos pos, RandomSource random) {
            Vec3 center = pos.getCenter();
            double scale = 0.5 + random.nextDouble();
            Vec3 offset = new Vec3(random.nextDouble() - 0.5, random.nextDouble() + 1.0, random.nextDouble() - 0.5);
            Vec3 target = center.add(offset.scale(scale));
            TrailParticleOption trailParticleOption = new TrailParticleOption(target, this.particleColor, (int)(20.0 * scale));
            level.sendParticles(trailParticleOption, center.x, center.y, center.z, 1, 0.0, 0.0, 0.0, 0.0);
        }

        public SoundEvent longSwitchSound() {
            return this.longSwitchSound.get();
        }

        public SoundEvent shortSwitchSound() {
            return this.shortSwitchSound.get();
        }
    }
}
