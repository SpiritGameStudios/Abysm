package dev.spiritstudios.abysm.client.render.entity.model;

import dev.spiritstudios.abysm.client.render.entity.renderer.AbstractFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.SmallFloralFishEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

/**
 * Basic GeckoLib Entity Model for fish entities. Handles things like flapping the tail bone and rotating the body bone(constructor param) while swimming.<br><br>
 * You can also tell GeckoLib to turn the models head bone using the entity's pitch and yaw with the third constructor param.
 *
 * @see AbstractFishEntityRenderer
 * @see SmallFloralFishEntityRenderer.SmallFloralFishEntityModel
 */
public abstract class AbstractFishEntityModel<T extends GeoAnimatable> extends DefaultedEntityGeoModel<T> {
	// The names of the bones groups (the folders in BlockBench) to animate the swimming animation
	// The body animation is optional, defined in the constructor
	public static final String TAIL = "tail";
	public static final String BODY = "body";
	public static final String HEAD = "head";

	public boolean doNotAnimate = false;
	public boolean animateBodyAndTail;

	public AbstractFishEntityModel(Identifier assetSubpath, boolean animateBodyAndTail, boolean turnsHead) {
		super(assetSubpath, turnsHead);
		this.animateBodyAndTail = animateBodyAndTail;
	}

	public AbstractFishEntityModel(Identifier assetSubpath, boolean animateBodyAndTail) {
		this(assetSubpath, animateBodyAndTail, false);
	}

	@Override
	public void setCustomAnimations(AnimationState<T> animationState) {
		// Head turning animation
		super.setCustomAnimations(animationState);
		if (doNotAnimate) return;
//
//		LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
//
//		GeoBone tail = getAnimationProcessor().getBone(TAIL);
//		if (tail == null) return;
//
//		float tailYaw = getTailYaw(animationState);
//		tail.setRotY(tailYaw);
//
//		if (animateBodyAndTail) {
//			GeoBone body = getAnimationProcessor().getBone(BODY);
//			if (body == null) return;
//
//			GeoBone head = getAnimationProcessor().getBone(HEAD);
//			if (head == null) return;
//
//			float yawMultiplier = renderState.touchingWater ? 1f : 1.5f;
//
//			float theta = renderState.age * 0.33F;
//			float sineTheta = MathHelper.sin(theta);
//
//			float pitch = -animationState.getData(DataTickets.ENTITY_PITCH);
//			float yaw = animationState.getData(DataTickets.ENTITY_YAW);
//
//			head.setRotX(pitch * MathHelper.RADIANS_PER_DEGREE + 0.05F * sineTheta);
//
//			float bodyYaw = getBodyYaw(animationState);
//			body.setRotY(bodyYaw);
//		}
	}

	protected float getTailYaw(AnimationState<T> animationState) {
		LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
		float yawMultiplier = renderState.touchingWater ? 1f : 1.5f;
		float yawReducer = animateBodyAndTail ? 0.35f : 0.45f; // 0.45f is Minecraft's amount
		return -yawMultiplier * yawReducer * MathHelper.sin(0.6f * renderState.age);
	}

	protected float getBodyYaw(AnimationState<T> animationState) {
		LivingEntityRenderState renderState = (LivingEntityRenderState) animationState.renderState();
		float yawMultiplier = renderState.touchingWater ? 1f : 1.5f;
		return -yawMultiplier * 0.2f * MathHelper.sin(0.6f * renderState.age);
	}
}
