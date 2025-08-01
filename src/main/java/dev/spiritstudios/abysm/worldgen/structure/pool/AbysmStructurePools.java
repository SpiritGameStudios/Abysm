package dev.spiritstudios.abysm.worldgen.structure.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.structure.processor.AbysmStructureProcessorLists;
import dev.spiritstudios.specter.api.worldgen.SpecterStructurePoolElements;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;

public class AbysmStructurePools {
	public static final RegistryKey<StructurePool> BASIC_RUIN = of("deep_sea_ruins/basic_ruin");

	public static void bootstrap(Registerable<StructurePool> registerable) {
		RegistryEntryLookup<StructureProcessorList> processorListLookup = registerable.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
		RegistryEntryLookup<StructurePool> templatePoolLookup = registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);

		RegistryEntry<StructurePool> emptyPool = templatePoolLookup.getOrThrow(StructurePools.EMPTY);

		RegistryEntry<StructureProcessorList> ruinsDegradation = processorListLookup.getOrThrow(AbysmStructureProcessorLists.DEEP_SEA_RUINS_DEGRADATION);

		registerable.register(
			BASIC_RUIN,
			new StructurePool(
				emptyPool,
				ImmutableList.of(Pair.of(
					SpecterStructurePoolElements.ofProcessedSingle(Abysm.id("deep_sea_ruins/basic_ruin"), ruinsDegradation),
					1
				)),
				StructurePool.Projection.RIGID
			)
		);
	}

	protected static RegistryKey<StructurePool> of(String id) {
		return RegistryKey.of(RegistryKeys.TEMPLATE_POOL, Abysm.id(id));
	}
}
