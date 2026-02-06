package com.otterly76.ott.registry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.biome.BiomeEffects;
import com.otterly76.ott.worldgen.modifier.util.BiomeClimate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeSpecialEffectsBuilder;
import net.neoforged.neoforge.common.world.ClimateSettingsBuilder;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;

public class OttNeoforgeBiomeModifiers {

    public record ReplaceClimateBiomeModifier(HolderSet<Biome> biomes, BiomeClimate climateSettings) implements BiomeModifier {
        public static final MapCodec<ReplaceClimateBiomeModifier> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(ReplaceClimateBiomeModifier::biomes),
                BiomeClimate.CODEC.fieldOf("climate").forGetter(ReplaceClimateBiomeModifier::climateSettings)
        ).apply(builder, ReplaceClimateBiomeModifier::new));

        @Override
        public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, @NotNull ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.MODIFY && this.biomes().contains(biome)) {
                ClimateSettingsBuilder climate = builder.getClimateSettings();
                this.climateSettings.temperature().ifPresent(climate::setTemperature);
                this.climateSettings.temperatureModifier().ifPresent(climate::setTemperatureModifier);
                this.climateSettings.hasPrecipitation().ifPresent(climate::setHasPrecipitation);
                this.climateSettings.downfall().ifPresent(climate::setDownfall);
            }
        }

        @Override
        public @NotNull MapCodec<? extends BiomeModifier> codec() {
            return CODEC;
        }
    }

    public record ReplaceEffectsBiomeModifier(HolderSet<Biome> biomes, BiomeEffects specialEffects) implements BiomeModifier {
        public static final MapCodec<ReplaceEffectsBiomeModifier> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(ReplaceEffectsBiomeModifier::biomes),
                BiomeEffects.CODEC.fieldOf("effects").forGetter(ReplaceEffectsBiomeModifier::specialEffects)
        ).apply(builder, ReplaceEffectsBiomeModifier::new));

        @Override
        public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, @NotNull ModifiableBiomeInfo.BiomeInfo.Builder info) {
            if (phase == Phase.MODIFY && this.biomes().contains(biome)) {
                BiomeSpecialEffectsBuilder builder = info.getSpecialEffects();

                this.specialEffects.fogColor().ifPresent(builder::fogColor);
                this.specialEffects.waterColor().ifPresent(builder::waterColor);
                this.specialEffects.waterFogColor().ifPresent(builder::waterFogColor);
                this.specialEffects.skyColor().ifPresent(builder::skyColor);
                this.specialEffects.foliageColor().ifPresent(builder::foliageColorOverride);
                this.specialEffects.grassColor().ifPresent(builder::grassColorOverride);
                this.specialEffects.grassColorModifier().ifPresent(builder::grassColorModifier);
                this.specialEffects.ambientParticle().ifPresent(builder::ambientParticle);
                this.specialEffects.ambientSound().ifPresent(builder::ambientLoopSound);
                this.specialEffects.moodSound().ifPresent(builder::ambientMoodSound);
                this.specialEffects.additionsSound().ifPresent(builder::ambientAdditionsSound);
                this.specialEffects.music().ifPresent(builder::backgroundMusic);
            }
        }

        @Override
        public @NotNull MapCodec<? extends BiomeModifier> codec() {
            return CODEC;
        }
    }
}
