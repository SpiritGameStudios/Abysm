package dev.spiritstudios.abysm.client.render.entity.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.client.render.GeoUtil;
import dev.spiritstudios.abysm.client.render.entity.renderer.ElectricOoglyBooglyRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.CustomBoneTextureGeoLayer;

public class ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer<T extends GeoAnimatable, O, R extends GeoRenderState> extends CustomBoneTextureGeoLayer<T, O, R> {
	public ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer(GeoRenderer<T, O, R> renderer) {
		super(renderer, "electricity", ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png"));
	}

	// Cancel this feature layer rendering if the oogly boogly isn't blowing up with its mind
	@Override
	protected void renderBone(R state, PoseStack poseStack, GeoBone bone, @Nullable RenderType renderType, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int renderColor) {
		if (!GeoUtil.getOrDefaultGeoData(state, ElectricOoglyBooglyRenderer.IS_BLOWING_UP_WITH_MIND, false)) return;

		super.renderBone(state, poseStack, bone, renderType, bufferSource, packedLight, packedOverlay, renderColor);
	}

	@Override
	protected RenderType getRenderType(R state, ResourceLocation texture) {
		double age = GeoUtil.getOrDefaultGeoData(state, DataTickets.TICK, 1.0);
		int blowingUpTicks = GeoUtil.getOrDefaultGeoData(state, ElectricOoglyBooglyRenderer.BLOWING_UP_WITH_MIND_TICKS, 0);

		return RenderType.energySwirl(texture, getX(age, blowingUpTicks) % 1, getY(age, blowingUpTicks) % 1);
	}

	private float getX(double age, int blowingUpTicks) {
		float speedIncrease = Mth.clamp(blowingUpTicks * 0.0001f, 0f, 0.05f);
		float speed = 0.01f + speedIncrease;
		return (float) (age * speed);
	}

	private float getY(double age, int blowingUpTicks) {
		float speedIncrease = Mth.clamp(blowingUpTicks * 0.00005f, 0f, 0.025f);
		float speed = 0.01f + speedIncrease;
		return (float) (age * speed);
	}

}
