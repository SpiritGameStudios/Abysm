package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import dev.spiritstudios.abysm.world.level.storage.loot.AbysmLootTableModifications;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AbysmFishingLootTableProvider extends SimpleFabricLootTableProvider {

	protected final Map<ResourceKey<LootTable>, LootTable.Builder> lootTables;

	public AbysmFishingLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup, LootContextParamSets.FISHING);
		this.lootTables = new HashMap<>();
	}

	public void generate() {
		addLootTable(
			AbysmLootTableModifications.FLORAL_REEF_JUNK,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.add(LootItem.lootTableItem(AbysmBlocks.ANTENNAE_PLANT).setWeight(3))
						.add(LootItem.lootTableItem(AbysmBlocks.ROSY_BLOOMSHROOM).setWeight(10))
						.add(LootItem.lootTableItem(AbysmBlocks.SUNNY_BLOOMSHROOM).setWeight(10))
						.add(LootItem.lootTableItem(AbysmBlocks.MAUVE_BLOOMSHROOM).setWeight(10))
						.add(LootItem.lootTableItem(AbysmBlocks.ROSEBLOOM_PETALS).setWeight(15))
						.add(LootItem.lootTableItem(AbysmBlocks.SUNBLOOM_PETALS).setWeight(15))
						.add(LootItem.lootTableItem(AbysmBlocks.MALLOWBLOOM_PETALS).setWeight(15))
				)
		);

		addLootTable(
			AbysmLootTableModifications.FLORAL_REEF_FISH,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.add(LootItem.lootTableItem(AbysmItems.SMALL_FLORAL_FISH).setWeight(60))
						.add(LootItem.lootTableItem(AbysmItems.BIG_FLORAL_FISH).setWeight(25))
				)
		);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
		generate();

		for (Map.Entry<ResourceKey<LootTable>, LootTable.Builder> entry : lootTables.entrySet()) {
			ResourceKey<LootTable> registryKey = entry.getKey();

			biConsumer.accept(registryKey, entry.getValue());
		}
	}

	public void addLootTable(ResourceKey<LootTable> registryKey, LootTable.Builder lootTable) {
		this.lootTables
			.put(registryKey, lootTable);
	}
}
