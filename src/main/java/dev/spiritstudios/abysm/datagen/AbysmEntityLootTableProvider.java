package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AbysmEntityLootTableProvider extends FabricEntityLootTableProvider {
	public AbysmEntityLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
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
		LootTable.Builder builder = LootTable.builder();

		if (fishItem != null) {
			builder.pool(
				LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1.0F))
					.with(ItemEntry.builder(fishItem)
						.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
			);
		}

		builder.pool(
			LootPool.builder()
				.rolls(ConstantLootNumberProvider.create(1.0F))
				.with(ItemEntry.builder(Items.BONE_MEAL))
				.conditionally(RandomChanceLootCondition.builder(bonemealDropChance))
		);

		register(
			entityType,
			builder
		);
	}
}
