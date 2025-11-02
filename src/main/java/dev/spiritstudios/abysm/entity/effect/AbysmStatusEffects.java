package dev.spiritstudios.abysm.entity.effect;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AbysmStatusEffects {

	public static final Holder<MobEffect> BLUE = register("blue", new BlueEffect(MobEffectCategory.NEUTRAL, 0x3F5FFF).setBlendDuration(20, 30, 30));

	private static Holder<MobEffect> register(String id, MobEffect statusEffect) {
		return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, keyOf(id), statusEffect);
	}

	private static ResourceKey<MobEffect> keyOf(String id) {
		return ResourceKey.create(Registries.MOB_EFFECT, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
