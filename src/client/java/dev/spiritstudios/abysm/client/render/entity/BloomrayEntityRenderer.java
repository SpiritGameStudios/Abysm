package dev.spiritstudios.abysm.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.BloomrayRenderState;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.world.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.spectre.api.client.model.animation.AnimationLocation;
import dev.spiritstudios.spectre.api.client.model.animation.SpectreKeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class BloomrayEntityRenderer extends VariantMobRenderer<BloomrayEntity, BloomrayEntityVariant, BloomrayRenderState, BloomrayEntityRenderer.Model> {
	public BloomrayEntityRenderer(EntityRendererProvider.Context context) {
		super(
			context, new Model(
				context,
				new ModelLayerLocation(Abysm.id("bloomray"), "main"),
				Abysm.id("bloomray"),
				"swim", "idle",
				"crown_wiggle", "crown_sway"
			),
			1F
		);
	}

	@Override
	public BloomrayRenderState createRenderState() {
		return new BloomrayRenderState();
	}

	@Override
	public void extractRenderState(BloomrayEntity entity, BloomrayRenderState renderState, float partialTick) {
		super.extractRenderState(entity, renderState, partialTick);

		renderState.query.set(entity, partialTick);
		renderState.antenna.copyFrom(entity.antenna);
	}

	@Override
	protected void setupRotations(BloomrayRenderState renderState, PoseStack poseStack, float bodyRot, float scale) {
		super.setupRotations(renderState, poseStack, bodyRot, scale);
		poseStack.mulPose(Axis.XP.rotationDegrees(-renderState.xRot));
	}

	public static class Model extends EntityModel<BloomrayRenderState> {
		private final Map<String, SpectreKeyframeAnimation> bakedAnimations = new HashMap<>();

		private final SpectreKeyframeAnimation swim;

		protected Model(
			EntityRendererProvider.Context context,
			ModelLayerLocation layerLocation,
			Identifier animationSet,
			String... animations
		) {
			super(context.bakeLayer(layerLocation));

			for (String animation : animations) {
				bakedAnimations.put(
					animation,
					context.bakeAnimation(new AnimationLocation(animationSet, animation), root())
				);
			}

			this.swim = context.bakeAnimation(new AnimationLocation(animationSet, "swim"), root());
		}

		@Override
		public void setupAnim(BloomrayRenderState renderState) {
			super.setupAnim(renderState);

			var query = renderState.query;

			swim.applyWalk(query, renderState.walkAnimationPos, renderState.walkAnimationSpeed, 1F, 1F);
			renderState.antenna.apply(bakedAnimations, query, renderState.ageInTicks);
		}
	}

	//		@Override
//		public void setCustomAnimations(AnimationState<BloomrayEntity> animationState) {
//			if (this.doNotAnimate) return;
//
//			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//
//			GeoBone head = getAnimationProcessor().getBone(HEAD);
//			if (head == null) return;
//
//			float pitch = -animationState.getData(DataTickets.ENTITY_PITCH);
//
//			GeoBone body = getAnimationProcessor().getBone(BODY);
//			if (body == null) return;
//
//			float theta = renderState.ageInTicks * 0.33F;
//			float sineTheta = Mth.sin(theta);
//			float cosTheta = Mth.cos(theta);
//
//			head.setRotX(-(0.13F * sineTheta) * 1.2F);
//
//			float yaw = animationState.getData(DataTickets.ENTITY_YAW);
//
//			body.setRotX(pitch * Mth.DEG_TO_RAD + 0.05F * sineTheta);
//			body.setRotY(yaw * Mth.DEG_TO_RAD);
//
//			// animate tail
//			GeoBone tail = getAnimationProcessor().getBone(TAIL);
//			if (tail == null) return;
//
//			float tailYaw = getTailYaw(animationState);
//			tail.setRotX(-(0.13F * sineTheta) * 1.8F);
//			tail.setRotY(tailYaw);
//
//			// animate fins
//			GeoBone leftFin = getAnimationProcessor().getBone(LEFT_FIN);
//			GeoBone rightFin = getAnimationProcessor().getBone(RIGHT_FIN);
//			if (leftFin == null || rightFin == null) return;
//
//			leftFin.setRotY((0.13F * cosTheta) * 0.5F);
//			rightFin.setRotY(-(0.13F * cosTheta) * 0.5F);
//
//			leftFin.setRotX(-(0.13F * sineTheta) * 0.5F);
//			rightFin.setRotX(-(0.13F * sineTheta) * 0.5F);
//
//			leftFin.setRotZ((0.13F * sineTheta) * 1.25F);
//			rightFin.setRotZ(-(0.13F * sineTheta) * 1.25F);
//
//			// animate... antennas? What do you call the things on top of them that are based on the crown flowers?
//			GeoBone leftAntenna = getAnimationProcessor().getBone(LEFT_ANTENNA);
//			GeoBone rightAntenna = getAnimationProcessor().getBone(RIGHT_ANTENNA);
//			if (leftAntenna == null || rightAntenna == null) return;
//
//			float antennaPitch = getAntennaPitch(animationState);
//			leftAntenna.setRotZ(antennaPitch);
//			rightAntenna.setRotZ(-antennaPitch);
//
//			GeoBone leftSpike = getAnimationProcessor().getBone(LEFT_CROWN_SPIKE);
//			GeoBone rightSpike = getAnimationProcessor().getBone(RIGHT_CROWN_SPIKE);
//			if (leftSpike == null || rightSpike == null) return;
//
//			float spikePitch = getCrownSpikeYaw(animationState);
//			leftSpike.setRotZ(spikePitch);
//			rightSpike.setRotZ(-spikePitch);
//		}

//		private float getTailYaw(AnimationState<BloomrayEntity> animationState) {
//			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//			float yawMultiplier = renderState.isInWater ? 0.15f : 0.35f;
//			return -yawMultiplier * Mth.sin(0.2f * renderState.ageInTicks);
//		}
//
//		private float getFinRoll(AnimationState<BloomrayEntity> animationState) {
//			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//			if (!renderState.isInWater) return 0f;
//			return 0.25f * Mth.sin(0.2f * renderState.ageInTicks);
//		}
//
//		private float getFinPitch(AnimationState<BloomrayEntity> animationState) {
//			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//			if (!renderState.isInWater) return 0f;
//			return 0.05f * Mth.sin(0.2f * renderState.ageInTicks);
//		}
//
//		private float getFinYaw(AnimationState<BloomrayEntity> animationState) {
//			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//			if (!renderState.isInWater) return 0f;
//			return 0.05f * Mth.sin(0.2f * renderState.ageInTicks);
//		}
//
//		private float getAntennaPitch(AnimationState<BloomrayEntity> animationState) {
//			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//			if (!renderState.isInWater) return 0f;
//			if (renderState.ageInTicks % 1000 <= 20) return 0.5f + 0.5f * Mth.sin(0.5f * renderState.ageInTicks);
//			return 0.5f + 0.07f * Mth.sin(0.3f * renderState.ageInTicks);
//		}
//
//		private float getCrownSpikeYaw(AnimationState<BloomrayEntity> animationState) {
//			LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//			if (!renderState.isInWater) return 0f;
//			if ((renderState.ageInTicks + 200) % 1000 <= 20) return 0.35f + 0.15f * Mth.sin(renderState.ageInTicks);
//			return 0.35f + 0.1f * Mth.sin(0.2f * renderState.ageInTicks);
//		}

}
