package dev.spiritstudios.abysm.entity.effect;

import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.registry.advancement.AbysmCriteria;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class SalinationEffect extends StatusEffect {

	public static final int BRINE_CONTACT_EFFECT_TIME = 60;
	private static final float CONVULSION_STRENGTH_PER_LEVEL = 0.2F;

	protected SalinationEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		RegistryEntry<DamageType> damageType = AbysmDamageTypes.getOrThrow(world, AbysmDamageTypes.SALINATION);
		entity.damage(world, new DamageSource(damageType), 2);

		if (!entity.getType().isIn(AbysmEntityTypeTags.NO_SALINATION_CONVULSING_MOBS))
			convulse(world, entity, amplifier);

		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = BRINE_CONTACT_EFFECT_TIME >> amplifier;
		return i == 0 || duration % i == 0;
	}

	public static boolean hasSalinationEffect(LivingEntity livingEntity) {
		return livingEntity.getActiveStatusEffects().containsKey(AbysmStatusEffects.SALINATION);
	}

	private static void convulse(ServerWorld world, LivingEntity entity, int amplifier) {
		float convulsionStrength = (amplifier + 1) * CONVULSION_STRENGTH_PER_LEVEL;
		Random random = world.getRandom();

		double x = MathHelper.nextBetween(random, -1, 1);
		double z = MathHelper.nextBetween(random, -1, 1);

		Vec3d velocity = new Vec3d(x, 1, z).normalize();
		velocity = velocity.multiply(convulsionStrength);
		velocity = velocity.add(entity.getVelocity());

		entity.velocityDirty = true;
        entity.setVelocity(velocity);
	}

	public static void tryGrantHeroBrineCriterion(ServerPlayerEntity player, LivingEntity entity, StatusEffectInstance instance) {
		if (!entity.isAffectedBySplashPotions() || !entity.canHaveStatusEffect(instance))
			return;

		if (!hasSalinationEffect(entity) || entity == player)
			return;

		boolean invertedHealingAndHarm = entity.getType().isIn(EntityTypeTags.INVERTED_HEALING_AND_HARM);

        if (instance.getEffectType().equals(invertedHealingAndHarm ? StatusEffects.INSTANT_DAMAGE : StatusEffects.INSTANT_HEALTH))
            AbysmCriteria.HERO_BRINE.trigger(player);
    }

}
