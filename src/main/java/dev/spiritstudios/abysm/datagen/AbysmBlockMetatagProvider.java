package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.spectre.api.core.registry.metatag.MetatagsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class AbysmBlockMetatagProvider extends MetatagsProvider.BlockMetatagProvider {
	public AbysmBlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(dataOutput, registriesFuture);
	}

	@Override
	protected void addMetatags(HolderLookup.Provider wrapperLookup) {
		// FIXME
//		builder(SpectreMetatags.FLATTENABLE)
//			.put(OOZING_DREGLOAM, DREGLOAM.defaultBlockState());
	}

	@Override
	public String getName() {
		return "";
	}
}
