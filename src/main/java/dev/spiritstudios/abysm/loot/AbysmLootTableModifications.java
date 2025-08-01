package dev.spiritstudios.abysm.loot;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.mixin.loottable.ItemEntryAccessor;
import dev.spiritstudios.abysm.mixin.loottable.LootPoolBuilderAccessor;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class AbysmLootTableModifications {

	public static final RegistryKey<LootTable> FISHING_JUNK_GAMEPLAY = LootTables.FISHING_JUNK_GAMEPLAY;
	public static final RegistryKey<LootTable> FISHING_TREASURE_GAMEPLAY = LootTables.FISHING_TREASURE_GAMEPLAY;
	public static final RegistryKey<LootTable> FISHING_FISH_GAMEPLAY = LootTables.FISHING_FISH_GAMEPLAY;

	public static final RegistryKey<LootTable> FLORAL_REEF_JUNK = keyOf("gameplay/fishing/junk/floral_reef");
	public static final RegistryKey<LootTable> FLORAL_REEF_FISH = keyOf("gameplay/fishing/fish/floral_reef");

	public static void init() {
		registerModify(FISHING_JUNK_GAMEPLAY, ((builder, source, wrapperLookup) -> {
			RegistryWrapper<Biome> biomeLookup = wrapperLookup.getOrThrow(RegistryKeys.BIOME);

			builder.modifyPools(pool -> {
				// find the pool containing lily pads (by default this is the only pool, but check anyway just in case other mods add bonus pools)
				if (poolContainsItemAsDirectChild(pool, Items.LILY_PAD)) {
					// add items to pool
					pool.with(biomeDependantLootTable(biomeLookup, FLORAL_REEF_JUNK, 50, AbysmBiomes.FLORAL_REEF));
				}
			});
		}));

		registerModify(FISHING_FISH_GAMEPLAY, (builder, lootTableSource, wrapperLookup) -> {
			RegistryWrapper<Biome> biomeLookup = wrapperLookup.getOrThrow(RegistryKeys.BIOME);

			builder.modifyPools(pool -> {
				if (poolContainsItemAsDirectChild(pool, Items.COD)) {
					pool.conditionally(biomeCondition(biomeLookup.getOrThrow(AbysmBiomes.FLORAL_REEF)).invert().build());
				}
			});

			builder.pool(
				LootPool.builder()
					.with(LootTableEntry.builder(FLORAL_REEF_FISH))
					.conditionally(biomeCondition(biomeLookup.getOrThrow(AbysmBiomes.FLORAL_REEF)).build())
					.build()
			);
		});
	}

	@SafeVarargs
	public static LootTableEntry.Builder<?> biomeDependantLootTable(RegistryWrapper<Biome> impl, RegistryKey<LootTable> lootTableKey, int weight, RegistryKey<Biome>... biomeKeys) {
		ImmutableList.Builder<RegistryEntry.Reference<Biome>> biomes = ImmutableList.builder();
		for (RegistryKey<Biome> biomeKey : biomeKeys) {
			impl.getOptional(biomeKey).ifPresent(biomes::add);
		}

		RegistryEntryList<Biome> entryList = RegistryEntryList.of(biomes.build());

		return LootTableEntry.builder(lootTableKey)
			.conditionally(
				LocationCheckLootCondition.builder(
					LocationPredicate.Builder.create()
						.biome(entryList)
				)
			)
			.weight(weight);
	}

	public static LootCondition.Builder biomeCondition(RegistryEntry<Biome> biome) {
		return LocationCheckLootCondition.builder(
			LocationPredicate.Builder.createBiome(biome)
		);
	}

	public static boolean poolContainsItemAsDirectChild(LootPool.Builder pool, Item item) {
		List<LootPoolEntry> currentEntries = ((LootPoolBuilderAccessor) pool).abysm$getEntries().build();
		Identifier id = Registries.ITEM.getId(item);

		for (LootPoolEntry entry : currentEntries) {
			if (entry instanceof ItemEntry itemEntry) {
				RegistryEntry<Item> ire = ((ItemEntryAccessor) itemEntry).abysm$getItem();

				if (ire.matchesId(id)) {
					return true;
				}
			}
		}

		return false;
	}

	public static void registerModify(RegistryKey<LootTable> registryKey, ModifyNoKey modifyNoKey) {
		registerModify((key, builder, source, wrapperLookup) -> {
			if (registryKey.equals(key)) {
				modifyNoKey.modify(builder, source, wrapperLookup);
			}
		});
	}

	public static void registerModify(LootTableEvents.Modify modify) {
		LootTableEvents.MODIFY.register(modify);
	}

	private static RegistryKey<LootTable> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.LOOT_TABLE, Abysm.id(id));
	}

	@FunctionalInterface
	public interface ModifyNoKey {
		void modify(LootTable.Builder builder, LootTableSource lootTableSource, RegistryWrapper.WrapperLookup wrapperLookup);
	}
}
