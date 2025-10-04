package dev.spiritstudios.abysm.client.particle;

import dev.spiritstudios.abysm.particle.OoglyBooglyFumesParticleEffect;
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
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

public class OoglyBooglyFumesParticle extends SpriteBillboardParticle {
	private final SpriteProvider provider;
	private final boolean deadly;
	private final Quaternionf rotationStorage = new Quaternionf();

	protected OoglyBooglyFumesParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ, OoglyBooglyFumesParticleEffect params, SpriteProvider provider) {
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.provider = provider;
		this.deadly = params.deadly();
		this.velocityX = 0;
		this.velocityY = 0.015f;
		this.velocityZ = 0;

		this.angle = MathHelper.PI * this.random.nextFloat() * 2f;
		this.lastAngle = angle;

		// Cool wheel effect thihng idk math at 2am is hard

		float offsetX = MathHelper.sin(this.angle);
		float offsetZ = MathHelper.cos(this.angle);
		this.setBoundingBox(this.getBoundingBox().offset(offsetX, 0, offsetZ));
		this.repositionFromBoundingBox();
		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;

		this.maxAge = 12;
		this.scale = 0.75f;

		int color = params.color();

		this.setColor(
			ColorHelper.getRedFloat(color),
			ColorHelper.getGreenFloat(color),
			ColorHelper.getBlueFloat(color)
		);
		this.setAlpha(ColorHelper.getAlphaFloat(color));

		this.setSpriteForAge(this.provider);
	}

	@Override
	public void tick() {
		super.tick();
		this.setSpriteForAge(this.provider);
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
		rotationStorage.rotationYXZ(MathHelper.lerp(tickProgress, this.lastAngle, this.angle), -MathHelper.PI / 4, 0f);
		this.render(vertexConsumer, camera, rotationStorage, tickProgress);

		rotationStorage.rotateY(MathHelper.PI);
		this.render(vertexConsumer, camera, rotationStorage, tickProgress);
	}

	@Override
	public int getBrightness(float tint) {
		if (this.deadly) {
			return 15728880; // LightmapTextureManager.pack(15, 15)
		}
		return 13631632; // LightmapTextureManager.pack(9, 13)
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<OoglyBooglyFumesParticleEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(OoglyBooglyFumesParticleEffect params, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
			return new OoglyBooglyFumesParticle(clientWorld, x, y, z, velX, velY, velZ, params, spriteProvider);
		}
	}
}
