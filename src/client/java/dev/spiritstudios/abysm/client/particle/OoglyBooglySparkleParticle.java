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

public class OoglyBooglySparkleParticle extends SimpleAnimatedParticle {
	private final SpriteSet provider;
	protected OoglyBooglySparkleParticle(ClientLevel clientWorld, double x, double y, double z, SpriteSet sprites) {
		super(clientWorld, x, y, z, sprites, 0.0F);
		this.provider = sprites;
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

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public @Nullable Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			return new OoglyBooglySparkleParticle(level, x, y, z, spriteProvider);
		}
	}
}
