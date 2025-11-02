package dev.spiritstudios.abysm.client.mixin.render;

import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnUpParticleMixin extends TextureSheetParticle {
	protected BubbleColumnUpParticleMixin(ClientLevel clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void pop(CallbackInfo ci) {
		if (this.removed) {
			level.addParticle(
				ParticleTypes.BUBBLE_POP,
				x, y, z,
				0F, 0F, 0F
			);

			level.playLocalSound(
				x, y, z,
				SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
				SoundSource.BLOCKS,
				0.25F,
				1.0F + 0.1F * Math.max(0, random.nextInt(10) - 5 + 1),
				false
			);
		}
	}

	@Override
	public float getQuadSize(float tickDelta) {
		return (float) Easing.QUINT.out(Math.min(age + tickDelta, getLifetime()), 0, quadSize, getLifetime());
	}
}
