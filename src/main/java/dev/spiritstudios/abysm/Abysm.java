package dev.spiritstudios.abysm;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmFoliagePlacerTypes;
import dev.spiritstudios.abysm.registry.AbysmTrunkPlacerTypes;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.specter.api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Abysm implements ModInitializer {
    public static final String MODID = "abysm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
		AbysmBlocks.init();

		RegistryHelper.registerFields(
			Registries.TRUNK_PLACER_TYPE, RegistryHelper.fixGenerics(TrunkPlacerType.class),
			AbysmTrunkPlacerTypes.class,
			MODID
		);

		RegistryHelper.registerFields(
			Registries.FOLIAGE_PLACER_TYPE, RegistryHelper.fixGenerics(FoliagePlacerType.class),
			AbysmFoliagePlacerTypes.class,
			MODID
		);

		AbysmBiomes.addAllToGenerator();
    }

	public static Identifier id(String path) {
		return Identifier.of(MODID, path);
	}
}
