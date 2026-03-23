package com.otterly76.ott.block.entity;

import com.otterly76.ott.entity.custom.Butterfly;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ButterflyJarBlockEntity extends BlockEntity implements GeoBlockEntity, Nameable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private Butterfly.Variant variant = Butterfly.Variant.MONARCH;

    public ButterflyJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BUTTERFLY_JAR.get(), pos, state);
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable("block.ott.butterfly_jar." + this.variant.getName());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.getName();
    }

    public Butterfly.Variant getVariant() {
        return variant;
    }

    public void setVariant(Butterfly.Variant variant) {
        this.variant = variant;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Variant")) {
            this.variant = Butterfly.Variant.getTypeById(tag.getInt("Variant"));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Variant", this.variant.getId());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putInt("Variant", this.variant.getId());
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(software.bernie.geckolib.animation.AnimationState<ButterflyJarBlockEntity> state) {
        state.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}