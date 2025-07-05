package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.loot.AbysmLootTableModifications;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AbysmFishingLootTableProvider extends SimpleFabricLootTableProvider {

	protected final Map<RegistryKey<LootTable>, LootTable.Builder> lootTables;

	public AbysmFishingLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(output, registryLookup, LootContextTypes.FISHING);
		this.lootTables = new HashMap<>();
	}

	public void generate() {
		addLootTable(
			AbysmLootTableModifications.FLORAL_REEF_JUNK,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.with(ItemEntry.builder(AbysmBlocks.ANTENNAE_PLANT).weight(3))
						.with(ItemEntry.builder(AbysmBlocks.ROSY_BLOOMSHROOM).weight(10))
						.with(ItemEntry.builder(AbysmBlocks.SUNNY_BLOOMSHROOM).weight(10))
						.with(ItemEntry.builder(AbysmBlocks.MAUVE_BLOOMSHROOM).weight(10))
						.with(ItemEntry.builder(AbysmBlocks.ROSEBLOOM_PETALS).weight(15))
						.with(ItemEntry.builder(AbysmBlocks.SUNBLOOM_PETALS).weight(15))
						.with(ItemEntry.builder(AbysmBlocks.MALLOWBLOOM_PETALS).weight(15))
				)
		);
	}

	@Override
	public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> biConsumer) {
		generate();

		for (Map.Entry<RegistryKey<LootTable>, LootTable.Builder> entry : lootTables.entrySet()) {
			RegistryKey<LootTable> registryKey = entry.getKey();

			biConsumer.accept(registryKey, entry.getValue());
		}
	}

	public void addLootTable(RegistryKey<LootTable> registryKey, LootTable.Builder lootTable) {
		this.lootTables
			.put(registryKey, lootTable);
	}
}
