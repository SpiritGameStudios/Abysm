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

public final class InkdepthRealmBiome extends AbysmBiome {

	public InkdepthRealmBiome() {
		super(AbysmBiomes.INKDEPTH_REALM, 0.25F, true, 0.5F);
	}

	@Override
	public BiomeSpecialEffects.Builder createEffects() {
		return new BiomeSpecialEffects.Builder()
			.waterColor(0x111112)
			.waterFogColor(0x111112)
			.fogColor(0x111112)
			.skyColor(OverworldBiomes.calculateSkyColor(this.temperature))
			.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
			.backgroundMusic(Musics.createGameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_INKDEPTH_REALM));
	}

	@Override
	public void createGenerationSettings(BiomeGenerationSettings.Builder builder) {
		// ???
	}

	@Override
	public MobSpawnSettings.Builder createSpawnSettings() {
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder()
			.addSpawn(
				MobCategory.WATER_CREATURE,
				1,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.SKELETON_SHARK, 1, 1)
			);

		// spawn blobabo but only 1

		BiomeDefaultFeatures.commonSpawns(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// I don't know enough about worldgen to do this
	}
}
