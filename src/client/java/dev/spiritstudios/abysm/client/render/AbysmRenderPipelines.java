package dev.spiritstudios.abysm.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderPipelines;

import static dev.spiritstudios.abysm.Abysm.id;
import static net.minecraft.client.renderer.RenderPipelines.GLOBALS_SNIPPET;
import static net.minecraft.client.renderer.RenderPipelines.MATRICES_FOG_SNIPPET;

public final class AbysmRenderPipelines {
	public static final RenderPipeline ADJUST_LIGHTMAP = register(
		RenderPipeline.builder()
			.withLocation(id("pipeline/adjust_lightmap"))
			.withVertexShader("core/screenquad")
			.withFragmentShader(id("core/adjust_lightmap"))
			.withSampler("InSampler")
			.withUniform("LightmapAdjustmentInfo", UniformType.UNIFORM_BUFFER)
			.withDepthWrite(false)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
			.build()
	);

	public static final VertexFormat POSITION_COLOR_NORMAL_LIGHT = VertexFormat.builder()
		.add("Position", VertexFormatElement.POSITION)
		.add("Color", VertexFormatElement.COLOR)
		.add("Normal", VertexFormatElement.NORMAL)
		.add("UV2", VertexFormatElement.UV2)
		.build();

	public static final RenderPipeline MAN_O_WAR_TENTACLES = register(
		RenderPipeline.builder(MATRICES_FOG_SNIPPET, GLOBALS_SNIPPET)
			.withLocation(id("pipeline/man_o_war_tentacles"))
			.withVertexShader(id("core/rendertype_man_o_war_tentacles"))
			.withFragmentShader("core/rendertype_lines")
			.withSampler("Sampler2")
			.withBlend(BlendFunction.TRANSLUCENT)
			.withCull(true)
			.withVertexFormat(POSITION_COLOR_NORMAL_LIGHT, VertexFormat.Mode.LINES)
			.build()
	);

	private static RenderPipeline register(RenderPipeline pipeline) {
		return RenderPipelines.register(pipeline);
	}

	public static void init() {
		// NO-OP
	}
}
