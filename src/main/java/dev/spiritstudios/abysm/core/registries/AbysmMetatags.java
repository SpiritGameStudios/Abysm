package dev.spiritstudios.abysm.core.registries;

import com.mojang.serialization.Codec;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
import dev.spiritstudios.spectre.api.core.registry.SpectreRegistries;
import dev.spiritstudios.spectre.api.core.registry.metatag.MetatagKey;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;

public final class AbysmMetatags {
	public static final MetatagKey<Biome, Float> PRESSURE = register(
		"pressure",
		new MetatagKey.Builder<>(
			Registries.BIOME,
			Codec.floatRange(0F, 1F)
		).build()
	);

	public static final MetatagKey<EntityType<?>, EcosystemData> ECOSYSTEM_DATA = register(
		"ecosystem_data",
		new MetatagKey.Builder<>(
			Registries.ENTITY_TYPE,
			EcosystemData.CODEC
		).build()
	);

	private static <K, V> MetatagKey<K, V> register(String id, MetatagKey<K, V> key) {
		return Registry.register(
			SpectreRegistries.METATAG,
			Abysm.id(id),
			key
		);
	}

	public static void init() {
		// NO-OP
	}
}
