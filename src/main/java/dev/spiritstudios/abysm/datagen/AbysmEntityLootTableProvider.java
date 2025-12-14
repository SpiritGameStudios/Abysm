package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AbysmEntityLootTableProvider extends FabricEntityLootTableProvider {
	public AbysmEntityLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		registerBasicFishDrops(AbysmEntityTypes.SMALL_FLORAL_FISH, 0.05F, AbysmItems.SMALL_FLORAL_FISH);
		registerBasicFishDrops(AbysmEntityTypes.BIG_FLORAL_FISH, 0.3F, AbysmItems.BIG_FLORAL_FISH);
		registerBasicFishDrops(AbysmEntityTypes.PADDLEFISH, 0.05F, null);
		registerBasicFishDrops(AbysmEntityTypes.LECTORFIN, 0.05F, null);
		registerBasicFishDrops(AbysmEntityTypes.SNAPPER, 0.05F, null);
		registerBasicFishDrops(AbysmEntityTypes.GUP_GUP, 0.025F, null);
		registerBasicFishDrops(AbysmEntityTypes.AROWANA_MAGICII, 0.05F, null);
	}

	public <T extends Entity> void registerBasicFishDrops(EntityType<T> entityType, float bonemealDropChance, @Nullable Item fishItem) {
		LootTable.Builder builder = LootTable.lootTable();

		if (fishItem != null) {
			builder.withPool(
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(fishItem)
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
			);
		}

		builder.withPool(
			LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.BONE_MEAL))
				.when(LootItemRandomChanceCondition.randomChance(bonemealDropChance))
		);

		add(
			entityType,
			builder
		);
	}
}
