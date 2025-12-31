package dev.spiritstudios.abysm.client.particle;

import dev.spiritstudios.abysm.core.particles.OoglyBooglyFumesParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class OoglyBooglyFumesParticle extends SimpleAnimatedParticle {
	private final SpriteSet provider;
	private final boolean deadly;
	private final Quaternionf rotationStorage = new Quaternionf();

	protected OoglyBooglyFumesParticle(ClientLevel level, double x, double y, double z, OoglyBooglyFumesParticleEffect params, SpriteSet sprites) {
		super(level, x, y, z, sprites, 0.0F);
		this.provider = sprites;
		this.deadly = params.deadly();
		this.xd = 0;
		this.yd = 0.015f;
		this.zd = 0;

		this.roll = Mth.PI * this.random.nextFloat() * 2f;
		this.oRoll = roll;

		// Cool wheel effect thihng idk math at 2am is hard

		float offsetX = Mth.sin(this.roll);
		float offsetZ = Mth.cos(this.roll);
		this.setBoundingBox(this.getBoundingBox().move(offsetX, 0, offsetZ));
		this.setLocationFromBoundingbox();
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		this.lifetime = 12;
		this.quadSize = 0.75f;

		int color = params.color();

		this.setColor(
			ARGB.redFloat(color),
			ARGB.greenFloat(color),
			ARGB.blueFloat(color)
		);
		this.setAlpha(ARGB.alphaFloat(color));

		this.setSpriteFromAge(this.provider);
	}

	@Override
	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.provider);
	}

	@Override
	public void extract(QuadParticleRenderState reusedState, Camera camera, float partialTick) {
		rotationStorage.rotationYXZ(Mth.lerp(partialTick, this.oRoll, this.roll), -Mth.PI / 4, 0f);
		this.extractRotatedQuad(reusedState, camera, rotationStorage, partialTick);

		rotationStorage.rotateY(Mth.PI);
		this.extractRotatedQuad(reusedState, camera, rotationStorage, partialTick);
	}

	@Override
	public int getLightColor(float tint) {
		if (this.deadly) {
			return 15728880; // LightmapTextureManager.pack(15, 15)
		}
		return 13631632; // LightmapTextureManager.pack(9, 13)
	}

	@Override
	public @NotNull Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<OoglyBooglyFumesParticleEffect> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public @Nullable Particle createParticle(OoglyBooglyFumesParticleEffect particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			return new OoglyBooglyFumesParticle(level, x, y, z, particleType, spriteProvider);
		}
	}
}
