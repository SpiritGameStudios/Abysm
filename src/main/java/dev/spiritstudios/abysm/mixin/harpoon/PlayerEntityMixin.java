package dev.spiritstudios.abysm.mixin.harpoon;

import dev.spiritstudios.abysm.duck.HarpoonOwner;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HarpoonOwner {
	@Unique
	private @Nullable HarpoonEntity harpoon;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void abysm$setHarpoon(HarpoonEntity harpoon) {
		this.harpoon = harpoon;
	}

	@Inject(method = "tickMovement", at = @At("TAIL"))
	private void grappleHook(CallbackInfo ci) {
		if (this.harpoon == null || !this.harpoon.isInBlock() || !this.harpoon.isGrappling()) {
			return;
		}

		// Sets fallDistance to 0.0 (negate fall damage)
		this.onLanding();

		Vec3d direction = this.harpoon.getPos().subtract(this.getEyePos());

		if (this.isSubmergedInWater()) {
			this.addVelocity(direction.normalize().multiply(0.2, 0.22, 0.2));
			return;
		}

		float length = this.harpoon.getLength();
		double distance = direction.length();

		if (distance > (double) length) {
			double e = distance / (double) length * 0.1;
			this.addVelocity(direction.multiply(1.0 / distance).multiply(e, e * 1.1, e));
		}
	}
}
