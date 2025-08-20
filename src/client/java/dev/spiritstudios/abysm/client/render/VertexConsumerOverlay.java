package dev.spiritstudios.abysm.client.render;

import net.minecraft.client.render.VertexConsumer;

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
	public VertexConsumer vertex(float x, float y, float z) {
		return this.parent.vertex(x, y, z);
	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {
		return this.parent.color(
			red * this.multiplyRed / 255,
			green * this.multiplyGreen / 255,
			blue * this.multiplyBlue / 255,
			alpha
		);
	}

	@Override
	public VertexConsumer texture(float u, float v) {
		return this.parent.texture(u, v);
	}

	@Override
	public VertexConsumer overlay(int u, int v) {
		return this.parent.overlay(u, v);
	}

	@Override
	public VertexConsumer light(int u, int v) {
		return this.parent.light(u, v);
	}

	@Override
	public VertexConsumer normal(float x, float y, float z) {
		return this.parent.normal(x, y, z);
	}
}
