package dev.spiritstudios.abysm.world.item;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.entity.effect.AbysmStatusEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class AbysmPotions {
	public static final Holder<Potion> BLUE = register(
		"blue", new Potion("abysm.blue", new MobEffectInstance(AbysmStatusEffects.BLUE, 3600))
	);
	public static final Holder<Potion> LONG_BLUE = register(
		"long_blue", new Potion("abysm.blue", new MobEffectInstance(AbysmStatusEffects.BLUE, 9600))
	);

	private static Holder<Potion> register(String id, Potion statusEffect) {
		return Registry.registerForHolder(BuiltInRegistries.POTION, keyOf(id), statusEffect);
	}

	private static ResourceKey<Potion> keyOf(String id) {
		return ResourceKey.create(Registries.POTION, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
