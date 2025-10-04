package dev.spiritstudios.abysm.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.spiritstudios.abysm.client.duck.ClientPlayerEntityDuckInterface;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;

import java.util.OptionalInt;

public class LightmapAdjustment {
	public static final int UBO_SIZE = new Std140SizeCalculator()
		.putFloat()
		.get();

	public static void adjustLightmap(
		ClientPlayerEntity player,
		GpuTexture mainTexture,
		GpuTextureView mainTextureView,
		GpuTexture secondaryTexture,
		GpuTextureView secondaryTextureView,
		MappableRingBuffer uniformBuffer,
		float tickProgress
	) {
		float brightenSkyFactor = 0.0F; // at 0.0F does nothing, at 1.0F renders everything with maximum skylight

		var commandEncoder = RenderSystem.getDevice().createCommandEncoder();

		if (player.isSubmergedIn(FluidTags.WATER)) {
			brightenSkyFactor = ((ClientPlayerEntityDuckInterface) player).abysm$getUnderwaterAmbientSkyLight(tickProgress);
		}

		if (brightenSkyFactor > MathHelper.EPSILON) {
			try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(uniformBuffer.getBlocking(), false, true)) {
				Std140Builder.intoBuffer(mappedView.data())
					.putFloat(brightenSkyFactor);
			}

			RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
			GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(6);

			commandEncoder.copyTextureToTexture(mainTexture, secondaryTexture, 0, 0, 0, 0, 0, mainTexture.getWidth(0), mainTexture.getHeight(0));

			// update main texture
			try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Adjust lightmap", mainTextureView, OptionalInt.empty())) {
				renderPass.setPipeline(AbysmRenderPipelines.ADJUST_LIGHTMAP);
				RenderSystem.bindDefaultUniforms(renderPass);
				renderPass.setUniform("LightmapAdjustmentInfo", uniformBuffer.getBlocking());
				renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
				renderPass.setIndexBuffer(indexBuffer, shapeIndexBuffer.getIndexType());
				renderPass.bindSampler("InSampler", secondaryTextureView);
				renderPass.drawIndexed(0, 0, 6, 1);
			}
		}
	}
}
