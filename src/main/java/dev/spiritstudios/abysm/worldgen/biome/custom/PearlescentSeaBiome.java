package dev.spiritstudios.abysm.worldgen.biome.custom;

import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public final class PearlescentSeaBiome extends AbysmBiome {

	public PearlescentSeaBiome() {
		super(AbysmBiomes.PEARLESCENT_SEA, 0.5F, true, 0.5F);
	}

	@Override
	public BiomeEffects.Builder createEffects() {
		return new BiomeEffects.Builder()
			.waterColor(0xDAB9ED)
			.waterFogColor(0xB796E3)
			.fogColor(0xF2E8FF)
			.skyColor(OverworldBiomeCreator.getSkyColor(this.temperature))
			.moodSound(BiomeMoodSound.CAVE)
			.music(MusicType.createIngameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_PEARLESCENT_SEA));
	}

	@Override
	public void createGenerationSettings(GenerationSettings.LookupBackedBuilder builder) {
		// weird unlocking mechanism I have no idea
	}

	@Override
	public SpawnSettings.Builder createSpawnSettings() {
		SpawnSettings.Builder builder = new SpawnSettings.Builder()
			.spawn(
				SpawnGroup.WATER_CREATURE,
				1,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.RETICULATED_FLIPRAY, 1, 2)
			);

		DefaultBiomeFeatures.addBatsAndMonsters(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// I don't know enough about worldgen to do this
	}
}
