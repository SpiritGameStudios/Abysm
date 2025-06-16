package dev.spiritstudios.abysm.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class OoglyBooglySparkleParticle extends SpriteBillboardParticle {
	private final SpriteProvider provider;
	protected OoglyBooglySparkleParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteProvider provider) {
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.provider = provider;
		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;

		this.maxAge = 27;
		this.scale = 3f + this.random.nextFloat();

		this.setSpriteForAge(this.provider);
	}

	@Override
	public void tick() {
		super.tick();
		if(this.scale >= 2.5f) {
			this.scale -= 0.05f;
		}

		this.setSpriteForAge(this.provider);
	}

	@Override
	public int getBrightness(float tint) {
		return 13631632; // LightmapTextureManager.pack(9, 13)
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
			OoglyBooglySparkleParticle particle = new OoglyBooglySparkleParticle(clientWorld, x, y, z, velX, velY, velZ, spriteProvider);
			return particle;
		}
	}
}
