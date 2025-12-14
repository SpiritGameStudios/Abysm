package dev.spiritstudios.abysm.mixin.harpoon;

import dev.spiritstudios.abysm.duck.HarpoonOwner;
import dev.spiritstudios.abysm.world.entity.harpoon.HarpoonEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements HarpoonOwner {
	@Unique
	private @Nullable HarpoonEntity harpoon;

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public void abysm$setHarpoon(HarpoonEntity harpoon) {
		this.harpoon = harpoon;
	}

	@Inject(method = "aiStep", at = @At("TAIL"))
	private void grappleHook(CallbackInfo ci) {
		if (this.harpoon == null || !this.harpoon.isInBlock() || !this.harpoon.isGrappling()) {
			return;
		}

		// Sets fallDistance to 0.0 (negate fall damage)
		this.resetFallDistance();

		Vec3 direction = this.harpoon.position().subtract(this.getEyePosition());

		if (this.isUnderWater()) {
			this.push(direction.normalize().multiply(0.2, 0.22, 0.2));
			return;
		}

		float length = this.harpoon.getLength();
		double distance = direction.length();

		if (distance > (double) length) {
			double e = distance / (double) length * 0.1;
			this.push(direction.scale(1.0 / distance).multiply(e, e * 1.1, e));
		}
	}
}
