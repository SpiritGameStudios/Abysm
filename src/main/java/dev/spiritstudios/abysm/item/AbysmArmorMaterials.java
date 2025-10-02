package dev.spiritstudios.abysm.item;

import com.google.common.collect.Maps;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Pair;

import java.util.Map;

/**
 * @author Ampflower
 **/
public final class AbysmArmorMaterials {
	public static final RegistryKey<EquipmentAsset> DIVING_SUIT_ASSET = registerAsset("diving");

	public static final ArmorMaterial DIVING_SUIT = new ArmorMaterial(
			20,
			vanillaCopy$defenseMap(2, 4, 5, 2, 4),
			12,
			SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
			0.F,
			0.F,
			ItemTags.REPAIRS_IRON_ARMOR,
			DIVING_SUIT_ASSET
	);

	@SafeVarargs
	public static Item.Settings applyWithModel(
			final Item.Settings settings,
			final ArmorMaterial material,
			final EquipmentType type,
			final Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>... attributes
	) {
		// Frankly going to be more of a helper than not.
		return settings
				.armor(material, type)
				.attributeModifiers(attributesComponent(material, type, attributes));
	}

	@SafeVarargs
	public static Item.Settings applyWithoutModel(
			final Item.Settings settings,
			final ArmorMaterial material,
			final EquipmentType type,
			final Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>... attributes
	) {
		return settings.maxDamage(type.getMaxDamage(material.durability()))
				.attributeModifiers(attributesComponent(material, type, attributes))
				.enchantable(material.enchantmentValue())
				.component(
						DataComponentTypes.EQUIPPABLE,
						EquippableComponent.builder(type.getEquipmentSlot())
								.equipSound(material.equipSound())
								.build()
				)
				.repairable(material.repairIngredient());
	}

	@SafeVarargs
	public static AttributeModifiersComponent attributesComponent(
			final ArmorMaterial material,
			final EquipmentType type,
			final Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>... attributes
	) {
		AttributeModifiersComponent attributesComponent = material.createAttributeModifiers(type);

		final var slot = AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot());

		for (final var attribute : attributes) {
			attributesComponent = attributesComponent.with(attribute.getLeft(), attribute.getRight(), slot);
		}

		return attributesComponent;
	}

	public static Map<EquipmentType, Integer> vanillaCopy$defenseMap(
			final int boots,
			final int leggings,
			final int chestplate,
			final int helmet,
			final int body
	) {
		return Maps.newEnumMap(Map.of(
				EquipmentType.BOOTS, boots,
				EquipmentType.LEGGINGS, leggings,
				EquipmentType.CHESTPLATE, chestplate,
				EquipmentType.HELMET, helmet,
				EquipmentType.BODY, body
		));
	}

	private static RegistryKey<EquipmentAsset> registerAsset(String abysmAsset) {
		return RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Abysm.id(abysmAsset));
	}
}
