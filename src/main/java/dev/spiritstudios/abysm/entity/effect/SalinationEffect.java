package dev.spiritstudios.abysm.entity.effect;

import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;

public class SalinationEffect extends StatusEffect {

	public static final int BRINE_CONTACT_EFFECT_DURATION = 30;

	protected SalinationEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		RegistryEntry<DamageType> damageType = AbysmDamageTypes.getOrThrow(world, AbysmDamageTypes.SALINATION);
		entity.damage(world, new DamageSource(damageType, entity), 2);
		// passing in the entity as the attacker causes random "convulsions"--as if being shot by skeletons from many angles

		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = BRINE_CONTACT_EFFECT_DURATION >> amplifier;
		return i == 0 || duration % i == 0;
	}

}
