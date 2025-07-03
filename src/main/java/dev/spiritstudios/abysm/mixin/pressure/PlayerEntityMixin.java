package dev.spiritstudios.abysm.mixin.pressure;

import dev.spiritstudios.abysm.registry.AbysmDamageTypes;
import dev.spiritstudios.abysm.util.PressureFinder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void pressureDamage(CallbackInfo ci) {
		if (!(this.getWorld() instanceof ServerWorld world)) {
			return;
		}
		float pressure = PressureFinder.getPressure(world, this.getBlockPos());
		if (pressure >= 40f) {
			this.damage(world,
				new DamageSource(AbysmDamageTypes.getFromWorld(world, AbysmDamageTypes.PRESSURE)),
				pressure * 0.2f);
		}
	}
}
