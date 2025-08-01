package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AbysmEntityLootTableProvider extends FabricEntityLootTableProvider {
	public AbysmEntityLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		register(
			AbysmEntityTypes.SMALL_FLORAL_FISH,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(AbysmItems.SMALL_FLORAL_FISH)
							.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
				)
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(Items.BONE_MEAL))
						.conditionally(RandomChanceLootCondition.builder(0.05F))
				)
		);

		register(
			AbysmEntityTypes.BIG_FLORAL_FISH,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(AbysmItems.BIG_FLORAL_FISH)
							.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
				)
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(Items.BONE_MEAL))
						.conditionally(RandomChanceLootCondition.builder(0.05F))
				)
		);
	}
}
