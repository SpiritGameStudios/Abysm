package dev.spiritstudios.abysm.client.particle;

import net.minecraft.client.particle.AscendingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class BloomGlimmerParticle extends AscendingParticle {

	public float rotationSpeed = 0;

	protected BloomGlimmerParticle(
		ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider, int baseMaxAge, float gravityStrength, float velocityMultiplier
	) {
		super(world, x, y, z, 0F, 0F, 0F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 0.0F, baseMaxAge, gravityStrength, false);
		this.velocityMultiplier = velocityMultiplier;
	}

	@Override
	public void tick() {
		super.tick();
		if(!this.dead) {
			this.lastAngle = this.angle;
			this.angle = this.angle + (float) Math.TAU * this.rotationSpeed;

			this.rotationSpeed *= 0.99F;
		}
	}

	@Override
	public float getSize(float tickProgress) {
		float relativeAge = MathHelper.clamp((this.age + tickProgress) / this.maxAge, 0F, 1F);
		float multiplier = MathHelper.clamp(relativeAge * 10.0F, 0.0F, 1.0F) * MathHelper.clamp((1 - relativeAge) * 2.0F, 0.0F, 1.0F);
		return this.scale * multiplier;
	}

	@Override
	protected int getBrightness(float tickProgress) {
		float relativeAge = MathHelper.clamp((this.age + tickProgress) / this.maxAge, 0F, 1F);

		int baseBrightness = super.getBrightness(tickProgress);

		int blockLight = baseBrightness & 0xFF;
		blockLight += (int)(relativeAge * 3.0F * 16.0F);
		if (blockLight > 240) {
			blockLight = 240;
		}

		int skyLight = baseBrightness >> 16 & 0xFF;

		return blockLight | (skyLight << 16);
	}

	public abstract static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public abstract int getColorStart();
		public abstract int getColorEnd();

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			Random random = clientWorld.getRandom();

			BloomGlimmerParticle particle = new BloomGlimmerParticle(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.spriteProvider, 40, 0.0125F, 0.96F);

			int col = ColorHelper.lerp(random.nextFloat(), getColorStart(), getColorEnd());
			float red = ColorHelper.getRed(col) / 255.0F;
			float green = ColorHelper.getGreen(col) / 255.0F;
			float blue = ColorHelper.getBlue(col) / 255.0F;
			particle.setColor(red, green, blue);

			particle.scale(0.6F + 1.8F * random.nextFloat());

			particle.rotationSpeed = (float) (random.nextGaussian() * 0.2F * Math.sqrt(xSpeed*xSpeed + ySpeed*ySpeed + zSpeed*zSpeed));

			return particle;
		}
	}

	public static class RosyFactory extends Factory {
		public RosyFactory(SpriteProvider spriteProvider) {
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
		public SunnyFactory(SpriteProvider spriteProvider) {
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
		public MauveFactory(SpriteProvider spriteProvider) {
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

	public static class ThornFactory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public ThornFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			Random random = clientWorld.getRandom();

			BloomGlimmerParticle particle = new BloomGlimmerParticle(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.spriteProvider, 25, 0.005F, 0.98F);

			particle.setColor(1F, 1F, 1F);

			particle.scale(0.45F + 0.75F * random.nextFloat());

			particle.rotationSpeed = (float) ((-1.5F + random.nextGaussian()) * 0.2F * Math.sqrt(xSpeed*xSpeed + ySpeed*ySpeed + zSpeed*zSpeed));

			return particle;
		}
	}
}
