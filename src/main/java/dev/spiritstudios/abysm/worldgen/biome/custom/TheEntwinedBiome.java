package dev.spiritstudios.abysm.worldgen.biome.custom;

import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public final class TheEntwinedBiome extends AbysmBiome {

	public TheEntwinedBiome() {
		super(AbysmBiomes.THE_ENTWINED, 0.25F, true, 0.5F);
	}

	@Override
	public BiomeSpecialEffects.Builder createEffects() {
		return new BiomeSpecialEffects.Builder()
			.waterColor(0x000F5C)
			.waterFogColor(0x241961)
			.fogColor(0xC0C9ff)
			.skyColor(OverworldBiomes.calculateSkyColor(this.temperature))
			.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
			.backgroundMusic(Musics.createGameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_THE_ENTWINED));
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
