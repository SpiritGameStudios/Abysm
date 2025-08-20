package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.effect.AbysmStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class AbysmPotions {
	public static final RegistryEntry<Potion> BLUE = register(
		"blue", new Potion("abysm.blue", new StatusEffectInstance(AbysmStatusEffects.BLUE, 3600))
	);
	public static final RegistryEntry<Potion> LONG_BLUE = register(
		"long_blue", new Potion("abysm.blue", new StatusEffectInstance(AbysmStatusEffects.BLUE, 9600))
	);

	private static RegistryEntry<Potion> register(String id, Potion statusEffect) {
		return Registry.registerReference(Registries.POTION, keyOf(id), statusEffect);
	}

	private static RegistryKey<Potion> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.POTION, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
