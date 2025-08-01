package dev.spiritstudios.abysm.worldgen.structure.processor;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.structure.processor.ProtectedBlocksStructureProcessor;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;

import java.util.Arrays;

public class AbysmStructureProcessorLists {
	public static final RegistryKey<StructureProcessorList> DEEP_SEA_RUINS_DEGRADATION = of("deep_sea_ruins_degradation");

	public static void bootstrap(Registerable<StructureProcessorList> registerable) {
		RegistryEntryLookup<Block> blockLookup = registerable.getRegistryLookup(RegistryKeys.BLOCK);

		registerable.register(
			DEEP_SEA_RUINS_DEGRADATION,
			of(
				new BlockRotStructureProcessor(blockLookup.getOrThrow(AbysmBlockTags.DEEP_SEA_RUINS_REPLACEABLE), 0.75F),
				rules(
					replace(Blocks.SOUL_LANTERN, Blocks.WATER, 0.25F)
				),
				new ProtectedBlocksStructureProcessor(BlockTags.FEATURES_CANNOT_REPLACE)
			)
		);
	}

	private static StructureProcessorRule replace(Block toReplace, Block replaceWith, float probability) {
		return new StructureProcessorRule(
			new RandomBlockMatchRuleTest(toReplace, probability),
			AlwaysTrueRuleTest.INSTANCE,
			replaceWith.getDefaultState()
		);
	}

	private static RuleStructureProcessor rules(StructureProcessorRule... rules) {
		return new RuleStructureProcessor(Arrays.asList(rules));
	}

	private static StructureProcessorList of(StructureProcessor... processors) {
		return new StructureProcessorList(Arrays.asList(processors));
	}

	protected static RegistryKey<StructureProcessorList> of(String id) {
		return RegistryKey.of(RegistryKeys.PROCESSOR_LIST, Abysm.id(id));
	}
}
