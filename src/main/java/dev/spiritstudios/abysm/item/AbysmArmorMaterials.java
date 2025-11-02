package dev.spiritstudios.abysm.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.entity.attribute.AbysmAttribute;
import dev.spiritstudios.abysm.mixin.accessors.ArmorMaterialsAccessor;
import dev.spiritstudios.abysm.registry.tags.AbysmItemTags;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;

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
			ArmorMaterialsAccessor.invokeMakeDefense(2, 4, 5, 2, 4),
			12,
			SoundEvents.ARMOR_EQUIP_NETHERITE,
			0.F,
			0.F,
			ItemTags.REPAIRS_IRON_ARMOR,
			AbysmEquipmentAssetKeys.DIVING_SUIT
	);

	public static Item.Properties applyWithModel(
			final Item.Properties settings,
			final ArmorMaterial material,
			final ArmorType type,
			final AbysmAttribute... attributes
	) {
		// Frankly going to be more of a helper than not.
		return settings
				.humanoidArmor(material, type)
				.attributes(attributesComponent(material, type, attributes));
	}

	public static Item.Properties applyWithoutModel(
			final Item.Properties settings,
			final ArmorMaterial material,
			final ArmorType type,
			final AbysmAttribute... attributes
	) {
		return settings.durability(type.getDurability(material.durability()))
				.attributes(attributesComponent(material, type, attributes))
				.enchantable(material.enchantmentValue())
				.component(
						DataComponents.EQUIPPABLE,
						Equippable.builder(type.getSlot())
								.setEquipSound(material.equipSound())
								.build()
				)
				.repairable(material.repairIngredient());
	}

	public static ItemAttributeModifiers attributesComponent(
			final ArmorMaterial material,
			final ArmorType type,
			final AbysmAttribute... attributes
	) {
		final ImmutableList.Builder<ItemAttributeModifiers.Entry> builder = ImmutableList.builder();

		builder.addAll(material.createAttributes(type).modifiers());

		final EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());

		for (final var attribute : attributes) {
			builder.add(attribute.toEntry(slot));
		}

		return new ItemAttributeModifiers(builder.build());
	}

}
