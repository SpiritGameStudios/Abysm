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

public class OoglyBooglyElectricityParticle extends TextureSheetParticle {
	protected OoglyBooglyElectricityParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteSet provider) {
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;

		this.lifetime = 2;
		this.quadSize = 0.01f + this.random.nextFloat();

		this.pickSprite(provider);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public int getLightColor(float tint) {
		return 8388752; // LightmapTextureManager.pack(9, 8)
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
			return new OoglyBooglyElectricityParticle(clientWorld, x, y, z, velX, velY, velZ, spriteProvider);
		}
	}
}
