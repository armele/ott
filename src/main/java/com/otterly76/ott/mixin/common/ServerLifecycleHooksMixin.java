package com.otterly76.ott.mixin.common;

import com.otterly76.ott.registry.OttRegistryKeys;
import com.otterly76.ott.worldgen.modifier.AbstractBiomeModifier;
import com.otterly76.ott.worldgen.modifier.Modifier;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(
        value = {ServerLifecycleHooks.class},
        remap = false
)
public class ServerLifecycleHooksMixin {
    @ModifyVariable(
            method = "runModifiers(Lnet/minecraft/server/MinecraftServer;)V",
            at = @At("STORE"),
            name = "biomeModifiers"
    )
    private static List<BiomeModifier> ott$injectBiomeModifiers(List<BiomeModifier> biomeModifiers, MinecraftServer server) {
        List<BiomeModifier> allBiomeModifiers = new ArrayList<>(biomeModifiers);
        Registry<Modifier> registry = server.registryAccess()
                .registryOrThrow(OttRegistryKeys.WORLDGEN_MODIFIER);

        for (Modifier modifier : registry) {
            if (modifier instanceof AbstractBiomeModifier biomeModifier) {
                allBiomeModifiers.add(biomeModifier.neoforgeBiomeModifier());
            }
        }

        return allBiomeModifiers;
    }
}
