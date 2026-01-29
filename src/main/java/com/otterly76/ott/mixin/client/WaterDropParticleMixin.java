package com.otterly76.ott.mixin.client;


import com.otterly76.ott.neoforge.impl.client.ClientModEvents;
import com.otterly76.ott.neoforge.impl.config.OttConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.otterly76.ott.api.core.Constants.MOD_ID;

@Mixin({WaterDropParticle.class})
public abstract class WaterDropParticleMixin extends TextureSheetParticleMixin {
    protected WaterDropParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
        super(clientLevel, d, e, f);
    }

    public void pickSprite(SpriteSet spriteSet, CallbackInfo ci) {
        if (OttConfig.WEATHER.BIOME_TINT.get()) {
            this.setSprite(Minecraft.getInstance().particleEngine.textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "splash" + this.random.nextInt(4))));
            ClientModEvents.applyWaterTint((TextureSheetParticle) (Object) this, this.level, BlockPos.containing(this.x, this.y, this.z));
        }
    }
}

