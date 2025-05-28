package dev.spiritstudios.abysm.client.render.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.TropicalFishEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

/**
 * Basic GeckoLib Entity Renderer for fish entities. Handles things like body rotation while swimming, and rotating sideways while on land.
 *
 * @see AbstractFishEntityModel
 * @see SmallFloralFishEntityRenderer
 */
public abstract class AbstractFishEntityRenderer<T extends Entity & GeoAnimatable, R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<T, R> {
	public AbstractFishEntityRenderer(EntityRendererFactory.Context context, GeoModel<T> model) {
		super(context, model);
	}

	/**
	 * Applies magic numbers pulled from {@link net.minecraft.client.render.entity.TropicalFishEntityRenderer#setupTransforms(TropicalFishEntityRenderState, MatrixStack, float, float)}, which include rotations as part of(but not fully!) the swimming animation, and the floundering/flopping animation when on land.
	 */
	@Override
	protected void applyRotations(R renderState, MatrixStack matrixStack, float nativeScale) {
		super.applyRotations(renderState, matrixStack, nativeScale);
		float rotation = 4.3F * MathHelper.sin(0.6F * renderState.age);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
		if(!renderState.touchingWater) {
			matrixStack.translate(0.2F, 0.1F, 0.0F);
			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
		}
	}
}
