package dev.spiritstudios.abysm.worldgen.structure.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.structure.processor.AbysmStructureProcessorLists;
import dev.spiritstudios.specter.api.worldgen.SpecterStructurePoolElements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class AbysmStructurePools {
	public static final ResourceKey<StructureTemplatePool> BASIC_RUIN = of("deep_sea_ruins/basic_ruin");

	public static void bootstrap(BootstrapContext<StructureTemplatePool> registerable) {
		HolderGetter<StructureProcessorList> processorListLookup = registerable.lookup(Registries.PROCESSOR_LIST);
		HolderGetter<StructureTemplatePool> templatePoolLookup = registerable.lookup(Registries.TEMPLATE_POOL);

		Holder<StructureTemplatePool> emptyPool = templatePoolLookup.getOrThrow(Pools.EMPTY);

		Holder<StructureProcessorList> ruinsDegradation = processorListLookup.getOrThrow(AbysmStructureProcessorLists.DEEP_SEA_RUINS_DEGRADATION);

		registerable.register(
			BASIC_RUIN,
			new StructureTemplatePool(
				emptyPool,
				ImmutableList.of(Pair.of(
					SpecterStructurePoolElements.ofProcessedSingle(Abysm.id("deep_sea_ruins/basic_ruin"), ruinsDegradation),
					1
				)),
				StructureTemplatePool.Projection.RIGID
			)
		);
	}

	protected static ResourceKey<StructureTemplatePool> of(String id) {
		return ResourceKey.create(Registries.TEMPLATE_POOL, Abysm.id(id));
	}
}
