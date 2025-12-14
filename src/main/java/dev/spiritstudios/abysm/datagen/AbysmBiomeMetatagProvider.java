package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiomes;
import dev.spiritstudios.spectre.api.core.registry.metatag.MetatagsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class AbysmBiomeMetatagProvider extends MetatagsProvider<Biome> {
	public AbysmBiomeMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(dataOutput, lookupProvider, Registries.BIOME);
	}

	@Override
	protected void addMetatags(HolderLookup.Provider provider) {
		builder(AbysmMetatags.PRESSURE)
			.put(AbysmBiomes.DEEP_SEA_RUINS, 0.05f);
//			.put(AbysmBiomes.GLOWING_CAVES, 0.3f)
//			.put(AbysmBiomes.THE_ENTWINED, 0.375f)
//			.put(AbysmBiomes.INKDEPTH_REALM, 0.5F);
	}

	@Override
	public @NotNull String getName() {
		return "Biome Metatags";
	}
}
