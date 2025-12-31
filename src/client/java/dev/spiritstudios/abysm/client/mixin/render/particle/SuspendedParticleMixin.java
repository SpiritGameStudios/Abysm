package dev.spiritstudios.abysm.client.mixin.render.particle;

import dev.spiritstudios.spectre.api.core.math.Easing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SuspendedParticle.class)
public abstract class SuspendedParticleMixin extends SingleQuadParticle {
	protected SuspendedParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
	}

	@Override
	public float getQuadSize(float tickDelta) {
		return (float) Easing.SINE.yoyoOutIn(Math.min(age + tickDelta, getLifetime()), 0, quadSize, getLifetime());
	}
}
