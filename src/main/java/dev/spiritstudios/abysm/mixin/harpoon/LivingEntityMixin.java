package dev.spiritstudios.abysm.mixin.harpoon;

import dev.spiritstudios.abysm.entity.harpoon.HarpoonDrag;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements HarpoonDrag {
	@Shadow
	public abstract boolean hasInfiniteMaterials();

	@Unique
	protected HarpoonEntity abysm$harpoon;

	@Unique
	protected int abysm$dragTicks = 0;

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void getDragged(CallbackInfo ci) {
		if (this.abysm$dragTicks > 0) {
			this.abysm$dragTicks--;
		}
		if (this.abysm$harpoon == null) {
			return;
		}
		if (!this.abysm$harpoon.isAlive() || this.isInvulnerable() || this.noPhysics || this.hasInfiniteMaterials() || this.isSpectator() || this.getType().is(AbysmEntityTypeTags.HARPOON_UNHAULABLE)) {
			this.abysm$harpoon = null;
			return;
		}
		Entity harpoonOwner = this.abysm$harpoon.getOwner();
		if (harpoonOwner == null || !harpoonOwner.isAlive() || this.isPassenger()) {
			this.abysm$harpoon = null;
			return;
		}
		this.setDeltaMovement(this.abysm$harpoon.getDeltaMovement());
		this.hasImpulse = true;
		this.setPos(this.abysm$harpoon.position());

		if ((Object) this instanceof PathfinderMob pathAwareEntity) {
			pathAwareEntity.getNavigation().stop();
		}
	}

	@Override
	public void abysm$setStuckHarpoon(HarpoonEntity harpoon) {
		this.abysm$harpoon = harpoon;
	}

	@Override
	public void abysm$setDragTicks(int dragTicks) {
		this.abysm$dragTicks = dragTicks;
	}

	@Override
	public int abysm$getDragTicks() {
		return this.abysm$dragTicks;
	}
}
