package dev.spiritstudios.abysm.entity.effect;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class AbysmStatusEffects {

	public static final RegistryEntry<StatusEffect> BLUE = register("blue", new BlueEffect(StatusEffectCategory.NEUTRAL, 0x3F5FFF).fadeTicks(20, 30, 30));
	public static final RegistryEntry<StatusEffect> SALINATION = register("salination", new SalinationEffect(StatusEffectCategory.HARMFUL, 0xA1E182).fadeTicks(20, 30, 30));

	private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
		return Registry.registerReference(Registries.STATUS_EFFECT, keyOf(id), statusEffect);
	}

	private static RegistryKey<StatusEffect> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.STATUS_EFFECT, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
