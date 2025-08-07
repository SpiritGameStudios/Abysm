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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HarpoonOwner {
	private @Nullable HarpoonEntity harpoon;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void abysm$setHarpoon(HarpoonEntity harpoon) {
		this.harpoon = harpoon;
	}

	@Override
	public @Nullable HarpoonEntity abysm$getHarpoon() {
		return harpoon;
	}

	@Inject(method = "tickMovement", at = @At("TAIL"))
	private void grappleHook(CallbackInfo ci) {
		if (this.harpoon != null && this.harpoon.isInBlock() && this.harpoon.isGrappling()) {
			this.onLanding();
			if (this.isLogicalSideForUpdatingMovement()) {
				Vec3d vec3d = this.harpoon.getPos().subtract(this.getEyePos());
				float g = this.harpoon.getLength();
				double d = vec3d.length();
				if (d > (double)g) {
					double e = d / (double)g * 0.1;
					this.addVelocity(vec3d.multiply(1.0 / d).multiply(e, e * 1.1, e));
				}
			}
		}
	}
}
