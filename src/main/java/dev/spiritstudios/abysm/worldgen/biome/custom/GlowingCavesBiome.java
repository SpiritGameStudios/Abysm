package dev.spiritstudios.abysm.worldgen.biome.custom;

import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public final class GlowingCavesBiome extends AbysmBiome {

	public GlowingCavesBiome() {
		super(AbysmBiomes.GLOWING_CAVES, 0.2F, true, 0.5F);
	}

	@Override
	public BiomeEffects.Builder createEffects() {
		return new BiomeEffects.Builder()
			.waterColor(0x0B0B21)
			.waterFogColor(0x252529)
			.fogColor(0xDBDBFF)
			.skyColor(OverworldBiomeCreator.getSkyColor(this.temperature))
			.moodSound(BiomeMoodSound.CAVE)
			.music(MusicType.createIngameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_GLOWING_CAVES));
	}

	@Override
	public void createGenerationSettings(GenerationSettings.LookupBackedBuilder builder) {
		// ???
	}

	@Override
	public SpawnSettings.Builder createSpawnSettings() {
		SpawnSettings.Builder builder = new SpawnSettings.Builder();

		DefaultBiomeFeatures.addBatsAndMonsters(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// I don't know enough about worldgen to do this
	}
}
