package dev.spiritstudios.abysm.entity.effect;

import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class BlueEffect extends MobEffect {

	protected BlueEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
		if (entity instanceof Player player) {
			if (!player.isSpectator()) {
				player.getAbilities().flying = false;
				player.onUpdateAbilities();
			}
		}

		return super.applyEffectTick(world, entity, amplifier);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	public static boolean hasBlueEffect(LivingEntity livingEntity) {
		return livingEntity.getActiveEffectsMap().containsKey(AbysmStatusEffects.BLUE);
	}

	public static boolean isBlue(LivingEntity livingEntity) {
		return hasBlueEffect(livingEntity) || livingEntity instanceof LivingEntityDuck duck && duck.abysm$isBlue();
	}
}
