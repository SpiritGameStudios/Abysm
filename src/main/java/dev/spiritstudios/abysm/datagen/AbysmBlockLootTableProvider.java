package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.world.level.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AbysmBlockLootTableProvider extends FabricBlockLootTableProvider {

	protected static final float[] SAPLING_DROP_CHANCE = new float[] {0.05F, 0.0625F, 0.083333336F, 0.1F};

	public AbysmBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		HolderGetter<Enchantment> enchantmentLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

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

			AbysmBlocks.SWEET_NECTARSAP,
			AbysmBlocks.SOUR_NECTARSAP,
			AbysmBlocks.BITTER_NECTARSAP,

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

		forEach(this::dropPottedContents,
			AbysmBlocks.POTTED_ROSY_SPRIGS,
			AbysmBlocks.POTTED_SUNNY_SPRIGS,
			AbysmBlocks.POTTED_MAUVE_SPRIGS,

			AbysmBlocks.POTTED_ROSY_BLOOMSHROOM,
			AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM,
			AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM,

			AbysmBlocks.POTTED_ANTENNAE_PLANT,

			AbysmBlocks.POTTED_OOZETRICKLE_FILAMENTS
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

		this.add(AbysmBlocks.OOZING_DREGLOAM, block -> this.createSilkTouchDispatchTable(
			AbysmBlocks.OOZING_DREGLOAM,
			this.applyExplosionCondition(
				AbysmBlocks.OOZING_DREGLOAM,
				NestedLootTable.inlineLootTable(
					LootTable.lootTable()
						.withPool(
							LootPool.lootPool().add(
								LootItem.lootTableItem(AbysmBlocks.DREGLOAM)
							)
						).withPool(
							LootPool.lootPool().add(
								LootItem.lootTableItem(AbysmItems.DREGLOAM_OOZEBALL)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
							)
						)
						.build()
				)
			)
		));
		this.add(AbysmBlocks.DREGLOAM_OOZE, block -> this.createSingleItemTableWithSilkTouch(
				block, AbysmItems.DREGLOAM_OOZEBALL, UniformGenerator.between(2.0F, 4.0F)
			)
		);
		this.add(AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE, block -> this.createSilkTouchDispatchTable(
				block,
				this.applyExplosionDecay(
					block,
					NestedLootTable.inlineLootTable(
						LootTable.lootTable()
							.withPool(
								LootPool.lootPool().add(
									LootItem.lootTableItem(Items.GOLD_NUGGET)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
										.apply(ApplyBonusCount.addOreBonusCount(enchantmentLookup.getOrThrow(Enchantments.FORTUNE)))
								)
							)
							.withPool(
								LootPool.lootPool().add(
									LootItem.lootTableItem(Items.LAPIS_LAZULI)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
										.apply(ApplyBonusCount.addOreBonusCount(enchantmentLookup.getOrThrow(Enchantments.FORTUNE)))
								)
							)
							.build()
					)
				)
			)
		);

		this.add(AbysmBlocks.OOZETRICKLE_FILAMENTS, this::createShearsOnlyDrop);
		this.add(AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS, block -> this.createDoublePlantWithSeedDrops(block, AbysmBlocks.OOZETRICKLE_FILAMENTS));

		this.addOrefurlDrop(AbysmBlocks.GOLDEN_LAZULI_OREFURL, true);
		this.addOrefurlDrop(AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT, false);

		this.dropSelf(AbysmBlocks.OOZETRICKLE_CORD);
		this.dropSelf(AbysmBlocks.OOZETRICKLE_LANTERN);
	}

	private void forEach(Consumer<Block> consumer, Block... blocks) {
		for (Block block : blocks) {
			consumer.accept(block);
		}
	}

	private void dropSelf(Block... blocks) {
		forEach(this::dropSelf, blocks);
	}

	private void addSilkTouchOrElseDrop(Block block, Block noSilkTouch) {
		this.add(block, blck -> this.createSingleItemTableWithSilkTouch(blck, noSilkTouch));
	}

	private void addSegmentedDrop(Block block) {
		this.add(block, this.createSegmentedBlockDrops(block));
	}

	private void addPetaleavesDrop(Block petaleaves, Block petal, Block bloomshroom) {
		this.add(petaleaves, this.petaleavesDrops(petaleaves, petal, bloomshroom, NORMAL_LEAVES_SAPLING_CHANCES));
	}

	private LootTable.Builder petaleavesDrops(Block petaleaves, Block petal, Block bloomshroom, float... saplingChance) {
		HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		return this.createSilkTouchOrShearsDispatchTable(
				petaleaves,
				(this.applyExplosionCondition(petaleaves, LootItem.lootTableItem(bloomshroom)))
					.when(BonusLevelTableCondition.bonusLevelFlatChance(impl.getOrThrow(Enchantments.FORTUNE), saplingChance))
			)
			.withPool(
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.when(this.doesNotHaveShearsOrSilkTouch())
					.add(
						(this.applyExplosionDecay(
							petaleaves, LootItem.lootTableItem(petal).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
						))
					)
			);
	}

	private void addOrefurlDrop(Block orefurl, boolean isHead) {
		HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		LootTable.Builder builder = LootTable.lootTable();

		if (isHead) {
			// add guaranteed bulb drop
			builder = builder.withPool(LootPool.lootPool()
				.add(
					LootItem.lootTableItem(AbysmItems.LAPIS_BULB)
				)
			);
		} else {
			// add choice between bulbs and leaves
			builder = builder.withPool(LootPool.lootPool()
				.add(
					LootItem.lootTableItem(AbysmItems.LAPIS_BULB)
				)
				.add(
					LootItem.lootTableItem(AbysmItems.GOLD_LEAF)
				)
			);

			// add chance for bonus bulbs
			builder = builder.withPool(LootPool.lootPool()
				.add(
					LootItem.lootTableItem(AbysmItems.LAPIS_BULB)
						.when(LootItemRandomChanceCondition.randomChance(0.5F))
						.apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE), 0.25F, 2))
				)
			);

			// add chance for bonus leaves
			builder = builder.withPool(
				LootPool.lootPool()
					.add(
						LootItem.lootTableItem(AbysmItems.GOLD_LEAF)
							.when(LootItemRandomChanceCondition.randomChance(0.5F))
							.apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE), 0.25F, 2))
					)
			);
		}

		this.add(
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
				Block block = family.get(variant);
				if (block != null) {
					this.dropSelf(block);
				}
			}
		}
		Block door = family.get(BlockFamily.Variant.DOOR);
		if (door != null) {
			this.add(door, this.createDoorTable(door));
		}
		Block slab = family.get(BlockFamily.Variant.SLAB);
		if (slab != null) {
			this.add(slab, this.createSlabItemTable(slab));
		}
	}
}
