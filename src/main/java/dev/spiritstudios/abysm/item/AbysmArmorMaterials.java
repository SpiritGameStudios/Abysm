package dev.spiritstudios.abysm.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.entity.attribute.AbysmAttribute;
import dev.spiritstudios.abysm.mixin.accessors.ArmorMaterialsAccessor;
import dev.spiritstudios.abysm.registry.tags.AbysmItemTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.List;

/**
 * @author Ampflower
 **/
public final class AbysmArmorMaterials {

	/**
	 * @author SkyNotTheLimit (do I even need this line?)
	 * Echo please help me fix this terrible code
	 */
	public static final ImmutableMap<EquipmentSlot, TagKey<Item>> DIVING_SUITS_BY_SLOT = Util.make(ImmutableMap.<EquipmentSlot, TagKey<Item>>builder(), builder -> {
		builder.put(EquipmentSlot.HEAD, AbysmItemTags.DIVING_SUIT_HELMETS);
		builder.put(EquipmentSlot.CHEST, AbysmItemTags.DIVING_SUIT_CHESTPLATES);
		builder.put(EquipmentSlot.LEGS, AbysmItemTags.DIVING_SUIT_LEGGINGS);
		builder.put(EquipmentSlot.FEET, AbysmItemTags.DIVING_SUIT_BOOTS);
	}).build();

	public static final ArmorMaterial DIVING_SUIT = new ArmorMaterial(
			20,
			ArmorMaterialsAccessor.invokeCreateDefenseMap(2, 4, 5, 2, 4),
			12,
			SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
			0.F,
			0.F,
			ItemTags.REPAIRS_IRON_ARMOR,
			AbysmEquipmentAssetKeys.DIVING_SUIT
	);

	public static Item.Settings applyWithModel(
			final Item.Settings settings,
			final ArmorMaterial material,
			final EquipmentType type,
			final AbysmAttribute... attributes
	) {
		// Frankly going to be more of a helper than not.
		return settings
				.armor(material, type)
				.attributeModifiers(attributesComponent(material, type, attributes));
	}

	public static Item.Settings applyWithoutModel(
			final Item.Settings settings,
			final ArmorMaterial material,
			final EquipmentType type,
			final AbysmAttribute... attributes
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

	public static AttributeModifiersComponent attributesComponent(
			final ArmorMaterial material,
			final EquipmentType type,
			final AbysmAttribute... attributes
	) {
		final ImmutableList.Builder<AttributeModifiersComponent.Entry> builder = ImmutableList.builder();

		builder.addAll(material.createAttributeModifiers(type).modifiers());

		final AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot());

		for (final var attribute : attributes) {
			builder.add(attribute.toEntry(slot));
		}

		return new AttributeModifiersComponent(builder.build());
	}

}
