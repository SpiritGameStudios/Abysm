package dev.spiritstudios.abysm.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class AbysmRenderPipelines {

	public static final RenderPipeline ADJUST_LIGHTMAP = register(
		RenderPipeline.builder()
			.withLocation(id("pipeline/adjust_lightmap"))
			.withVertexShader("core/blit_screen")
			.withFragmentShader(id("core/adjust_lightmap"))
			.withSampler("InSampler")
			.withUniform("BrightenSkyFactor", UniformType.FLOAT)
			.withDepthWrite(false)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS)
			.build()
	);

	private static RenderPipeline register(RenderPipeline pipeline) {
		return RenderPipelines.register(pipeline);
	}

	private static Identifier id(String path) {
		return Abysm.id(path);
	}

	public static void init() {
		// NO-OP
	}
}
