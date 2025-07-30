package dev.spiritstudios.abysm.worldgen.structure.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;

public class AbysmStructurePools {

	public static final RegistryKey<StructurePool> BASIC_RUIN = of("deep_sea_ruins/basic_ruin");

	public static void bootstrap(Registerable<StructurePool> registerable) {
		RegistryEntryLookup<StructureProcessorList> processorListLookup = registerable.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
		RegistryEntryLookup<StructurePool> templatePoolLookup = registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);

		RegistryEntry<StructurePool> emptyPool = templatePoolLookup.getOrThrow(StructurePools.EMPTY);

		registerable.register(
			BASIC_RUIN,
			new StructurePool(
				emptyPool,
				ImmutableList.of(Pair.of(
					StructurePoolElement.ofSingle(Abysm.id("deep_sea_ruins/basic_ruin").toString()),
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
