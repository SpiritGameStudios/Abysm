package dev.spiritstudios.abysm.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;

import java.util.OptionalDouble;

import static net.minecraft.client.render.RenderPhase.ENABLE_LIGHTMAP;
import static net.minecraft.client.render.RenderPhase.ITEM_ENTITY_TARGET;
import static net.minecraft.client.render.RenderPhase.VIEW_OFFSET_Z_LAYERING;

public class AbysmRenderLayers {
	public static final RenderLayer.MultiPhase MAN_O_WAR_TENTACLES = RenderLayer.of(
		"man_o_war_tentacles",
		1536,
		AbysmRenderPipelines.MAN_O_WAR_TENTACLES,
		RenderLayer.MultiPhaseParameters.builder()
			.lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(2)))
			.layering(VIEW_OFFSET_Z_LAYERING)
			.target(ITEM_ENTITY_TARGET)
			.lightmap(ENABLE_LIGHTMAP)
			.build(false)
	);
}
