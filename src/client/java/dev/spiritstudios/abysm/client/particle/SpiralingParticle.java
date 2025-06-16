package dev.spiritstudios.abysm.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;

public class SpiralingParticle extends SpriteBillboardParticle {
	private final SpriteProvider provider;

	public float maxRadius;
	public float radius;
	public float speed;
	public int spiralLeaveAge;
	public boolean billboard;

	public SpiralingParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ, float maxRadius, float speed, int maxAge, int spiralLeaveAge, boolean billboard, SpriteProvider provider) {
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.provider = provider;
		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;
		this.maxRadius = maxRadius;
		this.radius = this.maxRadius;
		this.speed = speed;
		this.spiralLeaveAge = spiralLeaveAge;
		this.billboard = billboard;

		// FIXME - if someone who's better at math wants to make this perfect, go on ahead
		this.setBoundingBox(this.getBoundingBox().offset((this.maxRadius * this.speed) / 2f, 0, -(this.maxRadius / this.speed)));
		this.repositionFromBoundingBox();
		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;

		this.maxAge = maxAge;

		this.setSpriteForAge(this.provider);
	}

	@Override
	public void tick() {
		super.tick();
		this.lastAngle = this.angle;
		if(this.scale <= 0f) {
			this.markDead();
		}

		if(this.age == this.spiralLeaveAge) {
			this.speed /= 2f;
			this.velocityY = this.random.nextGaussian() / 64f;
		}

		if(this.age >= this.spiralLeaveAge) {
			this.radius += 0.001f;
			this.scale -= 0.005f;
		}

		this.velocityX = Math.cos(this.age * this.speed) * this.radius;
		this.velocityZ = Math.sin(this.age * this.speed) * this.radius;
		this.angle = (float) (Math.atan2(this.velocityX, this.velocityZ) + Math.toRadians(90f));

		this.setSpriteForAge(this.provider);
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
		if(this.billboard) {
			super.render(vertexConsumer, camera, tickProgress);
			return;
		}

		Quaternionf quaternionf = new Quaternionf();
		float lerpedAngle = MathHelper.lerp(tickProgress, this.lastAngle, this.angle);

		quaternionf.rotateYXZ(lerpedAngle, 0, 0);
		this.render(vertexConsumer, camera, quaternionf, tickProgress);
		quaternionf.rotateY((float) Math.toRadians(180f));
		this.render(vertexConsumer, camera, quaternionf, tickProgress);
	}

	@Override
	public int getBrightness(float tint) {
		return 13631632; // LightmapTextureManager.pack(9, 13)
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public abstract static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		// The radius has no unit of measurement, and is affected by the speed because I couldn't figure out the math before wanting to move on
		public abstract float maxRadius(Random random);
		public abstract float maxSpeed(Random random);
		public abstract int maxAge(Random random);
		public abstract int spiralLeaveAge(Random random);
		public boolean billboard() { return true; }

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
			Random random = clientWorld.getRandom();
			float maxRadius = this.maxRadius(random);
			float maxSpeed = this.maxSpeed(random);
			int maxAge = this.maxAge(random);
			int spiralLeaveAge = this.spiralLeaveAge(random);
			boolean billboard = this.billboard();

			return new SpiralingParticle(clientWorld, x, y, z, velX, velY, velZ, maxRadius, maxSpeed, maxAge, spiralLeaveAge, billboard, spriteProvider);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class ElectricitySpiral extends Factory {
		public ElectricitySpiral(SpriteProvider spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public float maxRadius(Random random) {
			return random.nextFloat();
		}

		@Override
		public float maxSpeed(Random random) {
			return 0.5f + (random.nextInt(7) == 0 ? 1f : 0f);
		}

		@Override
		public int maxAge(Random random) {
			return 100;
		}

		@Override
		public int spiralLeaveAge(Random random) {
			return random.nextBetween(60, 75);
		}

		@Override
		public boolean billboard() {
			return false;
		}
	}

	public static class ElectricitySpeck extends ElectricitySpiral {
		public ElectricitySpeck(SpriteProvider spriteProvider) {
			super(spriteProvider);
		}

		@Override
		public boolean billboard() {
			return true;
		}
	}
}
