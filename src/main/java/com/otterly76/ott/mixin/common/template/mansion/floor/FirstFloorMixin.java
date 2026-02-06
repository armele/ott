package com.otterly76.ott.mixin.common.template.mansion.floor;

import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.duck.MansionRoom;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces$FirstFloorRoomCollection")
public abstract class FirstFloorMixin implements MansionRoom {
    @Inject(
            method = {"get1x1(Lnet/minecraft/util/RandomSource;)Ljava/lang/String;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void use1x1TemplateList(RandomSource random, CallbackInfoReturnable<String> cir) {
        if (ConfigHandler.getConfig().breaksSeedParity()) {
            cir.setReturnValue(this.ott$getRandom("1x1", random));
        }

    }

    @Inject(
            method = {"get1x1Secret(Lnet/minecraft/util/RandomSource;)Ljava/lang/String;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void use1x1SecretTemplateList(RandomSource random, CallbackInfoReturnable<String> cir) {
        if (ConfigHandler.getConfig().breaksSeedParity()) {
            cir.setReturnValue(this.ott$getRandom("1x1_secret", random));
        }

    }

    @Inject(
            method = {"get1x2SideEntrance(Lnet/minecraft/util/RandomSource;Z)Ljava/lang/String;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void use1x2SideTemplateList(RandomSource random, boolean bl, CallbackInfoReturnable<String> cir) {
        if (ConfigHandler.getConfig().breaksSeedParity() && !bl) {
            cir.setReturnValue(this.ott$getRandom("1x2_side", random));
        }

    }

    @Inject(
            method = {"get1x2FrontEntrance(Lnet/minecraft/util/RandomSource;Z)Ljava/lang/String;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void use1x2FrontTemplateList(RandomSource random, boolean bl, CallbackInfoReturnable<String> cir) {
        if (ConfigHandler.getConfig().breaksSeedParity() && !bl) {
            cir.setReturnValue(this.ott$getRandom("1x2_front", random));
        }

    }

    @Inject(
            method = {"get1x2Secret(Lnet/minecraft/util/RandomSource;)Ljava/lang/String;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void use1x2SecretTemplateList(RandomSource random, CallbackInfoReturnable<String> cir) {
        if (ConfigHandler.getConfig().breaksSeedParity()) {
            cir.setReturnValue(this.ott$getRandom("1x2_secret", random));
        }

    }

    @Inject(
            method = {"get2x2(Lnet/minecraft/util/RandomSource;)Ljava/lang/String;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void use2x2TemplateList(RandomSource random, CallbackInfoReturnable<String> cir) {
        if (ConfigHandler.getConfig().breaksSeedParity()) {
            cir.setReturnValue(this.ott$getRandom("2x2", random));
        }

    }

    @Inject(
            method = {"get2x2Secret(Lnet/minecraft/util/RandomSource;)Ljava/lang/String;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void use2x2SecretTemplateList(RandomSource random, CallbackInfoReturnable<String> cir) {
        if (ConfigHandler.getConfig().breaksSeedParity()) {
            cir.setReturnValue(this.ott$getRandom("2x2_secret", random));
        }

    }

    public int ott$floorNumber() {
        return 1;
    }
}
