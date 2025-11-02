package dev.spiritstudios.abysm.client.render;

import java.util.OptionalDouble;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import static net.minecraft.client.renderer.RenderStateShard.LIGHTMAP;
import static net.minecraft.client.renderer.RenderStateShard.ITEM_ENTITY_TARGET;
import static net.minecraft.client.renderer.RenderStateShard.VIEW_OFFSET_Z_LAYERING;

public class AbysmRenderLayers {
	public static final RenderType.CompositeRenderType MAN_O_WAR_TENTACLES = RenderType.create(
		"man_o_war_tentacles",
		1536,
		AbysmRenderPipelines.MAN_O_WAR_TENTACLES,
		RenderType.CompositeState.builder()
			.setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(2)))
			.setLayeringState(VIEW_OFFSET_Z_LAYERING)
			.setOutputState(ITEM_ENTITY_TARGET)
			.setLightmapState(LIGHTMAP)
			.createCompositeState(false)
	);
}
