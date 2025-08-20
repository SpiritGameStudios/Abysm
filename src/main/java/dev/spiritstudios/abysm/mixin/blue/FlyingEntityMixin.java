package dev.spiritstudios.abysm.mixin.blue;

import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlyingEntity.class)
public abstract class FlyingEntityMixin extends MobEntity {
	protected FlyingEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "travel", at = @At("RETURN"))
	private void fallIfBlue(Vec3d movementInput, CallbackInfo ci) {
		if (BlueEffect.isBlue(this)) {
			this.setVelocity(this.getVelocity().add(0.0, -this.getEffectiveGravity(), 0.0));
		}
	}
}
