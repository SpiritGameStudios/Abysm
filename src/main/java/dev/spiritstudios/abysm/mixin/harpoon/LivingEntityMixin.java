package dev.spiritstudios.abysm.mixin.harpoon;

import dev.spiritstudios.abysm.entity.harpoon.HarpoonDrag;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements HarpoonDrag {

	@Shadow
	public abstract boolean isInCreativeMode();

	@Unique
	protected HarpoonEntity abysm$harpoon;

	@Unique
	protected int abysm$dragTicks = 0;

	public LivingEntityMixin(EntityType<?> type, World world) {
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
		if (!this.abysm$harpoon.isAlive() || this.isInvulnerable() || this.noClip || this.isInCreativeMode() || this.isSpectator() || this.getType().isIn(AbysmEntityTypeTags.HARPOON_UNHAULABLE)) {
			this.abysm$harpoon = null;
			return;
		}
		Entity harpoonOwner = this.abysm$harpoon.getOwner();
		if (harpoonOwner == null || !harpoonOwner.isAlive() || this.hasVehicle()) {
			this.abysm$harpoon = null;
			return;
		}
		this.setVelocity(this.abysm$harpoon.getVelocity());
		this.velocityDirty = true;
		this.setPosition(this.abysm$harpoon.getPos());
		LivingEntity thisLiving = (LivingEntity) (Object) this;
		if (thisLiving instanceof PathAwareEntity pathAwareEntity) {
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
