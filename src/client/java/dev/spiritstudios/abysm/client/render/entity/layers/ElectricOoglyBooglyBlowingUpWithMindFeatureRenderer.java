package dev.spiritstudios.abysm.client.render.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.client.render.entity.ElectricOoglyBooglyRenderer;
import dev.spiritstudios.abysm.client.render.entity.state.ElectricOoglyBooglyRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer extends RenderLayer<ElectricOoglyBooglyRenderState, ElectricOoglyBooglyRenderer.Model> {
	public static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

	private final EntityModel<ElectricOoglyBooglyRenderState> model;

	public ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer(RenderLayerParent<ElectricOoglyBooglyRenderState, ElectricOoglyBooglyRenderer.Model> renderer, EntityModel<ElectricOoglyBooglyRenderState> model) {
		super(renderer);
		this.model = model;
	}

	private float getX(double age, float blowingUpTicks) {
		float speedIncrease = Mth.clamp(blowingUpTicks * 0.0001f, 0f, 0.05f);
		float speed = 0.01f + speedIncrease;
		return (float) (age * speed);
	}

	private float getY(double age, float blowingUpTicks) {
		float speedIncrease = Mth.clamp(blowingUpTicks * 0.00005f, 0f, 0.025f);
		float speed = 0.01f + speedIncrease;
		return (float) (age * speed);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, ElectricOoglyBooglyRenderState renderState, float yRot, float xRot) {
		if (renderState.isBlowingUpWithMind) {
			float age = renderState.ageInTicks;
			float blowingUpTicks = renderState.blowingUpWithMindTicks;

			nodeCollector.order(1).submitModel(
				model,
				renderState,
				poseStack,
				RenderTypes.energySwirl(TEXTURE_LOCATION, getX(age, blowingUpTicks) % 1, getY(age, blowingUpTicks) % 1),
				packedLight,
				OverlayTexture.NO_OVERLAY,
				-8355712,
				null,
				renderState.outlineColor,
				null
			);
		}
	}
}
