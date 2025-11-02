package dev.spiritstudios.abysm.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.spiritstudios.abysm.particle.OoglyBooglyFumesParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class OoglyBooglyFumesParticle extends TextureSheetParticle {
	private final SpriteSet provider;
	private final boolean deadly;
	private final Quaternionf rotationStorage = new Quaternionf();

	protected OoglyBooglyFumesParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, OoglyBooglyFumesParticleEffect params, SpriteSet provider) {
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.provider = provider;
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
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
		rotationStorage.rotationYXZ(Mth.lerp(tickProgress, this.oRoll, this.roll), -Mth.PI / 4, 0f);
		this.renderRotatedQuad(vertexConsumer, camera, rotationStorage, tickProgress);

		rotationStorage.rotateY(Mth.PI);
		this.renderRotatedQuad(vertexConsumer, camera, rotationStorage, tickProgress);
	}

	@Override
	public int getLightColor(float tint) {
		if (this.deadly) {
			return 15728880; // LightmapTextureManager.pack(15, 15)
		}
		return 13631632; // LightmapTextureManager.pack(9, 13)
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<OoglyBooglyFumesParticleEffect> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(OoglyBooglyFumesParticleEffect params, ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
			return new OoglyBooglyFumesParticle(clientWorld, x, y, z, velX, velY, velZ, params, spriteProvider);
		}
	}
}
