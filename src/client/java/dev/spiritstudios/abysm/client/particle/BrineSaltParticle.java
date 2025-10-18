package dev.spiritstudios.abysm.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AscendingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BrineSaltParticle extends AscendingParticle {

	private static final float RANDOM_COLOR_MULTIPLIER = 0.1F;
	private static final float MIN_SCALE = 1.0F;
	private static final float MAX_SCALE = 4.0F;
	private static final int COLOR = 0xFFFFF1C6;

	public float rotationSpeed;

	public BrineSaltParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
		super(world, x, y, z, 0.1F, 0.1F, 0.1F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 0.3F, 8, -0.1F, false);
		float f = this.random.nextFloat() * RANDOM_COLOR_MULTIPLIER;

		this.red = ColorHelper.getRedFloat(COLOR) - f;
		this.green = ColorHelper.getGreenFloat(COLOR) - f;
		this.blue = ColorHelper.getBlueFloat(COLOR) - f;
		this.rotationSpeed = f;

		this.scale(MathHelper.nextBetween(this.random, MIN_SCALE, MAX_SCALE));
	}

	@Override
	public void tick() {
		super.tick();

        if (this.dead)
			return;

        this.lastAngle = this.angle;
        this.angle = this.angle + MathHelper.TAU * this.rotationSpeed;

        this.rotationSpeed *= 0.99F;
    }

	@Override
	protected int getBrightness(float tint) {
		return LightmapTextureManager.MAX_LIGHT_COORDINATE;
	}

	@Override
	public float getSize(float tickProgress) {
		return MathHelper.lerp((this.age + tickProgress) / this.maxAge, this.scale, 0);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new BrineSaltParticle(clientWorld, d, e, f, g, h, i, 1.0F, this.spriteProvider);
		}

	}

}