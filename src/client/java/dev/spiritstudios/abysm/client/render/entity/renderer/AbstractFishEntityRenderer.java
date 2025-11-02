package dev.spiritstudios.abysm.client.render.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.TropicalFishRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

/**
 * Basic GeckoLib Entity Renderer for fish entities. Handles things like body rotation while swimming, and rotating sideways while on land.
 *
 * @see SmallFloralFishEntityRenderer
 */
@SuppressWarnings("JavadocReference")
public abstract class AbstractFishEntityRenderer<T extends Entity & GeoAnimatable, R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<T, R> {
	public AbstractFishEntityRenderer(EntityRendererProvider.Context context, GeoModel<T> model) {
		super(context, model);
	}

	/**
	 * Applies magic numbers pulled from {@link net.minecraft.client.renderer.entity.TropicalFishRenderer#setupRotations(TropicalFishRenderState, PoseStack, float, float)}, which include rotations as part of(but not fully!) the swimming animation, and the floundering/flopping animation when on land.
	 */
	@Override
	protected void applyRotations(R renderState, PoseStack matrixStack, float nativeScale) {
		super.applyRotations(renderState, matrixStack, nativeScale);
		float rotation = 4.3F * Mth.sin(0.6F * renderState.ageInTicks);
		matrixStack.mulPose(Axis.YP.rotationDegrees(rotation));
		if(!renderState.isInWater && this.shouldApplyFishLandTransforms()) {
			matrixStack.translate(0.2F, 0.1F, 0.0F);
			matrixStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
		}
	}

	protected boolean shouldApplyFishLandTransforms() {
		return true;
	}
}
