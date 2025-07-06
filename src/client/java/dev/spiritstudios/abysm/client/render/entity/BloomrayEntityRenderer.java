package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.entity.floralreef.BloomrayEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BloomrayEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<BloomrayEntity, R> {
	public static final DataTicket<BloomrayEntityVariant> VARIANT_TICKET = DataTicket.create("bloomray_variant_ticket", BloomrayEntityVariant.class);

	public BloomrayEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BloomrayEntityModel());
		this.withScale(3f);
	}

	@Override
	public Identifier getTextureLocation(R renderState) {
		BloomrayEntityVariant variant = renderState.getGeckolibData(VARIANT_TICKET);
		if (variant != null && variant.getTexture() != null) return variant.getTexture();

		return MissingSprite.getMissingSpriteId();
	}

	public static class BloomrayEntityModel extends DefaultedEntityGeoModel<BloomrayEntity> {
		public static final String BODY = "body";
		public static final String TAIL = "tail";
		public static final String LEFT_FIN = "left";
		public static final String RIGHT_FIN = "right";
		public static final String LEFT_CROWN_SPIKE = "left2";
		public static final String RIGHT_CROWN_SPIKE = "right2";
		public static final String LEFT_ANTENNA = "left3";
		public static final String RIGHT_ANTENNA = "right3";

		public boolean doNotAnimate = false;

		public BloomrayEntityModel() {
			super(Abysm.id("bloomray"), true);
		}

		@Override
		public void addAdditionalStateData(BloomrayEntity animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			renderState.addGeckolibData(VARIANT_TICKET, animatable.getVariant());
		}

		@Override
		public void setCustomAnimations(AnimationState<BloomrayEntity> animationState) {
			super.setCustomAnimations(animationState);

			if (this.doNotAnimate) return;

			// animate tail
			GeoBone tail = getAnimationProcessor().getBone(TAIL);
			if (tail == null) return;

			float tailYaw = getTailYaw(animationState);
			tail.setRotY(tailYaw);

			// animate fins
			GeoBone leftFin = getAnimationProcessor().getBone(LEFT_FIN);
			GeoBone rightFin = getAnimationProcessor().getBone(RIGHT_FIN);
			if (leftFin == null || rightFin == null) return;

			float finPitch = getFinPitch(animationState);
			leftFin.setRotZ(finPitch);
			rightFin.setRotZ(-finPitch);

			// animate... antennas? What do you call the things on top of them that are based on the crown flowers?
			GeoBone leftAntenna = getAnimationProcessor().getBone(LEFT_ANTENNA);
			GeoBone rightAntenna = getAnimationProcessor().getBone(RIGHT_ANTENNA);
			if (leftAntenna == null || rightAntenna == null) return;

			float antennaPitch = getAntennaPitch(animationState);
			leftAntenna.setRotZ(antennaPitch);
			rightAntenna.setRotZ(-antennaPitch);

			GeoBone leftSpike = getAnimationProcessor().getBone(LEFT_CROWN_SPIKE);
			GeoBone rightSpike = getAnimationProcessor().getBone(RIGHT_CROWN_SPIKE);
			if (leftSpike == null || rightSpike == null) return;

			float spikePitch = getCrownSpikeYaw(animationState);
			leftSpike.setRotZ(spikePitch);
			rightSpike.setRotZ(-spikePitch);
		}

		private float getTailYaw(AnimationState<BloomrayEntity> animationState) {
			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
			float yawMultiplier = renderState.touchingWater ? 0.15f : 0.35f;
			return -yawMultiplier * MathHelper.sin(0.2f * renderState.age);
		}

		private float getFinPitch(AnimationState<BloomrayEntity> animationState) {
			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
			if (!renderState.touchingWater) return 0f;
			return 0.25f * MathHelper.sin(0.1f * renderState.age);
		}

		private float getAntennaPitch(AnimationState<BloomrayEntity> animationState) {
			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
			if (!renderState.touchingWater) return 0f;
			if (renderState.age % 1000 <= 20) return 0.5f + 0.5f * MathHelper.sin(0.5f * renderState.age);
			return 0.5f + 0.07f * MathHelper.sin(0.3f * renderState.age);
		}

		private float getCrownSpikeYaw(AnimationState<BloomrayEntity> animationState) {
			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
			if (!renderState.touchingWater) return 0f;
			if ((renderState.age + 200) % 1000 <= 20) return 0.35f + 0.15f * MathHelper.sin(renderState.age);
			return 0.35f + 0.1f * MathHelper.sin(0.2f * renderState.age);
		}
	}
}
