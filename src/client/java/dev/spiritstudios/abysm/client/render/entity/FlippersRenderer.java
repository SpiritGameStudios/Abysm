package dev.spiritstudios.abysm.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.model.FlippersModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class FlippersRenderer implements ArmorRenderer {
	private static final Identifier TEXTURE = Abysm.id("textures/entity/armor/flippers.png");

	private FlippersModel model;

	@Override
	public void render(PoseStack poseStack, SubmitNodeCollector nodeCollector, ItemStack stack, HumanoidRenderState state, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
		if (model == null)
			model = new FlippersModel(Minecraft.getInstance().getEntityModels().bakeLayer(AbysmEntityLayers.FLIPPERS));

		model.setupAnim(state);
		model.setAllVisible(false);

		model.leftLeg.visible = true;
		model.rightLeg.visible = true;

		model.rightFlipperSwimming.visible = state.isVisuallySwimming;
		model.leftFlipperSwimming.visible = state.isVisuallySwimming;

		model.rightFlipper.visible = !state.isVisuallySwimming;
		model.leftFlipper.visible = !state.isVisuallySwimming;

		nodeCollector.submitModel(
			model,
			state,
			poseStack,
			RenderTypes.armorCutoutNoCull(TEXTURE),
			state.lightCoords,
			OverlayTexture.NO_OVERLAY,
			state.outlineColor,
			null
		);
	}
}
