package dev.spiritstudios.abysm.worldgen.biome.custom;

import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public final class GlowingCavesBiome extends AbysmBiome {

	public GlowingCavesBiome() {
		super(AbysmBiomes.GLOWING_CAVES, 0.2F, true, 0.5F);
	}

	@Override
	public BiomeSpecialEffects.Builder createEffects() {
		return new BiomeSpecialEffects.Builder()
			.waterColor(0x0B0B21)
			.waterFogColor(0x252529)
			.fogColor(0xDBDBFF)
			.skyColor(OverworldBiomes.calculateSkyColor(this.temperature))
			.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
			.backgroundMusic(Musics.createGameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_GLOWING_CAVES));
	}

	@Override
	public void createGenerationSettings(BiomeGenerationSettings.Builder builder) {
		// ???
	}

	@Override
	public MobSpawnSettings.Builder createSpawnSettings() {
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();

		BiomeDefaultFeatures.commonSpawns(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// I don't know enough about worldgen to do this
	}
}
