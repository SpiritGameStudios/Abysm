package dev.spiritstudios.abysm.worldgen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.BiomeKeys;

import java.util.function.Predicate;

public final class AbysmBiomeModifications {
	public static void init() {
		// Kind of beaches, right?

		Predicate<BiomeSelectionContext> seaside = BiomeSelectors.includeByKey(
			BiomeKeys.BEACH,
			BiomeKeys.SNOWY_BEACH,
			BiomeKeys.STONY_SHORE,
			BiomeKeys.RIVER
		).or(BiomeSelectors.tag(BiomeTags.PLAYS_UNDERWATER_MUSIC));

		BiomeModifications.create(Abysm.id("seasides"))
			.add(ModificationPhase.ADDITIONS, seaside, context -> {
				context.getEffects().setMusic(MusicType.createIngameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_SEASIDE));
			});
	}
}
