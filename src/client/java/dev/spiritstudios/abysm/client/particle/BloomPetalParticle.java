package dev.spiritstudios.abysm.client.particle;

import dev.spiritstudios.abysm.core.particles.AbysmParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class BloomPetalParticle extends SingleQuadParticle {
	private static final float SPEED_SCALE = 0.0025F;
	private static final int INITIAL_MAX_AGE = 300;

	private float angularVelocity;
	private final float angularAcceleration;
	private final float windStrength;
	private final float xWindStrength;
	private final float zWindStrength;
	private final float swirlAngleOffset;
	private final ParticleOptions nextParticle;

	protected BloomPetalParticle(
		ClientLevel world,
		double x,
		double y,
		double z,
		SpriteSet sprites,
		float gravity,
		float windStrength,
		float size,
		float initialYVelocity,
		ParticleOptions nextParticle
	) {
		super(world, x, y, z, sprites.first());

		this.setSprite(sprites.get(this.random.nextInt(12), 12));

		this.lifetime = INITIAL_MAX_AGE;

		float actualSize = size * (this.random.nextBoolean() ? 0.05F : 0.075F);
		this.quadSize = actualSize;
		this.setSize(actualSize, actualSize);

		this.friction = 0.98F;
		this.yd = -initialYVelocity;

		this.gravity = gravity * 1.2F * SPEED_SCALE;


		this.angularVelocity = this.random.nextBoolean() ? -Mth.PI / 6.0F : Mth.PI / 6.0F;
		this.angularAcceleration = this.random.nextBoolean() ? -Mth.PI / 36.0F : Mth.PI / 36.0F;

		this.windStrength = windStrength;

		float windAngle = this.random.nextFloat() * Mth.PI;
		this.xWindStrength = Mth.cos(windAngle) * this.windStrength;
		this.zWindStrength = Mth.sin(windAngle) * this.windStrength;

		this.swirlAngleOffset = this.random.nextFloat() * Mth.PI;

		this.nextParticle = nextParticle;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.lifetime-- <= 0) {
			this.remove();
		}

		if (!this.removed) {
			float age = INITIAL_MAX_AGE - this.lifetime;
			float lifeLeft = Math.min(age / INITIAL_MAX_AGE, 1.0F);

			double xWind = 0.0;
			double zWind = 0.0;
			double strength = Math.pow(lifeLeft, 1.25);
			xWind += this.xWindStrength * strength;
			zWind += this.zWindStrength * strength;

			xWind += lifeLeft * (Mth.cos(lifeLeft * Mth.TWO_PI + this.swirlAngleOffset) * this.windStrength) * strength;
			zWind += lifeLeft * (Mth.sin(lifeLeft * Mth.TWO_PI + this.swirlAngleOffset) * this.windStrength) * strength;

			this.xd += xWind * SPEED_SCALE;
			this.zd += zWind * SPEED_SCALE;
			this.yd = this.yd - this.gravity;

			this.angularVelocity = this.angularVelocity + this.angularAcceleration / 20.0F;
			this.oRoll = this.roll;
			this.roll = this.roll + this.angularVelocity / 20.0F;

			this.move(this.xd, this.yd, this.zd);

			if (this.onGround || this.lifetime < (INITIAL_MAX_AGE - 1) && (this.xd == 0.0 || this.zd == 0.0)) {
				this.lifetime -= 10;
				if (this.lifetime <= 0) {
					this.remove();
				}
			}

			if (!this.removed) {
				this.xd = this.xd * this.friction;
				this.yd = this.yd * this.friction;
				this.zd = this.zd * this.friction;

				if(this.random.nextInt(95) == 0) {
					this.level.addParticle(
						this.nextParticle,
						this.x, this.y, this.z,
						this.random.nextGaussian() * 0.1F + this.xd * (0.3F + 0.7F * this.random.nextFloat()),
						this.random.nextGaussian() * 0.1F + this.yd * (0.3F + 0.7F * this.random.nextFloat()),
						this.random.nextGaussian() * 0.1F + this.zd * (0.3F + 0.7F * this.random.nextFloat())
					);
				}
			}
		}
	}

	@Override
	public float getQuadSize(float tickProgress) {
		float relativeAge = Mth.clamp((INITIAL_MAX_AGE - this.lifetime + tickProgress) / INITIAL_MAX_AGE, 0F, 1F);
		float multiplier = Mth.clamp(relativeAge * 10.0F, 0.0F, 1.0F) * Mth.clamp((1 - relativeAge) * 1.5F, 0.0F, 1.0F);
		return this.quadSize * multiplier;
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
	}

	@Override
	protected int getLightColor(float tickProgress) {
		float relativeAge = Mth.clamp((INITIAL_MAX_AGE - this.lifetime + tickProgress) / INITIAL_MAX_AGE, 0F, 1F);

		int baseBrightness = super.getLightColor(tickProgress);

		int blockLight = baseBrightness & 0xFF;
		blockLight += (int)(relativeAge * 1.5F * 16.0F);
		if (blockLight > 255) {
			blockLight = 255;
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
		public abstract ParticleOptions getNextParticle();

		@Override
		public @Nullable Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			float size = 0.6F + random.nextFloat() * 2.5F;
			BloomPetalParticle particle = new BloomPetalParticle(level, x, y, z, this.spriteProvider, 0.7F, 10.0F, size, 0.011F, getNextParticle());

			int color = ARGB.srgbLerp(random.nextFloat(), getColorStart(), getColorEnd());
			float red = ARGB.redFloat(color);
			float green = ARGB.greenFloat(color);
			float blue = ARGB.blueFloat(color);
			particle.setColor(red, green, blue);

			return particle;
		}
	}

	public static class RosyFactory extends Factory {
		public RosyFactory(SpriteSet spriteProvider) {
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
		public ParticleOptions getNextParticle() {
			return AbysmParticleTypes.ROSEBLOOM_GLIMMER;
		}
	}

	public static class SunnyFactory extends Factory {
		public SunnyFactory(SpriteSet spriteProvider) {
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
		public ParticleOptions getNextParticle() {
			return AbysmParticleTypes.SUNBLOOM_GLIMMER;
		}
	}

	public static class MauveFactory extends Factory {
		public MauveFactory(SpriteSet spriteProvider) {
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
		public ParticleOptions getNextParticle() {
			return AbysmParticleTypes.MALLOWBLOOM_GLIMMER;
		}
	}
}
