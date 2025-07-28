package dev.spiritstudios.abysm.client.particle;

import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class PoggdrygllSporesParticle extends SpriteBillboardParticle {
	protected PoggdrygllSporesParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		float color = this.random.nextFloat() * 0.1F + 0.5F;
		this.red = color;
		this.green = color + 0.05F;
		this.blue = color + 0.25F;
		this.setBoundingBoxSpacing(0.02F, 0.02F);
		this.scale = this.scale * (this.random.nextFloat() * 0.6F + 0.6F);
		this.velocityX *= 0.02F;
		this.velocityY *= 0.02F;
		this.velocityZ *= 0.02F;
		this.maxAge = (int)(20.0 / (Math.random() * 0.8 + 1.0));
	}

	@Override
	public float getSize(float tickDelta) {
		return (float) Easing.SINE.yoyoOutIn(Math.min(age + tickDelta, getMaxAge()), 0, scale, getMaxAge());
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			PoggdrygllSporesParticle particle = new PoggdrygllSporesParticle(clientWorld, d, e, f, g, h, i);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}
}
