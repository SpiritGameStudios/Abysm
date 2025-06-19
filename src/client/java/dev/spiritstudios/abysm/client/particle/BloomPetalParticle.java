package dev.spiritstudios.abysm.client.particle;

import dev.spiritstudios.abysm.registry.AbysmParticleTypes;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class BloomPetalParticle extends SpriteBillboardParticle {
	private static final float SPEED_SCALE = 0.0025F;
	private static final int INITIAL_MAX_AGE = 300;

	private float angularVelocity;
	private final float angularAcceleration;
	private final float windStrength;
	private final float xWindStrength;
	private final float zWindStrength;
	private final float swirlAngleOffset;
	private final ParticleEffect nextParticle;

	protected BloomPetalParticle(
		ClientWorld world,
		double x,
		double y,
		double z,
		SpriteProvider spriteProvider,
		float gravity,
		float windStrength,
		float size,
		float initialYVelocity,
		ParticleEffect nextParticle
	) {
		super(world, x, y, z);

		this.setSprite(spriteProvider.getSprite(this.random.nextInt(12), 12));

		this.maxAge = INITIAL_MAX_AGE;

		float actualSize = size * (this.random.nextBoolean() ? 0.05F : 0.075F);
		this.scale = actualSize;
		this.setBoundingBoxSpacing(actualSize, actualSize);

		this.velocityMultiplier = 0.98F;
		this.velocityY = -initialYVelocity;

		this.gravityStrength = gravity * 1.2F * SPEED_SCALE;

		this.angularVelocity = (float)Math.toRadians(this.random.nextBoolean() ? -60.0 : 60.0);
		this.angularAcceleration = (float)Math.toRadians(this.random.nextBoolean() ? -10.0 : 10.0);

		this.windStrength = windStrength;

		float windAngle = this.random.nextFloat() * MathHelper.PI;
		this.xWindStrength = MathHelper.cos(windAngle) * this.windStrength;
		this.zWindStrength = MathHelper.sin(windAngle) * this.windStrength;

		this.swirlAngleOffset = this.random.nextFloat() * MathHelper.PI;

		this.nextParticle = nextParticle;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;
		if (this.maxAge-- <= 0) {
			this.markDead();
		}

		if (!this.dead) {
			float age = INITIAL_MAX_AGE - this.maxAge;
			float lifeLeft = Math.min(age / INITIAL_MAX_AGE, 1.0F);

			double xWind = 0.0;
			double zWind = 0.0;
			double strength = Math.pow(lifeLeft, 1.25);
			xWind += this.xWindStrength * strength;
			zWind += this.zWindStrength * strength;

			xWind += lifeLeft * (MathHelper.cos(lifeLeft * MathHelper.TAU + this.swirlAngleOffset) * this.windStrength) * strength;
			zWind += lifeLeft * (MathHelper.sin(lifeLeft * MathHelper.TAU + this.swirlAngleOffset) * this.windStrength) * strength;

			this.velocityX += xWind * SPEED_SCALE;
			this.velocityZ += zWind * SPEED_SCALE;
			this.velocityY = this.velocityY - this.gravityStrength;

			this.angularVelocity = this.angularVelocity + this.angularAcceleration / 20.0F;
			this.lastAngle = this.angle;
			this.angle = this.angle + this.angularVelocity / 20.0F;

			this.move(this.velocityX, this.velocityY, this.velocityZ);

			if (this.onGround || this.maxAge < (INITIAL_MAX_AGE - 1) && (this.velocityX == 0.0 || this.velocityZ == 0.0)) {
				this.maxAge -= 10;
				if (this.maxAge <= 0) {
					this.markDead();
				}
			}

			if (!this.dead) {
				this.velocityX = this.velocityX * this.velocityMultiplier;
				this.velocityY = this.velocityY * this.velocityMultiplier;
				this.velocityZ = this.velocityZ * this.velocityMultiplier;

				if(this.random.nextInt(95) == 0) {
					this.world.addParticleClient(
						this.nextParticle,
						this.x, this.y, this.z,
						this.random.nextGaussian() * 0.1F + this.velocityX * (0.3F + 0.7F * this.random.nextFloat()),
						this.random.nextGaussian() * 0.1F + this.velocityY * (0.3F + 0.7F * this.random.nextFloat()),
						this.random.nextGaussian() * 0.1F + this.velocityZ * (0.3F + 0.7F * this.random.nextFloat())
					);
				}
			}
		}
	}

	@Override
	public float getSize(float tickProgress) {
		float relativeAge = MathHelper.clamp((INITIAL_MAX_AGE - this.maxAge + tickProgress) / INITIAL_MAX_AGE, 0F, 1F);
		float multiplier = MathHelper.clamp(relativeAge * 10.0F, 0.0F, 1.0F) * MathHelper.clamp((1 - relativeAge) * 1.5F, 0.0F, 1.0F);
		return this.scale * multiplier;
	}

	@Override
	protected int getBrightness(float tickProgress) {
		float relativeAge = MathHelper.clamp((INITIAL_MAX_AGE - this.maxAge + tickProgress) / INITIAL_MAX_AGE, 0F, 1F);

		int baseBrightness = super.getBrightness(tickProgress);

		int blockLight = baseBrightness & 0xFF;
		blockLight += (int)(relativeAge * 1.5F * 16.0F);
		if (blockLight > 255) {
			blockLight = 255;
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
		public abstract ParticleEffect getNextParticle();

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			Random random = clientWorld.getRandom();

			float size = 0.6F + random.nextFloat() * 2.5F;
			BloomPetalParticle particle = new BloomPetalParticle(clientWorld, x, y, z, this.spriteProvider, 0.7F, 10.0F, size, 0.011F, getNextParticle());

			int col = ColorHelper.lerp(random.nextFloat(), getColorStart(), getColorEnd());
			float red = ColorHelper.getRed(col) / 255.0F;
			float green = ColorHelper.getGreen(col) / 255.0F;
			float blue = ColorHelper.getBlue(col) / 255.0F;
			particle.setColor(red, green, blue);

			return particle;
		}
	}

	public static class RosyFactory extends Factory {
		public RosyFactory(SpriteProvider spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public int getColorStart() {
			return 0xEF4F5F;
		}

		@Override
		public int getColorEnd() {
			return 0xFF8FCF;
		}

		@Override
		public ParticleEffect getNextParticle() {
			return AbysmParticleTypes.ROSEBLOOM_GLIMMER;
		}
	}

	public static class SunnyFactory extends Factory {
		public SunnyFactory(SpriteProvider spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public int getColorStart() {
			return 0xEFD73F;
		}

		@Override
		public int getColorEnd() {
			return 0xFFFFBF;
		}

		@Override
		public ParticleEffect getNextParticle() {
			return AbysmParticleTypes.SUNBLOOM_GLIMMER;
		}
	}

	public static class MauveFactory extends Factory {
		public MauveFactory(SpriteProvider spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public int getColorStart() {
			return 0x7F4F9F;
		}

		@Override
		public int getColorEnd() {
			return 0xAF6FDF;
		}

		@Override
		public ParticleEffect getNextParticle() {
			return AbysmParticleTypes.MALLOWBLOOM_GLIMMER;
		}
	}
}
