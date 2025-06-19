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

public class OoglyBooglyElectricityParticle extends SpriteBillboardParticle {
	protected OoglyBooglyElectricityParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteProvider provider) {
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;

		this.maxAge = 2;
		this.scale = 0.01f + this.random.nextFloat();

		this.setSprite(provider);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public int getBrightness(float tint) {
		return 8388752; // LightmapTextureManager.pack(9, 8)
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
			return new OoglyBooglyElectricityParticle(clientWorld, x, y, z, velX, velY, velZ, spriteProvider);
		}
	}
}
