package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AbysmBiomeMetatagProvider extends MetatagProvider<Biome> {
	public AbysmBiomeMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(dataOutput, Registries.BIOME, registriesFuture, PackOutput.Target.DATA_PACK);
	}

	@Override
	protected void configure(Consumer<MetatagProvider<Biome>.MetatagBuilder<?>> consumer, HolderLookup.Provider provider) {
		consumer.accept(
			create(AbysmBiomes.Metatags.PRESSURE)
				.put(AbysmBiomes.DEEP_SEA_RUINS, 0.05f)
				.put(AbysmBiomes.GLOWING_CAVES, 0.3f)
				.put(AbysmBiomes.THE_ENTWINED, 0.375f)
				.put(AbysmBiomes.INKDEPTH_REALM, 0.5F)
		);
	}
}
