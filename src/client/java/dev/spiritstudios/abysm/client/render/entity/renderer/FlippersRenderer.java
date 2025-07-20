package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.model.FlippersModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class FlippersRenderer implements ArmorRenderer {
	private static final Identifier TEXTURE = Abysm.id("textures/entity/armor/flippers.png");

	private FlippersModel model;

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, BipedEntityRenderState state, EquipmentSlot slot, int light, BipedEntityModel<BipedEntityRenderState> contextModel) {
		if (model == null)
			model = new FlippersModel(MinecraftClient.getInstance().getLoadedEntityModels().getModelPart(AbysmEntityLayers.FLIPPERS));

		model.setAngles(state);
		model.setVisible(false);

		model.leftLeg.visible = true;
		model.rightLeg.visible = true;

		model.rightFlipperSwimming.visible = state.isSwimming;
		model.leftFlipperSwimming.visible = state.isSwimming;

		model.rightFlipper.visible = !state.isSwimming;
		model.leftFlipper.visible = !state.isSwimming;

		ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, TEXTURE);
	}
}
