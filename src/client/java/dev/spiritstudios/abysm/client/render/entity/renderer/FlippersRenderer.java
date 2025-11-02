package dev.spiritstudios.abysm.client.render.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.model.FlippersModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class FlippersRenderer implements ArmorRenderer {
	private static final ResourceLocation TEXTURE = Abysm.id("textures/entity/armor/flippers.png");

	private FlippersModel model;

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, HumanoidRenderState state, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
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

		ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, TEXTURE);
	}
}
