package dev.spiritstudios.abysm.client.render.entity.model;

import dev.spiritstudios.abysm.client.render.entity.AbstractFishEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

/**
 * Basic GeckoLib Entity Model for fish entities. Handles things like flapping the tail bone and rotating the body bone(constructor param) while swimming.<br><br>
 * You can also tell GeckoLib to turn the models head bone using the entity's pitch and yaw with the third constructor param.
 *
 * @see AbstractFishEntityRenderer
 * @see dev.spiritstudios.abysm.client.render.entity.SmallFloralFishEntityRenderer.SmallFloralFishEntityModel
 */
public abstract class AbstractFishEntityModel<T extends GeoAnimatable> extends DefaultedEntityGeoModel<T> {
	public static final DataTicket<Boolean> TEST_TICKET = DataTicket.create("test", Boolean.class);

	// The names of the bones groups (the folders in BlockBench) to animate the swimming animation
	// The body animation is optional, defined in the constructor
	public static final String TAIL = "tail";
	public static final String BODY = "body";

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
	public void addAdditionalStateData(T animatable, GeoRenderState renderState) {
		LivingEntity entity = (LivingEntity) animatable;
		renderState.addGeckolibData(TEST_TICKET, !entity.isBaby());
	}

	@Override
	public void setCustomAnimations(AnimationState<T> animationState) {
		// Head turning animation
		super.setCustomAnimations(animationState);
		if(doNotAnimate) return;

		GeoBone tail = getAnimationProcessor().getBone(TAIL);
		if (tail == null) return;

		float tailYaw = getTailYaw(animationState);
		tail.setRotY(tailYaw);

		if(animateBodyAndTail) {
			GeoBone body = getAnimationProcessor().getBone(BODY);
			if(body == null) return;

			float bodyYaw = getBodyYaw(animationState);
			body.setRotY(bodyYaw);
		}
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
