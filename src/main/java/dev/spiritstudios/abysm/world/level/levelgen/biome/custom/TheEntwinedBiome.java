package dev.spiritstudios.abysm.world.level.levelgen.biome.custom;

import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiomes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public final class TheEntwinedBiome extends AbysmBiome {

	public TheEntwinedBiome() {
		super(AbysmBiomes.THE_ENTWINED, 0.25F, true, 0.5F);
	}

	@Override
	public EnvironmentAttributeMap.Builder createAttributes() {
		return EnvironmentAttributeMap.builder()
			.set(EnvironmentAttributes.WATER_FOG_COLOR, 0x000F5C)
			.set(EnvironmentAttributes.FOG_COLOR, 0x241961)
			.set(EnvironmentAttributes.SKY_COLOR, OverworldBiomes.calculateSkyColor(this.temperature))
			.set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
			.set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(AbysmSoundEvents.MUSIC_OVERWORLD_THE_ENTWINED));
	}

	@Override
	public BiomeSpecialEffects.Builder createEffects() {
		return new BiomeSpecialEffects.Builder()
			.waterColor(0x000F5C);
	}

	@Override
	public void createGenerationSettings(BiomeGenerationSettings.Builder builder) {
		// place entanglers
	}

	@Override
	public MobSpawnSettings.Builder createSpawnSettings() {
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder()
			.addSpawn(
				MobCategory.WATER_CREATURE,
				1,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.RETICULATED_FLIPRAY, 1, 2)
			);

		BiomeDefaultFeatures.commonSpawns(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// I don't know enough about worldgen to do this
	}
}
