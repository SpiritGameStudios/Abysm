package dev.spiritstudios.abysm.mixin.blue;

import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "isSwimming", at = @At("HEAD"), cancellable = true)
	private void blockSwimmingWhenBlue(CallbackInfoReturnable<Boolean> cir) {
		if (BlueEffect.hasBlueEffect(this)) {
			cir.setReturnValue(false);
		}
	}
}
