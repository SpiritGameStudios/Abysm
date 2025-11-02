package dev.spiritstudios.abysm.loot;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.mixin.loottable.LootItemAccessor;
import dev.spiritstudios.abysm.mixin.loottable.LootPool$BuilderAccessor;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.List;

public class AbysmLootTableModifications {

	public static final ResourceKey<LootTable> FISHING_JUNK_GAMEPLAY = BuiltInLootTables.FISHING_JUNK;
	public static final ResourceKey<LootTable> FISHING_TREASURE_GAMEPLAY = BuiltInLootTables.FISHING_TREASURE;
	public static final ResourceKey<LootTable> FISHING_FISH_GAMEPLAY = BuiltInLootTables.FISHING_FISH;

	public static final ResourceKey<LootTable> FLORAL_REEF_JUNK = keyOf("gameplay/fishing/junk/floral_reef");
	public static final ResourceKey<LootTable> FLORAL_REEF_FISH = keyOf("gameplay/fishing/fish/floral_reef");

	public static void init() {
		registerModify(FISHING_JUNK_GAMEPLAY, ((builder, source, wrapperLookup) -> {
			HolderLookup<Biome> biomeLookup = wrapperLookup.lookupOrThrow(Registries.BIOME);

			builder.modifyPools(pool -> {
				// find the pool containing lily pads (by default this is the only pool, but check anyway just in case other mods add bonus pools)
				if (poolContainsItemAsDirectChild(pool, Items.LILY_PAD)) {
					// add items to pool
					pool.add(biomeDependantLootTable(biomeLookup, FLORAL_REEF_JUNK, 50, AbysmBiomes.FLORAL_REEF));
				}
			});
		}));

		registerModify(FISHING_FISH_GAMEPLAY, (builder, lootTableSource, wrapperLookup) -> {
			HolderLookup<Biome> biomeLookup = wrapperLookup.lookupOrThrow(Registries.BIOME);

			builder.modifyPools(pool -> {
				if (poolContainsItemAsDirectChild(pool, Items.COD)) {
					pool.conditionally(biomeCondition(biomeLookup.getOrThrow(AbysmBiomes.FLORAL_REEF)).invert().build());
				}
			});

			builder.pool(
				LootPool.lootPool()
					.add(NestedLootTable.lootTableReference(FLORAL_REEF_FISH))
					.conditionally(biomeCondition(biomeLookup.getOrThrow(AbysmBiomes.FLORAL_REEF)).build())
					.build()
			);
		});
	}

	@SafeVarargs
	public static NestedLootTable.Builder<?> biomeDependantLootTable(HolderLookup<Biome> impl, ResourceKey<LootTable> lootTableKey, int weight, ResourceKey<Biome>... biomeKeys) {
		ImmutableList.Builder<Holder.Reference<Biome>> biomes = ImmutableList.builder();
		for (ResourceKey<Biome> biomeKey : biomeKeys) {
			impl.get(biomeKey).ifPresent(biomes::add);
		}

		HolderSet<Biome> entryList = HolderSet.direct(biomes.build());

		return NestedLootTable.lootTableReference(lootTableKey)
			.when(
				LocationCheck.checkLocation(
					LocationPredicate.Builder.location()
						.setBiomes(entryList)
				)
			)
			.setWeight(weight);
	}

	public static LootItemCondition.Builder biomeCondition(Holder<Biome> biome) {
		return LocationCheck.checkLocation(
			LocationPredicate.Builder.inBiome(biome)
		);
	}

	public static boolean poolContainsItemAsDirectChild(LootPool.Builder pool, Item item) {
		List<LootPoolEntryContainer> currentEntries = ((LootPool$BuilderAccessor) pool).getEntries().build();
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);

		for (LootPoolEntryContainer entry : currentEntries) {
			if (entry instanceof LootItem itemEntry) {
				Holder<Item> ire = ((LootItemAccessor) itemEntry).getItem();

				if (ire.is(id)) {
					return true;
				}
			}
		}

		return false;
	}

	public static void registerModify(ResourceKey<LootTable> registryKey, ModifyNoKey modifyNoKey) {
		registerModify((key, builder, source, wrapperLookup) -> {
			if (registryKey.equals(key)) {
				modifyNoKey.modify(builder, source, wrapperLookup);
			}
		});
	}

	public static void registerModify(LootTableEvents.Modify modify) {
		LootTableEvents.MODIFY.register(modify);
	}

	private static ResourceKey<LootTable> keyOf(String id) {
		return ResourceKey.create(Registries.LOOT_TABLE, Abysm.id(id));
	}

	@FunctionalInterface
	public interface ModifyNoKey {
		void modify(LootTable.Builder builder, LootTableSource lootTableSource, HolderLookup.Provider wrapperLookup);
	}
}
