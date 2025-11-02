package dev.spiritstudios.abysm.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class OoglyBooglySparkleParticle extends TextureSheetParticle {
	private final SpriteSet provider;
	protected OoglyBooglySparkleParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteSet provider) {
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.provider = provider;
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;

		this.lifetime = 27;
		this.quadSize = 3f + this.random.nextFloat();

		this.setSpriteFromAge(this.provider);
	}

	@Override
	public void tick() {
		super.tick();
		if(this.quadSize >= 2.5f) {
			this.quadSize -= 0.05f;
		}

		this.setSpriteFromAge(this.provider);
	}

	@Override
	public int getLightColor(float tint) {
		return 13631632; // LightmapTextureManager.pack(9, 13)
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
			return new OoglyBooglySparkleParticle(clientWorld, x, y, z, velX, velY, velZ, spriteProvider);
		}
	}
}
