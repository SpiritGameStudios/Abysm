package dev.spiritstudios.abysm.client.render.entity.feature;

import dev.spiritstudios.abysm.client.render.entity.ElectricOoglyBooglyRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.CustomBoneTextureGeoLayer;

public class ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer<T extends GeoAnimatable, O, R extends GeoRenderState> extends CustomBoneTextureGeoLayer<T, O, R> {
	public ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer(GeoRenderer<T, O, R> renderer) {
//		super(renderer, "electricity", Abysm.id("textures/item/rosebloom_petals.png"));
		super(renderer, "electricity", Identifier.ofVanilla("textures/entity/creeper/creeper_armor.png"));
	}

	public boolean shouldRender(R renderState) {
		return renderState.getOrDefaultGeckolibData(ElectricOoglyBooglyRenderer.IS_BLOWING_UP_WITH_MIND, false);
	}

	// Cancel this feature layer rendering if the oogly boogly isn't blowing up with its mind
	@Override
	protected void renderBone(R renderState, MatrixStack poseStack, GeoBone bone, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay, int renderColor) {
		if(!shouldRender(renderState)) return;
		super.renderBone(renderState, poseStack, bone, renderType, bufferSource, packedLight, packedOverlay, renderColor);
	}

	@Override
	public void preRender(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
		if(!shouldRender(renderState)) return;
		super.preRender(renderState, poseStack, bakedModel, renderType, bufferSource, buffer, packedLight, packedOverlay, renderColor);
	}

	@Override
	protected RenderLayer getRenderType(R renderState, Identifier texture) {
		double age = renderState.getOrDefaultGeckolibData(DataTickets.TICK, 1.0);
		int blowingUpTicks = renderState.getOrDefaultGeckolibData(ElectricOoglyBooglyRenderer.BLOWING_UP_WITH_MIND_TICKS, 0);
		return RenderLayer.getEnergySwirl(texture, getX(age, blowingUpTicks) % 1, getY(age, blowingUpTicks) % 1);
	}

	private float getX(double age, int blowingUpTicks) {
		float speedIncrease = MathHelper.clamp(blowingUpTicks * 0.0001f, 0f, 0.05f);
		float speed = 0.01f + speedIncrease;
		return (float) (age * speed);
	}

	private float getY(double age, int blowingUpTicks) {
		float speedIncrease = MathHelper.clamp(blowingUpTicks * 0.00005f, 0f, 0.025f);
		float speed = 0.01f + speedIncrease;
		return (float) (age * speed);
	}

}
