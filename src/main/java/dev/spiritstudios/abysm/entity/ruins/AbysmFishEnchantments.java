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
import net.minecraft.util.Identifier;

public class AbysmFishEnchantments {

	public static final Identifier NONE_ID = Abysm.id("none");
	public static final Identifier JAW_ID = Abysm.id("jaw");
	public static final Identifier SHELL_ID = Abysm.id("shell");
	public static final Identifier JET_ID = Abysm.id("jet");
	public static final Identifier OBFUSCATED_ID = Abysm.id("obfuscated");

	public static final RegistryKey<FishEnchantment> NONE = of(NONE_ID);
	public static final RegistryKey<FishEnchantment> JAW = of(JAW_ID);
	public static final RegistryKey<FishEnchantment> SHELL = of(SHELL_ID);
	public static final RegistryKey<FishEnchantment> JET = of(JET_ID);
	public static final RegistryKey<FishEnchantment> OBFUSCATED = of(OBFUSCATED_ID);

	/*
	@SuppressWarnings("SameParameterValue")
	private static RegistryKey<FishEnchantment> of(String path) {
		return of(Abysm.id(path));
	}
	 */

	private static RegistryKey<FishEnchantment> of(Identifier id) {
		return RegistryKey.of(AbysmRegistryKeys.FISH_ENCHANTMENT, id);
	}

	private static void register(Registerable<FishEnchantment> registerable, RegistryKey<FishEnchantment> key, Identifier id, RegistryEntry<EntityAttribute> attribute, double value) {
		registerable.register(key, FishEnchantment.builder()
			.id(id)
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
		registerable.register(NONE, FishEnchantment.builder().id(NONE_ID).build());

		register(registerable, JAW, JAW_ID, EntityAttributes.ATTACK_DAMAGE, 2);
		register(registerable, SHELL, SHELL_ID, EntityAttributes.ARMOR, 10);
		register(registerable, JET, JET_ID,  EntityAttributes.MOVEMENT_SPEED, 0.7);
		register(registerable, OBFUSCATED, OBFUSCATED_ID, EntityAttributes.LUCK, 1);
	}
}
