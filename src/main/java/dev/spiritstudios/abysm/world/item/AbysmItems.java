package dev.spiritstudios.abysm.world.item;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.core.component.AbysmFoods;
import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.world.entity.attribute.AbysmAttribute;
import dev.spiritstudios.abysm.world.entity.attribute.AbysmEntityAttributes;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class AbysmItems {
	public static final Item LAPIS_BULB = register(
		"lapis_bulb",
		createBlockItemWithUniqueName(AbysmBlocks.GOLDEN_LAZULI_OREFURL)
	);

	public static final Item GOLD_LEAF = register(
		"gold_leaf"
	);

	public static final Item DREGLOAM_OOZEBALL = register(
		"dregloam_oozeball",
		OozeballItem::new
	);

	public static final Item FLIPPERS = register(
		"flippers",
		new Item.Properties()
			.stacksTo(1)
			.humanoidArmor(ArmorMaterials.TURTLE_SCUTE, ArmorType.BOOTS)
			.attributes(AbysmAttribute.ofAdd(
				AbysmEntityAttributes.SWIMMING_SPEED,
				"flippers_swimming_speed_multiplier",
				0.05
			).toComponent(EquipmentSlotGroup.FEET))
	);

	public static final Item DIVING_BOOTS = register(
		"diving_boots",
		AbysmArmorMaterials.applyWithModel(
			new Item.Properties().stacksTo(1),
			AbysmArmorMaterials.DIVING_SUIT,
			ArmorType.BOOTS,
			AbysmAttribute.ofAdd(Attributes.OXYGEN_BONUS, "diving_boots_oxygen", 3.0)
		)
	);

	public static final Item DIVING_LEGGINGS = register(
		"diving_leggings",
		AbysmArmorMaterials.applyWithModel(
			new Item.Properties().stacksTo(1),
			AbysmArmorMaterials.DIVING_SUIT,
			ArmorType.LEGGINGS,
			AbysmAttribute.ofAdd(Attributes.OXYGEN_BONUS, "diving_leggings_oxygen", 3.0)
		)
	);

	public static final Item DIVING_CHESTPLATE = register(
		"diving_chestplate",
		AbysmArmorMaterials.applyWithModel(
			new Item.Properties().stacksTo(1),
			AbysmArmorMaterials.DIVING_SUIT,
			ArmorType.CHESTPLATE,
			AbysmAttribute.ofAdd(Attributes.OXYGEN_BONUS, "diving_chestplate_oxygen", 3.0)
		)
	);

	public static final Item DIVING_HELMET = register(
		"diving_helmet",
		AbysmArmorMaterials.applyWithoutModel(
			new Item.Properties().stacksTo(1),
			AbysmArmorMaterials.DIVING_SUIT,
			ArmorType.HELMET,
			AbysmAttribute.ofAdd(Attributes.OXYGEN_BONUS, "diving_helmet_oxygen", 3.0)
		)
	);

	public static final Item HARPOON = register(
		"harpoon",
		HarpoonItem::new,
		new Item.Properties()
			.stacksTo(1)
			.durability(250)
			.enchantable(1)
			.rarity(Rarity.RARE)
	);

	public static final Item SMALL_FLORAL_FISH = register(
		"small_floral_fish",
		new Item.Properties().food(AbysmFoods.SMALL_FLORAL_FISH)
	);

	public static final Item BIG_FLORAL_FISH = register(
		"big_floral_fish",
		new Item.Properties().food(AbysmFoods.BIG_FLORAL_FISH)
	);

	public static final Item SMALL_FLORAL_FISH_BUCKET = register(
		"small_floral_fish_bucket",
		settings -> new MobBucketItem(AbysmEntityTypes.SMALL_FLORAL_FISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, settings),
		new Item.Properties()
			.stacksTo(1)
			.component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
	);

	public static final Item BIG_FLORAL_FISH_BUCKET = register(
		"big_floral_fish_bucket",
		settings -> new MobBucketItem(AbysmEntityTypes.BIG_FLORAL_FISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, settings),
		new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
	);

	public static final Item PADDLEFISH_BUCKET = register(
		"paddlefish_bucket",
		settings -> new MobBucketItem(AbysmEntityTypes.PADDLEFISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, settings),
		new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
	);

	public static final Item SMALL_FLORAL_FISH_SPAWN_EGG = register(
		"floral_fish_small_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.SMALL_FLORAL_FISH)
	);

	public static final Item BIG_FLORAL_FISH_SPAWN_EGG = register(
		"floral_fish_big_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.BIG_FLORAL_FISH)
	);

	public static final Item PADDLEFISH_SPAWN_EGG = register(
		"paddlefish_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.PADDLEFISH)
	);

	public static final Item SNAPPER_SPAWN_EGG = register(
		"snapper_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.SNAPPER)
	);

	public static final Item GUP_GUP_SPAWN_EGG = register(
		"gup_gup_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.GUP_GUP)
	);

	public static final Item AROWANA_MAGICII_SPAWN_EGG = register(
		"arowana_magicii_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.AROWANA_MAGICII)
	);

	public static final Item SYNTHETHIC_ORNIOTHOPE_SPAWN_EGG = register(
		"synthethic_orniothope_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.SYNTHETHIC_ORNIOTHOPE)
	);

	public static final Item BLOOMRAY_SPAWN_EGG = register(
		"bloomray_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.BLOOMRAY)
	);

	public static final Item ELECTRIC_OOGLY_BOOGLY_SPAWN_EGG = register(
		"electric_oogly_boogly_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY)
	);

	public static final Item MAN_O_WAR_SPAWN_EGG = register(
		"man_o_war_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.MAN_O_WAR)
	);

	public static final Item MYSTERIOUS_BLOB_SPAWN_EGG = register(
		"mysterious_blob_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.MYSTERIOUS_BLOB)
	);

	public static final Item LECTORFIN_SPAWN_EGG = register(
		"lectorfin_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.LECTORFIN)
	);

	public static final Item RETICULATED_FLIPRAY_SPAWN_EGG = register(
		"reticulated_flipray_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.RETICULATED_FLIPRAY)
	);

	public static final Item SKELETON_SHARK_SPAWN_EGG = register(
		"skeleton_shark_spawn_egg",
		SpawnEggItem::new,
		new Item.Properties().spawnEgg(AbysmEntityTypes.SKELETON_SHARK)
	);

	public static final Item MUSIC_DISC_RENAISSANCE = register(
		"music_disc_renaissance",
		new Item.Properties()
			.stacksTo(1)
			.rarity(Rarity.RARE)
			.jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Abysm.id("renaissance")))
	);

	private static ResourceKey<Item> keyOf(String id) {
		return ResourceKey.create(Registries.ITEM, Abysm.id(id));
	}

	private static Item register(String id, Function<Item.Properties, Item> factory) {
		return register(keyOf(id), factory, new Item.Properties());
	}

	private static Item register(String id, Function<Item.Properties, Item> factory, Item.Properties settings) {
		return register(keyOf(id), factory, settings);
	}

	private static Item register(String id, Item.Properties settings) {
		return register(keyOf(id), Item::new, settings);
	}

	private static Item register(String id) {
		return register(keyOf(id), Item::new, new Item.Properties());
	}

	private static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory) {
		return register(key, factory, new Item.Properties());
	}

	private static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties settings) {
		Item item = factory.apply(settings.setId(key));
		if (item instanceof BlockItem blockItem) {
			blockItem.registerBlocks(Item.BY_BLOCK, item);
		}

		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	private static Function<Item.Properties, Item> createBlockItemWithUniqueName(Block block) {
		return settings -> new BlockItem(block, settings.useItemDescriptionPrefix());
	}

	public static void init() {
		// NO-OP
	}
}
