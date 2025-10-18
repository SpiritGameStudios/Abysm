package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.component.AbysmFoodComponents;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.entity.attribute.AbysmAttribute;
import dev.spiritstudios.abysm.entity.attribute.AbysmEntityAttributes;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;

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
		new Item.Settings()
			.maxCount(1)
			.armor(ArmorMaterials.TURTLE_SCUTE, EquipmentType.BOOTS)
			.attributeModifiers(AbysmAttribute.ofAdd(
				AbysmEntityAttributes.SWIMMING_SPEED,
				"flippers_swimming_speed_multiplier",
				0.05
			).toComponent(AttributeModifierSlot.FEET))
	);

	public static final Item DIVING_BOOTS = register(
		"diving_boots",
		AbysmArmorMaterials.applyWithModel(
			new Item.Settings().maxCount(1),
			AbysmArmorMaterials.DIVING_SUIT,
			EquipmentType.BOOTS,
			AbysmAttribute.ofAdd(EntityAttributes.OXYGEN_BONUS, "diving_boots_oxygen", 3.0)
		)
	);

	public static final Item DIVING_LEGGINGS = register(
		"diving_leggings",
		AbysmArmorMaterials.applyWithModel(
			new Item.Settings().maxCount(1),
			AbysmArmorMaterials.DIVING_SUIT,
			EquipmentType.LEGGINGS,
			AbysmAttribute.ofAdd(EntityAttributes.OXYGEN_BONUS, "diving_leggings_oxygen", 3.0)
		)
	);

	public static final Item DIVING_CHESTPLATE = register(
		"diving_chestplate",
		AbysmArmorMaterials.applyWithModel(
			new Item.Settings().maxCount(1),
			AbysmArmorMaterials.DIVING_SUIT,
			EquipmentType.CHESTPLATE,
			AbysmAttribute.ofAdd(EntityAttributes.OXYGEN_BONUS, "diving_chestplate_oxygen", 3.0)
		)
	);

	public static final Item DIVING_HELMET = register(
		"diving_helmet",
		AbysmArmorMaterials.applyWithoutModel(
			new Item.Settings().maxCount(1),
			AbysmArmorMaterials.DIVING_SUIT,
			EquipmentType.HELMET,
			AbysmAttribute.ofAdd(EntityAttributes.OXYGEN_BONUS, "diving_helmet_oxygen", 3.0)
		)
	);

	public static final Item HARPOON = register(
		"harpoon",
		HarpoonItem::new,
		new Item.Settings()
			.maxCount(1)
			.maxDamage(250)
			.enchantable(1)
			.rarity(Rarity.RARE)
	);

	public static final Item SMALL_FLORAL_FISH = register(
		"small_floral_fish",
		new Item.Settings().food(AbysmFoodComponents.SMALL_FLORAL_FISH)
	);

	public static final Item BIG_FLORAL_FISH = register(
		"big_floral_fish",
		new Item.Settings().food(AbysmFoodComponents.BIG_FLORAL_FISH)
	);

	public static final Item SMALL_FLORAL_FISH_BUCKET = register(
		"small_floral_fish_bucket",
		settings -> new EntityBucketItem(AbysmEntityTypes.SMALL_FLORAL_FISH, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, settings),
		new Item.Settings()
			.maxCount(1)
			.component(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT)
	);

	public static final Item BIG_FLORAL_FISH_BUCKET = register(
		"big_floral_fish_bucket",
		settings -> new EntityBucketItem(AbysmEntityTypes.BIG_FLORAL_FISH, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, settings),
		new Item.Settings().maxCount(1).component(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT)
	);

	public static final Item PADDLEFISH_BUCKET = register(
		"paddlefish_bucket",
		settings -> new EntityBucketItem(AbysmEntityTypes.PADDLEFISH, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, settings),
		new Item.Settings().maxCount(1).component(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT)
	);

	public static final Item SMALL_FLORAL_FISH_SPAWN_EGG = register(
		"floral_fish_small_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.SMALL_FLORAL_FISH, settings)
	);

	public static final Item BIG_FLORAL_FISH_SPAWN_EGG = register(
		"floral_fish_big_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.BIG_FLORAL_FISH, settings)
	);

	public static final Item PADDLEFISH_SPAWN_EGG = register(
		"paddlefish_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.PADDLEFISH, settings)
	);

	public static final Item SNAPPER_SPAWN_EGG = register(
		"snapper_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.SNAPPER, settings)
	);

	public static final Item GUP_GUP_SPAWN_EGG = register(
		"gup_gup_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.GUP_GUP, settings)
	);

	public static final Item AROWANA_MAGICII_SPAWN_EGG = register(
		"arowana_magicii_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.AROWANA_MAGICII, settings)
	);

	public static final Item SYNTHETHIC_ORNIOTHOPE_SPAWN_EGG = register(
		"synthethic_orniothope_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.SYNTHETHIC_ORNIOTHOPE, settings)
	);

	public static final Item BLOOMRAY_SPAWN_EGG = register(
		"bloomray_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.BLOOMRAY, settings)
	);

	public static final Item ELECTRIC_OOGLY_BOOGLY_SPAWN_EGG = register(
		"electric_oogly_boogly_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY, settings)
	);

	public static final Item MAN_O_WAR_SPAWN_EGG = register(
		"man_o_war_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.MAN_O_WAR, settings)
	);

	public static final Item MYSTERIOUS_BLOB_SPAWN_EGG = register(
		"mysterious_blob_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.MYSTERIOUS_BLOB, settings)
	);

	public static final Item LECTORFIN_SPAWN_EGG = register(
		"lectorfin_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.LECTORFIN, settings)
	);

	public static final Item RETICULATED_FLIPRAY_SPAWN_EGG = register(
		"reticulated_flipray_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.RETICULATED_FLIPRAY, settings)
	);

	public static final Item SKELETON_SHARK_SPAWN_EGG = register(
		"skeleton_shark_spawn_egg",
		settings -> new SpawnEggItem(AbysmEntityTypes.SKELETON_SHARK, settings)
	);

	public static final Item MUSIC_DISC_RENAISSANCE = register(
		"music_disc_renaissance",
		new Item.Settings()
			.maxCount(1)
			.rarity(Rarity.RARE)
			.jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Abysm.id("renaissance")))
	);

	private static RegistryKey<Item> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.ITEM, Abysm.id(id));
	}

	private static Item register(String id, Function<Item.Settings, Item> factory) {
		return register(keyOf(id), factory, new Item.Settings());
	}

	private static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(keyOf(id), factory, settings);
	}

	private static Item register(String id, Item.Settings settings) {
		return register(keyOf(id), Item::new, settings);
	}

	private static Item register(String id) {
		return register(keyOf(id), Item::new, new Item.Settings());
	}

	private static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory) {
		return register(key, factory, new Item.Settings());
	}

	private static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) {
			blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		}

		return Registry.register(Registries.ITEM, key, item);
	}

	private static Function<Item.Settings, Item> createBlockItemWithUniqueName(Block block) {
		return settings -> new BlockItem(block, settings.useItemPrefixedTranslationKey());
	}

	public static void init() {
		// NO-OP
	}
}
