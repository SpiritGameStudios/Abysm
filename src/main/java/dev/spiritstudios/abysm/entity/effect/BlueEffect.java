package dev.spiritstudios.abysm.entity.effect;

import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class BlueEffect extends StatusEffect {

	protected BlueEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			if (!player.isSpectator()) {
				player.getAbilities().flying = false;
				player.sendAbilitiesUpdate();
			}
		}

		return super.applyUpdateEffect(world, entity, amplifier);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	public static boolean hasBlueEffect(LivingEntity livingEntity) {
		return livingEntity.getActiveStatusEffects().containsKey(AbysmStatusEffects.BLUE);
	}

	public static boolean isBlue(LivingEntity livingEntity) {
		return hasBlueEffect(livingEntity) || livingEntity instanceof LivingEntityDuck duck && duck.abysm$isBlue();
	}
}
