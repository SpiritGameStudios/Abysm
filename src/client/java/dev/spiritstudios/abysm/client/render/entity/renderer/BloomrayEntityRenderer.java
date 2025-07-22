package dev.spiritstudios.abysm.client.render.entity.renderer;

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
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BloomrayEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<BloomrayEntity, R> {
	public static final DataTicket<BloomrayEntityVariant> VARIANT_TICKET = DataTicket.create("bloomray_variant_ticket", BloomrayEntityVariant.class);

	public BloomrayEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BloomrayEntityModel());
		this.withScale(2.0f);
	}

	@Override
	public Identifier getTextureLocation(R renderState) {
		BloomrayEntityVariant variant = renderState.getGeckolibData(VARIANT_TICKET);
		if (variant != null && variant.getTexture() != null) return variant.getTexture();

		return MissingSprite.getMissingSpriteId();
	}

	public static class BloomrayEntityModel extends DefaultedEntityGeoModel<BloomrayEntity> {
		public static final String BODY = "body";
		public static final String HEAD = "head";
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
			if (this.doNotAnimate) return;

			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();

			GeoBone head = getAnimationProcessor().getBone(HEAD);
			if (head == null) return;

			float pitch = -animationState.getData(DataTickets.ENTITY_PITCH);

			GeoBone body = getAnimationProcessor().getBone(BODY);
			if (body == null) return;

			float theta = renderState.age * 0.33F;
			float sineTheta = MathHelper.sin(theta);
			float cosTheta = MathHelper.cos(theta);

			head.setRotX(-(0.13F * sineTheta) * 1.2F);

			float yaw = animationState.getData(DataTickets.ENTITY_YAW);

			body.setRotX(pitch * MathHelper.RADIANS_PER_DEGREE + 0.05F * sineTheta);
			body.setRotY(yaw * MathHelper.RADIANS_PER_DEGREE);

			// animate tail
			GeoBone tail = getAnimationProcessor().getBone(TAIL);
			if (tail == null) return;

			float tailYaw = getTailYaw(animationState);
			tail.setRotX(-(0.13F * sineTheta) * 1.8F);
			tail.setRotY(tailYaw);

			// animate fins
			GeoBone leftFin = getAnimationProcessor().getBone(LEFT_FIN);
			GeoBone rightFin = getAnimationProcessor().getBone(RIGHT_FIN);
			if (leftFin == null || rightFin == null) return;

			leftFin.setRotY((0.13F * cosTheta) * 0.5F);
			rightFin.setRotY(-(0.13F * cosTheta) * 0.5F);

			leftFin.setRotX(-(0.13F * sineTheta) * 0.5F);
			rightFin.setRotX(-(0.13F * sineTheta) * 0.5F);

			leftFin.setRotZ((0.13F * sineTheta) * 1.25F);
			rightFin.setRotZ(-(0.13F * sineTheta) * 1.25F);

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

		private float getFinRoll(AnimationState<BloomrayEntity> animationState) {
			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
			if (!renderState.touchingWater) return 0f;
			return 0.25f * MathHelper.sin(0.2f * renderState.age);
		}

		private float getFinPitch(AnimationState<BloomrayEntity> animationState) {
			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
			if (!renderState.touchingWater) return 0f;
			return 0.05f * MathHelper.sin(0.2f * renderState.age);
		}

		private float getFinYaw(AnimationState<BloomrayEntity> animationState) {
			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
			if (!renderState.touchingWater) return 0f;
			return 0.05f * MathHelper.sin(0.2f * renderState.age);
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
