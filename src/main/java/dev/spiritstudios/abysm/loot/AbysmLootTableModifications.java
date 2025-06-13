package dev.spiritstudios.abysm.loot;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.mixin.ItemEntryAccessor;
import dev.spiritstudios.abysm.mixin.LootPoolBuilderAccessor;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.predicate.entity.LocationPredicate;
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

	public static final Identifier ID_LILY_PAD = Identifier.ofVanilla("lily_pad");

	public static void init() {
		registerModify(FISHING_JUNK_GAMEPLAY, ((builder, source, wrapperLookup) -> builder.modifyPools(pool -> {
			RegistryWrapper.Impl<Biome> impl = wrapperLookup.getOrThrow(RegistryKeys.BIOME);

			// find the pool containing lily pads (by default this is the only pool, but check anyway just in case other mods add bonus pools)
			if(poolContainsItemAsDirectChild(pool, ID_LILY_PAD)) {
				// add items to pool
				pool.with(biomeDependantLootTable(impl, FLORAL_REEF_JUNK, 50, AbysmBiomes.FLORAL_REEF));
			}
		})));
	}

	@SafeVarargs
	public static LootTableEntry.Builder<?> biomeDependantLootTable(RegistryWrapper.Impl<Biome> impl, RegistryKey<LootTable> lootTableKey, int weight, RegistryKey<Biome>... biomeKeys) {
		ImmutableList.Builder<RegistryEntry.Reference<Biome>> biomes = ImmutableList.builder();
		for(RegistryKey<Biome> biomeKey : biomeKeys) {
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

	public static boolean poolContainsItemAsDirectChild(LootPool.Builder pool, Identifier itemIdentifier) {
		List<LootPoolEntry> currentEntries = ((LootPoolBuilderAccessor)pool).getEntries().build();

		for(LootPoolEntry entry : currentEntries) {
			if(entry instanceof ItemEntry itemEntry) {
				RegistryEntry<Item> ire = ((ItemEntryAccessor)itemEntry).getItem();

				if(ire.matchesId(itemIdentifier)) {
					return true;
				}
			}
		}

		return false;
	}

	public static void registerModify(RegistryKey<LootTable> registryKey, ModifyNoKey modifyNoKey) {
		registerModify((key, builder, source, wrapperLookup) -> {
			if(registryKey.equals(key)) {
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
