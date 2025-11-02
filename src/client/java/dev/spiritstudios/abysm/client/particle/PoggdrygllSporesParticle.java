package dev.spiritstudios.abysm.client.particle;

import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class PoggdrygllSporesParticle extends TextureSheetParticle {
	protected PoggdrygllSporesParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		float color = this.random.nextFloat() * 0.1F + 0.5F;
		this.rCol = color;
		this.gCol = color + 0.05F;
		this.bCol = color + 0.25F;
		this.setSize(0.02F, 0.02F);
		this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.6F);
		this.xd *= 0.02F;
		this.yd *= 0.02F;
		this.zd *= 0.02F;
		this.lifetime = (int)(20.0 / (Math.random() * 0.8 + 1.0));
	}

	@Override
	public float getQuadSize(float tickDelta) {
		return (float) Easing.SINE.yoyoOutIn(Math.min(age + tickDelta, getLifetime()), 0, quadSize, getLifetime());
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			PoggdrygllSporesParticle particle = new PoggdrygllSporesParticle(clientWorld, d, e, f, g, h, i);
			particle.pickSprite(this.spriteProvider);
			return particle;
		}
	}
}
