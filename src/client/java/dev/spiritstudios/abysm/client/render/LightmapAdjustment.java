package dev.spiritstudios.abysm.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.spiritstudios.abysm.client.duck.ClientPlayerEntityDuckInterface;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;

import java.util.OptionalInt;

public class LightmapAdjustment {

	public static void adjustLightmap(ClientPlayerEntity player, GpuTexture mainTexture, GpuTexture secondaryTexture, float tickProgress) {
		float brightenSkyFactor = 0.0F; // at 0.0F does nothing, at 1.0F renders everything with maximum skylight

		if (player.isSubmergedIn(FluidTags.WATER)) {
			brightenSkyFactor = ((ClientPlayerEntityDuckInterface) player).abysm$getUnderwaterAmbientSkyLight(tickProgress);
		}

		if (brightenSkyFactor > MathHelper.EPSILON) {
			RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
			GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(6);

			// copy texture to secondary buffer
			try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(secondaryTexture, OptionalInt.empty())) {
				renderPass.setPipeline(RenderPipelines.ENTITY_OUTLINE_BLIT);
				renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
				renderPass.setIndexBuffer(indexBuffer, shapeIndexBuffer.getIndexType());
				renderPass.bindSampler("InSampler", mainTexture);
				renderPass.drawIndexed(0, 6);
			}

			// update main texture
			try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(mainTexture, OptionalInt.empty())) {
				renderPass.setPipeline(AbysmRenderPipelines.ADJUST_LIGHTMAP);
				renderPass.setUniform("BrightenSkyFactor", brightenSkyFactor);
				renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
				renderPass.setIndexBuffer(indexBuffer, shapeIndexBuffer.getIndexType());
				renderPass.bindSampler("InSampler", secondaryTexture);
				renderPass.drawIndexed(0, 6);
			}
		}
	}
}
