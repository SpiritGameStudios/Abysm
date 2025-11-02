package dev.spiritstudios.abysm.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class BloomGlimmerParticle extends BaseAshSmokeParticle {
	public float rotationSpeed = 0;

	protected BloomGlimmerParticle(
		ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider, int baseMaxAge, float gravityStrength, float velocityMultiplier
	) {
		super(world, x, y, z, 0F, 0F, 0F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 0.0F, baseMaxAge, gravityStrength, false);
		this.friction = velocityMultiplier;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.removed) {
			this.oRoll = this.roll;
			this.roll = this.roll + Mth.TWO_PI * this.rotationSpeed;

			this.rotationSpeed *= 0.99F;
		}
	}

	@Override
	public float getQuadSize(float tickProgress) {
		float lifeProgress = Mth.clamp((this.age + tickProgress) / this.lifetime, 0F, 1F);
		float multiplier = Mth.clamp(lifeProgress * 10.0F, 0.0F, 1.0F) * Mth.clamp((1 - lifeProgress) * 2.0F, 0.0F, 1.0F);
		return this.quadSize * multiplier;
	}

	@Override
	protected int getLightColor(float tickProgress) {
		float relativeAge = Mth.clamp((this.age + tickProgress) / this.lifetime, 0F, 1F);

		int baseBrightness = super.getLightColor(tickProgress);

		int blockLight = baseBrightness & 0xFF;
		blockLight += (int) (relativeAge * 3.0F * 16.0F);
		if (blockLight > 240) {
			blockLight = 240;
		}

		int skyLight = baseBrightness >> 16 & 0xFF;

		return blockLight | (skyLight << 16);
	}

	public abstract static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public abstract int getColorStart();

		public abstract int getColorEnd();

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			RandomSource random = clientWorld.getRandom();

			BloomGlimmerParticle particle = new BloomGlimmerParticle(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.spriteProvider, 40, 0.0125F, 0.96F);

			int color = ARGB.lerp(random.nextFloat(), getColorStart(), getColorEnd());
			float red = ARGB.redFloat(color);
			float green = ARGB.greenFloat(color);
			float blue = ARGB.blueFloat(color);
			particle.setColor(red, green, blue);

			particle.scale(0.6F + 1.8F * random.nextFloat());

			particle.rotationSpeed = (float) (random.nextGaussian() * 0.2F * Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed + zSpeed * zSpeed));

			return particle;
		}
	}

	public static class RosyFactory extends Factory {
		public RosyFactory(SpriteSet spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public int getColorStart() {
			return 0xBF4FAF;
		}

		@Override
		public int getColorEnd() {
			return 0xFFDFEF;
		}
	}

	public static class SunnyFactory extends Factory {
		public SunnyFactory(SpriteSet spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public int getColorStart() {
			return 0xDFBF8F;
		}

		@Override
		public int getColorEnd() {
			return 0xFFEFDF;
		}
	}

	public static class MauveFactory extends Factory {
		public MauveFactory(SpriteSet spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public int getColorStart() {
			return 0xA75FBF;
		}

		@Override
		public int getColorEnd() {
			return 0xEFDFFF;
		}
	}

	public static class ThornFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public ThornFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			RandomSource random = clientWorld.getRandom();

			BloomGlimmerParticle particle = new BloomGlimmerParticle(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.spriteProvider, 25, 0.005F, 0.98F);

			particle.setColor(1F, 1F, 1F);

			particle.scale(0.45F + 0.75F * random.nextFloat());

			particle.rotationSpeed = (float) ((-1.5F + random.nextGaussian()) * 0.2F * Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed + zSpeed * zSpeed));

			return particle;
		}
	}
}
