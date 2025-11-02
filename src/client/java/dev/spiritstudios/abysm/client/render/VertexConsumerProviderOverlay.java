package dev.spiritstudios.abysm.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class VertexConsumerProviderOverlay implements MultiBufferSource {

	private final MultiBufferSource parent;
	private final int multiplyRed;
	private final int multiplyGreen;
	private final int multiplyBlue;

	public VertexConsumerProviderOverlay(MultiBufferSource parent, int multiplyRed, int multiplyGreen, int multiplyBlue) {
		this.parent = parent;
		this.multiplyRed = multiplyRed;
		this.multiplyGreen = multiplyGreen;
		this.multiplyBlue = multiplyBlue;
	}

	@Override
	public VertexConsumer getBuffer(RenderType layer) {
		VertexConsumer vertexConsumer = this.parent.getBuffer(layer);
		return new VertexConsumerOverlay(vertexConsumer, this.multiplyRed, this.multiplyGreen, this.multiplyBlue);
	}
}
