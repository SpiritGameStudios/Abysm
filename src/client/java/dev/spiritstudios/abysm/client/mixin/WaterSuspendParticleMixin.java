package dev.spiritstudios.abysm.client.mixin;

import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterSuspendParticle.class)
public abstract class WaterSuspendParticleMixin extends SpriteBillboardParticle {
	protected WaterSuspendParticleMixin(ClientWorld clientWorld, double x, double y, double z) {
		super(clientWorld, x, y, z);
	}

	@Override
	public float getSize(float tickDelta) {
		return (float) Easing.SINE.yoyoOutIn(Math.min(age + tickDelta, getMaxAge()), 0, scale, getMaxAge());
	}
}
