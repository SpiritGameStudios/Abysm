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

	public static final int FLOWER_CONTACT_EFFECT_DURATION = 40;

	protected SalinationEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		RegistryEntry<DamageType> damageType = AbysmDamageTypes.getOrThrow(world, AbysmDamageTypes.SALINATION);
		DamageSource source = new DamageSource(damageType, entity);

		entity.damage(world, source, 2);
		entity.updateLimbs(true);

		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = FLOWER_CONTACT_EFFECT_DURATION >> amplifier;
		return i == 0 || duration % i == 0;
	}

}
