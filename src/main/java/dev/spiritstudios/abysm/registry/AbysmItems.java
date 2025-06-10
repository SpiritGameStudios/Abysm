package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class AbysmItems {
    public static final Item FLIPPERS = register(
            "flippers",
            new Item.Settings()
				.maxCount(1)
				.armor(ArmorMaterials.TURTLE_SCUTE, EquipmentType.BOOTS)
				.attributeModifiers(AttributeModifiersComponent.builder().add(
						AbysmEntityAttributes.SWIMMING_SPEED,
						new EntityAttributeModifier(
							Abysm.id("flippers_swimming_speed_multiplier"),
							0.05,
							EntityAttributeModifier.Operation.ADD_VALUE
						),
						AttributeModifierSlot.FEET
					).build())
    );

	public static final Item NOOPRAH = register( // you can change the field name lol
		"harpoon",
		HarpoonItem::new,
		new Item.Settings()
			.maxCount(1).rarity(Rarity.RARE)
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

    public static void init() {
        // NO-OP
    }
}
