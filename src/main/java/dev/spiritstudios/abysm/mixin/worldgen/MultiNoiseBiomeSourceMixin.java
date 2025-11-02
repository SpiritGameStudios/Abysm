package dev.spiritstudios.abysm.mixin.worldgen;

import com.google.common.collect.Streams;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.terraformersmc.biolith.impl.biome.BiomeCoordinator;
import com.terraformersmc.biolith.impl.compat.BiolithCompat;
import dev.spiritstudios.abysm.registry.tags.AbysmBiomeTags;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceMixin {
	@Unique
	@Nullable
	private List<Holder<Biome>> abysm$bonusBiomes = null;

	@ModifyReturnValue(method = "collectPossibleBiomes", at = @At("RETURN"))
	private Stream<Holder<Biome>> addBiomes(Stream<Holder<Biome>> original) {
		if (BiolithCompat.COMPAT_DATAGEN) {
			// do not add biomes during datagen
			return original;
		}

		// init bonus biomes if not yet initialised
		synchronized (this) {
			if (this.abysm$bonusBiomes == null) {
				// try to get registry from where biolith stores it
				Optional<Registry<Biome>> biomeLookupOptional = BiomeCoordinator.getBiomeLookup();
				if (biomeLookupOptional.isEmpty()) {
					return original;
				} else {
					Registry<Biome> biomeLookup = biomeLookupOptional.get();

					// make a list from the stream then remake the stream from that list, to avoid operating upon the same stream twice
					List<Holder<Biome>> streamedBiomes = original.toList();
					original = streamedBiomes.stream();

					// add bonus biomes to list
					List<Holder<Biome>> bonusBiomes = new ArrayList<>();
					// check if the biome source contains any biomes that can spawn the deep sea ruins, and if so add it to the list
					boolean canSpawnDeepSeaRuins = streamedBiomes.stream().anyMatch(biome -> biome.is(AbysmBiomeTags.DEEP_SEA_RUINS_HAS_STRUCTURE));
					if (canSpawnDeepSeaRuins) {
						biomeLookup.get(AbysmBiomes.DEEP_SEA_RUINS.location()).ifPresent(bonusBiomes::add);
					}

					// store bonus biomes
					this.abysm$bonusBiomes = bonusBiomes;
				}
			}
		}

		if (this.abysm$bonusBiomes.isEmpty()) {
			return original;
		} else {
			return Streams.concat(
				original,
				this.abysm$bonusBiomes.stream()
			).distinct();
		}
	}
}
