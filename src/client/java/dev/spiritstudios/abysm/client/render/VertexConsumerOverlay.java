package dev.spiritstudios.abysm.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class VertexConsumerOverlay implements VertexConsumer {

	private final VertexConsumer parent;
	private final int multiplyRed;
	private final int multiplyGreen;
	private final int multiplyBlue;

	public VertexConsumerOverlay(VertexConsumer parent, int multiplyRed, int multiplyGreen, int multiplyBlue) {
		this.parent = parent;
		this.multiplyRed = multiplyRed;
		this.multiplyGreen = multiplyGreen;
		this.multiplyBlue = multiplyBlue;
	}

	@Override
	public VertexConsumer addVertex(float x, float y, float z) {
		return this.parent.addVertex(x, y, z);
	}

	@Override
	public VertexConsumer setColor(int red, int green, int blue, int alpha) {
		return this.parent.setColor(
			red * this.multiplyRed / 255,
			green * this.multiplyGreen / 255,
			blue * this.multiplyBlue / 255,
			alpha
		);
	}

	@Override
	public VertexConsumer setUv(float u, float v) {
		return this.parent.setUv(u, v);
	}

	@Override
	public VertexConsumer setUv1(int u, int v) {
		return this.parent.setUv1(u, v);
	}

	@Override
	public VertexConsumer setUv2(int u, int v) {
		return this.parent.setUv2(u, v);
	}

	@Override
	public VertexConsumer setNormal(float x, float y, float z) {
		return this.parent.setNormal(x, y, z);
	}
}
