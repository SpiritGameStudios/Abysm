package dev.spiritstudios.abysm.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class OoglyBooglyElectricityParticle extends SimpleAnimatedParticle {
	protected OoglyBooglyElectricityParticle(ClientLevel clientWorld, double x, double y, double z, SpriteSet sprites) {
		super(clientWorld, x, y, z, sprites, 0.0F);
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;

		this.lifetime = 2;
		this.quadSize = 0.01f + this.random.nextFloat();
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
	public Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}


		@Override
		public @Nullable Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			return new OoglyBooglyElectricityParticle(level, x, y, z, spriteProvider);
		}
	}
}
