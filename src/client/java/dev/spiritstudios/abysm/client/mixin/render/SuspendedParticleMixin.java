package dev.spiritstudios.abysm.client.mixin.render;

import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SuspendedParticle.class)
public abstract class SuspendedParticleMixin extends TextureSheetParticle {
	protected SuspendedParticleMixin(ClientLevel clientWorld, double x, double y, double z) {
		super(clientWorld, x, y, z);
	}

	@Override
	public float getQuadSize(float tickDelta) {
		return (float) Easing.SINE.yoyoOutIn(Math.min(age + tickDelta, getLifetime()), 0, quadSize, getLifetime());
	}
}
