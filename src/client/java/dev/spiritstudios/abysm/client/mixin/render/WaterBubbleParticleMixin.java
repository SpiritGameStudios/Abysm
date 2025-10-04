package dev.spiritstudios.abysm.client.mixin.render;

import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.WaterBubbleParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterBubbleParticle.class)
public abstract class WaterBubbleParticleMixin extends SpriteBillboardParticle {
	protected WaterBubbleParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/WaterBubbleParticle;markDead()V"))
	private void pop(CallbackInfo ci) {
		world.addParticleClient(
			ParticleTypes.BUBBLE_POP,
			x, y, z,
			0F, 0F, 0F
		);

		world.playSoundClient(
			x, y, z,
			SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP,
			SoundCategory.BLOCKS,
			0.25F,
			1.0F + 0.1F * Math.max(0, random.nextInt(10) - 5 + 1),
			false
		);
	}
}
