package dev.spiritstudios.abysm.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class SpiralingParticle extends SingleQuadParticle {
	private final SpriteSet provider;

	public float maxRadius;
	public float radius;
	public float speed;
	public int spiralLeaveAge;
	public boolean billboard;

	private final Quaternionf rotationStorage = new Quaternionf();

	public SpiralingParticle(ClientLevel level, double x, double y, double z, float maxRadius, float speed, int maxAge, int spiralLeaveAge, boolean billboard, SpriteSet sprites) {
		super(level, x, y, z, sprites.first());
		this.provider = sprites;

		this.xd = 0;
		this.yd = 0;
		this.zd = 0;

		this.maxRadius = maxRadius;
		this.radius = this.maxRadius;
		this.speed = speed;
		this.spiralLeaveAge = spiralLeaveAge;
		this.billboard = billboard;

		// FIXME - if someone who's better at math wants to make this perfect, go on ahead
		this.setBoundingBox(this.getBoundingBox().move((this.maxRadius * this.speed) / 2f, 0, -(this.maxRadius / this.speed)));
		this.setLocationFromBoundingbox();
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		this.lifetime = maxAge;

		this.setSpriteFromAge(this.provider);
	}

	@Override
	public void tick() {
		super.tick();
		this.oRoll = this.roll;
		if (this.quadSize <= 0f) {
			this.remove();
		}

		if (this.age == this.spiralLeaveAge) {
			this.speed /= 2f;
			this.yd = this.random.nextGaussian() / 64f;
		}

		if (this.age >= this.spiralLeaveAge) {
			this.radius += 0.001f;
			this.quadSize -= 0.005f;
		}

		this.xd = Mth.cos(this.age * this.speed) * this.radius;
		this.zd = Mth.sin(this.age * this.speed) * this.radius;

		//noinspection SuspiciousNameCombination
		this.roll = (float) Mth.atan2(this.xd, this.zd) + Mth.HALF_PI;

		this.setSpriteFromAge(this.provider);
	}

	@Override
	public void extract(QuadParticleRenderState reusedState, Camera camera, float partialTick) {
		if (billboard) {
			super.extract(reusedState, camera, partialTick);
			return;
		}

		rotationStorage.rotationY(Mth.lerp(partialTick, this.oRoll, this.roll));
		this.extractRotatedQuad(reusedState, camera, rotationStorage, partialTick);

		rotationStorage.rotateY(Mth.PI);
		this.extractRotatedQuad(reusedState, camera, rotationStorage, partialTick);

	}


	@Override
	public int getLightColor(float tint) {
		return 13631632; // LightmapTextureManager.pack(9, 13)
	}

	@Override
	protected @NotNull Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public abstract static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		// The radius has no unit of measurement, and is affected by the speed because I couldn't figure out the math before wanting to move on
		public abstract float maxRadius(RandomSource random);

		public abstract float maxSpeed(RandomSource random);

		public abstract int maxAge(RandomSource random);

		public abstract int spiralLeaveAge(RandomSource random);

		public boolean billboard() {
			return true;
		}

		@Override
		public @Nullable Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			float maxRadius = this.maxRadius(random);
			float maxSpeed = this.maxSpeed(random);
			int maxAge = this.maxAge(random);
			int spiralLeaveAge = this.spiralLeaveAge(random);
			boolean billboard = this.billboard();

			return new SpiralingParticle(level, x, y, z, maxRadius, maxSpeed, maxAge, spiralLeaveAge, billboard, spriteProvider);
		}

	}

	@Environment(EnvType.CLIENT)
	public static class ElectricitySpiral extends Factory {
		public ElectricitySpiral(SpriteSet spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public float maxRadius(RandomSource random) {
			return random.nextFloat();
		}

		@Override
		public float maxSpeed(RandomSource random) {
			return 0.5f + (random.nextInt(7) == 0 ? 1f : 0f);
		}

		@Override
		public int maxAge(RandomSource random) {
			return 100;
		}

		@Override
		public int spiralLeaveAge(RandomSource random) {
			return random.nextIntBetweenInclusive(60, 75);
		}

		@Override
		public boolean billboard() {
			return false;
		}
	}

	public static class ElectricitySpeck extends ElectricitySpiral {
		public ElectricitySpeck(SpriteSet spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public boolean billboard() {
			return true;
		}
	}
}
