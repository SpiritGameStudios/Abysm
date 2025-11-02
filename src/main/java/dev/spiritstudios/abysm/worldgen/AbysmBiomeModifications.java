package dev.spiritstudios.abysm.worldgen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.sounds.Musics;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import java.util.function.Predicate;

public final class AbysmBiomeModifications {
	public static void init() {
		// Kind of beaches, right?

		Predicate<BiomeSelectionContext> seaside = BiomeSelectors.includeByKey(
			Biomes.BEACH,
			Biomes.SNOWY_BEACH,
			Biomes.STONY_SHORE,
			Biomes.RIVER
		).or(BiomeSelectors.tag(BiomeTags.PLAYS_UNDERWATER_MUSIC));

		BiomeModifications.create(Abysm.id("seasides"))
			.add(ModificationPhase.ADDITIONS, seaside, context -> {
				context.getEffects().setMusic(Musics.createGameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_SEASIDE));
			});
	}
}
