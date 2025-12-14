package dev.spiritstudios.abysm.client.render;

import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public class AbysmRenderTypes {
	public static final RenderType MAN_O_WAR_TENTACLES = RenderType.create(
		"man_o_war_tentacles",
		RenderSetup.builder(AbysmRenderPipelines.MAN_O_WAR_TENTACLES)
			.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
			.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
			.useLightmap()
			.createRenderSetup()
	);
}
