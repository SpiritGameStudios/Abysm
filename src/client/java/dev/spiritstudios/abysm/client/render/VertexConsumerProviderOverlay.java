package dev.spiritstudios.abysm.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

public class VertexConsumerProviderOverlay implements VertexConsumerProvider {

	private final VertexConsumerProvider parent;
	private final int multiplyRed;
	private final int multiplyGreen;
	private final int multiplyBlue;

	public VertexConsumerProviderOverlay(VertexConsumerProvider parent, int multiplyRed, int multiplyGreen, int multiplyBlue) {
		this.parent = parent;
		this.multiplyRed = multiplyRed;
		this.multiplyGreen = multiplyGreen;
		this.multiplyBlue = multiplyBlue;
	}

	@Override
	public VertexConsumer getBuffer(RenderLayer layer) {
		VertexConsumer vertexConsumer = this.parent.getBuffer(layer);
		return new VertexConsumerOverlay(vertexConsumer, this.multiplyRed, this.multiplyGreen, this.multiplyBlue);
	}
}
