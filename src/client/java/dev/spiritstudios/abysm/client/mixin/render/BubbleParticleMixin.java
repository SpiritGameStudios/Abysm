package dev.spiritstudios.abysm.client.mixin.render;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleParticle.class)
public abstract class BubbleParticleMixin extends TextureSheetParticle {
	protected BubbleParticleMixin(ClientLevel clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleParticle;remove()V"))
	private void pop(CallbackInfo ci) {
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
