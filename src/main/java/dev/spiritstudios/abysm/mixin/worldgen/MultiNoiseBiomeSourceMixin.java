package dev.spiritstudios.abysm.mixin.worldgen;

import com.google.common.collect.Streams;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.terraformersmc.biolith.impl.biome.BiomeCoordinator;
import com.terraformersmc.biolith.impl.compat.BiolithCompat;
import dev.spiritstudios.abysm.registry.tags.AbysmBiomeTags;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
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
	private List<RegistryEntry<Biome>> bonusBiomes = null;

	@ModifyReturnValue(method = "biomeStream", at = @At("RETURN"))
	private Stream<RegistryEntry<Biome>> addBiomesToStream(Stream<RegistryEntry<Biome>> original) {
		if(BiolithCompat.COMPAT_DATAGEN) {
			// do not add biomes during datagen
			return original;
		}

		// init bonus biomes if not yet initialised
		synchronized (this) {
			if (this.bonusBiomes == null) {
				// try to get registry from where biolith stores it
				Optional<Registry<Biome>> biomeLookupOptional = BiomeCoordinator.getBiomeLookup();
				if(biomeLookupOptional.isEmpty()) {
					return original;
				} else {
					Registry<Biome> biomeLookup = biomeLookupOptional.get();

					// make a list from the stream then remake the stream from that list, to avoid operating upon the same stream twice
					List<RegistryEntry<Biome>> streamedBiomes = original.toList();
					original = streamedBiomes.stream();

					// add bonus biomes to list
					List<RegistryEntry<Biome>> bonusBiomes = new ArrayList<>();
					// check if the biome source contains any biomes that can spawn the deep sea ruins, and if so add it to the list
					boolean canSpawnDeepSeaRuins = streamedBiomes.stream().anyMatch(biome -> biome.isIn(AbysmBiomeTags.DEEP_SEA_RUINS_HAS_STRUCTURE));
					if(canSpawnDeepSeaRuins) {
						biomeLookup.getEntry(AbysmBiomes.DEEP_SEA_RUINS.getValue()).ifPresent(bonusBiomes::add);
					}

					// store bonus biomes
					this.bonusBiomes = bonusBiomes;
				}
			}
		}

		if(this.bonusBiomes.isEmpty()) {
			return original;
		} else {
			return Streams.concat(
				original,
				this.bonusBiomes.stream()
			).distinct();
		}
	}
}
