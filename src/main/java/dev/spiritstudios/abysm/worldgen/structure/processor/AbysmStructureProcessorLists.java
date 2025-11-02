package dev.spiritstudios.abysm.worldgen.structure.processor;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import java.util.Arrays;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class AbysmStructureProcessorLists {
	public static final ResourceKey<StructureProcessorList> DEEP_SEA_RUINS_DEGRADATION = of("deep_sea_ruins_degradation");

	public static void bootstrap(BootstrapContext<StructureProcessorList> registerable) {
		HolderGetter<Block> blockLookup = registerable.lookup(Registries.BLOCK);

		registerable.register(
			DEEP_SEA_RUINS_DEGRADATION,
			of(
				new BlockRotProcessor(blockLookup.getOrThrow(AbysmBlockTags.DEEP_SEA_RUINS_REPLACEABLE), 0.85F),
				rules(
					replace(AbysmBlocks.FLOROPUMICE_BRICKS, AbysmBlocks.CRACKED_FLOROPUMICE_BRICKS, 0.25F),
					replace(AbysmBlocks.FLOROPUMICE_TILES, AbysmBlocks.CRACKED_FLOROPUMICE_TILES, 0.25F),
					replace(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS, AbysmBlocks.CRACKED_SMOOTH_FLOROPUMICE_BRICKS, 0.25F),
					replace(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE, AbysmBlocks.CRACKED_CUT_SMOOTH_FLOROPUMICE, 0.25F),
					replace(AbysmBlocks.OOZETRICKLE_LANTERN, Blocks.WATER, 0.25F)
				),
				new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)
			)
		);
	}

	private static ProcessorRule replace(Block toReplace, Block replaceWith, float probability) {
		return new ProcessorRule(
			new RandomBlockMatchTest(toReplace, probability),
			AlwaysTrueTest.INSTANCE,
			replaceWith.defaultBlockState()
		);
	}

	private static RuleProcessor rules(ProcessorRule... rules) {
		return new RuleProcessor(Arrays.asList(rules));
	}

	private static StructureProcessorList of(StructureProcessor... processors) {
		return new StructureProcessorList(Arrays.asList(processors));
	}

	protected static ResourceKey<StructureProcessorList> of(String id) {
		return ResourceKey.create(Registries.PROCESSOR_LIST, Abysm.id(id));
	}
}
