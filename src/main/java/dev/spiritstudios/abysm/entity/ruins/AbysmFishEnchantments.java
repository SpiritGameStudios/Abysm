package dev.spiritstudios.abysm.entity.ruins;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public class AbysmFishEnchantments {
	public static final RegistryKey<FishEnchantment> NONE = ofKey("none");
	public static final RegistryKey<FishEnchantment> JAW = ofKey("jaw");
	public static final RegistryKey<FishEnchantment> SHELL = ofKey("shell");
	public static final RegistryKey<FishEnchantment> JET = ofKey("jet");
	public static final RegistryKey<FishEnchantment> OBFUSCATED = ofKey("obfuscated");

	private static RegistryKey<FishEnchantment> ofKey(String key) {
		return RegistryKey.of(AbysmRegistryKeys.FISH_ENCHANTMENT, Abysm.id(key));
	}

	private static void register(Registerable<FishEnchantment> registerable, RegistryKey<FishEnchantment> key,RegistryEntry<EntityAttribute> attribute, double value) {
		registerable.register(key, FishEnchantment.builder()
			.id(key.getValue())
			.add(
				attribute,
				new EntityAttributeModifier(
					key.getValue(),
					value,
					EntityAttributeModifier.Operation.ADD_VALUE
				)
			).build());
	}

	public static void bootstrap(Registerable<FishEnchantment> registerable) {
		registerable.register(NONE, FishEnchantment.builder().id(NONE.getValue()).build());

		register(registerable, JAW, EntityAttributes.ATTACK_DAMAGE, 2);
		register(registerable, SHELL, EntityAttributes.ARMOR, 10);
		register(registerable, JET, EntityAttributes.MOVEMENT_SPEED, 0.7);
		register(registerable, OBFUSCATED, EntityAttributes.LUCK, 1);
	}
}
