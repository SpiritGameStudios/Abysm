package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AbysmBlockLootTableProvider extends FabricBlockLootTableProvider {

	protected static final float[] SAPLING_DROP_CHANCE = new float[] {0.05F, 0.0625F, 0.083333336F, 0.1F};

	public AbysmBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);

		addLootForFamilies(
			AbysmBlockFamilies.FLOROPUMICE,
			AbysmBlockFamilies.FLOROPUMICE_BRICKS,
			AbysmBlockFamilies.FLOROPUMICE_TILES,
			AbysmBlockFamilies.SMOOTH_FLOROPUMICE,
			AbysmBlockFamilies.SMOOTH_FLOROPUMICE_BRICKS,
			AbysmBlockFamilies.CUT_SMOOTH_FLOROPUMICE
		);

		dropSelf(
			AbysmBlocks.POLISHED_FLOROPUMICE,
			AbysmBlocks.CHISLED_FLOROPUMICE,
			AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE,
			AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE,
			AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR,

			AbysmBlocks.ROSY_SPRIGS,
			AbysmBlocks.SUNNY_SPRIGS,
			AbysmBlocks.MAUVE_SPRIGS,

			AbysmBlocks.ROSY_BLOOMSHROOM,
			AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
			AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE,
			AbysmBlocks.ROSY_BLOOMSHROOM_CAP,

			AbysmBlocks.SUNNY_BLOOMSHROOM,
			AbysmBlocks.SUNNY_BLOOMSHROOM_STEM,
			AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE,
			AbysmBlocks.SUNNY_BLOOMSHROOM_CAP,

			AbysmBlocks.MAUVE_BLOOMSHROOM,
			AbysmBlocks.MAUVE_BLOOMSHROOM_STEM,
			AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE,
			AbysmBlocks.MAUVE_BLOOMSHROOM_CAP,

			AbysmBlocks.BLOOMSHROOM_GOOP,

			AbysmBlocks.BLOOMING_SODALITE_CROWN,
			AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
			AbysmBlocks.BLOOMING_MELILITE_CROWN,

			AbysmBlocks.WHITE_SCABIOSA,
			AbysmBlocks.ORANGE_SCABIOSA,
			AbysmBlocks.MAGENTA_SCABIOSA,
			AbysmBlocks.LIGHT_BLUE_SCABIOSA,
			AbysmBlocks.YELLOW_SCABIOSA,
			AbysmBlocks.LIME_SCABIOSA,
			AbysmBlocks.PINK_SCABIOSA,
			AbysmBlocks.GREY_SCABIOSA,
			AbysmBlocks.LIGHT_GREY_SCABIOSA,
			AbysmBlocks.CYAN_SCABIOSA,
			AbysmBlocks.PURPLE_SCABIOSA,
			AbysmBlocks.BLUE_SCABIOSA,
			AbysmBlocks.BROWN_SCABIOSA,
			AbysmBlocks.GREEN_SCABIOSA,
			AbysmBlocks.RED_SCABIOSA,
			AbysmBlocks.BLACK_SCABIOSA,

			AbysmBlocks.ANTENNAE_PLANT,

			AbysmBlocks.DREGLOAM,

			AbysmBlocks.SILT,
			AbysmBlocks.CHISELED_SILT,
			AbysmBlocks.CUT_SILT
		);

		forEach(this::addPottedPlantDrops,
			AbysmBlocks.POTTED_ROSY_SPRIGS,
			AbysmBlocks.POTTED_SUNNY_SPRIGS,
			AbysmBlocks.POTTED_MAUVE_SPRIGS,

			AbysmBlocks.POTTED_ROSY_BLOOMSHROOM,
			AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM,
			AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM,

			AbysmBlocks.POTTED_ANTENNAE_PLANT
		);

		this.addSilkTouchOrElseDrop(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		this.addSilkTouchOrElseDrop(AbysmBlocks.SUNBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		this.addSilkTouchOrElseDrop(AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);

		this.addPetaleavesDrop(AbysmBlocks.ROSEBLOOM_PETALEAVES, AbysmBlocks.ROSEBLOOM_PETALS, AbysmBlocks.ROSY_BLOOMSHROOM);
		this.addPetaleavesDrop(AbysmBlocks.SUNBLOOM_PETALEAVES, AbysmBlocks.SUNBLOOM_PETALS, AbysmBlocks.SUNNY_BLOOMSHROOM);
		this.addPetaleavesDrop(AbysmBlocks.MALLOWBLOOM_PETALEAVES, AbysmBlocks.MALLOWBLOOM_PETALS, AbysmBlocks.MAUVE_BLOOMSHROOM);

		forEach(this::addSegmentedDrop,
			AbysmBlocks.ROSEBLOOM_PETALS,
			AbysmBlocks.SUNBLOOM_PETALS,
			AbysmBlocks.MALLOWBLOOM_PETALS
		);

		this.addOrefurlDrop(AbysmBlocks.GOLDEN_LAZULI_OREFURL, true);
		this.addOrefurlDrop(AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT, false);

		this.addDrop(AbysmBlocks.OOZING_DREGLOAM, block -> this.dropsWithSilkTouch(
			AbysmBlocks.OOZING_DREGLOAM,
			this.addSurvivesExplosionCondition(
				AbysmBlocks.OOZING_DREGLOAM,
				LootTableEntry.builder(
					LootTable.builder()
						.pool(
							LootPool.builder().with(
								ItemEntry.builder(AbysmBlocks.DREGLOAM)
							)
						).pool(
							LootPool.builder().with(
								ItemEntry.builder(AbysmItems.DREGLOAM_OOZEBALL)
									.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
							)
						)
						.build()
				)
			)
		));
		this.addDrop(AbysmBlocks.DREGLOAM_OOZE, block -> this.drops(
				block, AbysmItems.DREGLOAM_OOZEBALL, UniformLootNumberProvider.create(2.0F, 4.0F)
			)
		);
		this.addDrop(AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE, block -> this.dropsWithSilkTouch(
				block,
				this.applyExplosionDecay(
					block,
					LootTableEntry.builder(
						LootTable.builder()
							.pool(
								LootPool.builder().with(
									ItemEntry.builder(Items.GOLD_NUGGET)
										.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
										.apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))
								)
							)
							.pool(
								LootPool.builder().with(
									ItemEntry.builder(Items.LAPIS_LAZULI)
										.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F)))
										.apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))
								)
							)
							.build()
					)
				)
			)
		);
	}

	private void forEach(Consumer<Block> consumer, Block... blocks) {
		for (Block block : blocks) {
			consumer.accept(block);
		}
	}

	private void dropSelf(Block... blocks) {
		forEach(this::addDrop, blocks);
	}

	private void addSilkTouchOrElseDrop(Block block, Block noSilkTouch) {
		this.addDrop(block, blck -> this.drops(blck, noSilkTouch));
	}

	private void addSegmentedDrop(Block block) {
		this.addDrop(block, this.segmentedDrops(block));
	}

	private void addPetaleavesDrop(Block petaleaves, Block petal, Block bloomshroom) {
		this.addDrop(petaleaves, this.petaleavesDrops(petaleaves, petal, bloomshroom, SAPLING_DROP_CHANCE));
	}

	private LootTable.Builder petaleavesDrops(Block petaleaves, Block petal, Block bloomshroom, float... saplingChance) {
		RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);

		return this.dropsWithSilkTouchOrShears(
				petaleaves,
				(this.addSurvivesExplosionCondition(petaleaves, ItemEntry.builder(bloomshroom)))
					.conditionally(TableBonusLootCondition.builder(impl.getOrThrow(Enchantments.FORTUNE), saplingChance))
			)
			.pool(
				LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1.0F))
					.conditionally(this.createWithoutShearsOrSilkTouchCondition())
					.with(
						(this.applyExplosionDecay(
							petaleaves, ItemEntry.builder(petal).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F)))
						))
					)
			);
	}

	private void addOrefurlDrop(Block orefurl, boolean isHead) {
		RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
		LootTable.Builder builder = LootTable.builder();

		if (isHead) {
			// add guaranteed bulb drop
			builder = builder.pool(LootPool.builder()
				.with(
					ItemEntry.builder(AbysmItems.LAPIS_BULB)
				)
			);
		} else {
			// add choice between bulbs and leaves
			builder = builder.pool(LootPool.builder()
				.with(
					ItemEntry.builder(AbysmItems.LAPIS_BULB)
				)
				.with(
					ItemEntry.builder(AbysmItems.GOLD_LEAF)
				)
			);

			// add chance for bonus bulbs
			builder = builder.pool(LootPool.builder()
				.with(
					ItemEntry.builder(AbysmItems.LAPIS_BULB)
						.conditionally(RandomChanceLootCondition.builder(0.5F))
						.apply(ApplyBonusLootFunction.binomialWithBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 0.25F, 2))
				)
			);

			// add chance for bonus leaves
			builder = builder.pool(
				LootPool.builder()
					.with(
						ItemEntry.builder(AbysmItems.GOLD_LEAF)
							.conditionally(RandomChanceLootCondition.builder(0.5F))
							.apply(ApplyBonusLootFunction.binomialWithBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 0.25F, 2))
					)
			);
		}

		this.addDrop(
			orefurl,
			this.applyExplosionDecay(orefurl, builder)
		);
	}

	private void addLootForFamilies(BlockFamily... families) {
		for (BlockFamily family : families) {
			addLootForFamily(family);
		}
	}

	private void addLootForFamily(BlockFamily family) {
		this.dropSelf(family.getBaseBlock());
		for (BlockFamily.Variant variant : BlockFamily.Variant.values()) {
			if (variant != BlockFamily.Variant.DOOR && variant != BlockFamily.Variant.SLAB) {
				Block block = family.getVariant(variant);
				if (block != null) {
					this.dropSelf(block);
				}
			}
		}
		Block door = family.getVariant(BlockFamily.Variant.DOOR);
		if (door != null) {
			this.addDrop(door, this.doorDrops(door));
		}
		Block slab = family.getVariant(BlockFamily.Variant.SLAB);
		if (slab != null) {
			this.addDrop(slab, this.slabDrops(slab));
		}
	}
}
