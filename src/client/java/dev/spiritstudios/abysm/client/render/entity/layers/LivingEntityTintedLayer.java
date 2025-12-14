package dev.spiritstudios.abysm.client.render.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import java.util.function.Function;

public class LivingEntityTintedLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
	private final Function<S, Identifier> textureProvider;
	private final ColorFunction<S> colorFunction;
	private final M model;
	private final Function<Identifier, RenderType> bufferProvider;
	private final boolean alwaysVisible;

	public LivingEntityTintedLayer(
		RenderLayerParent<S, M> renderer,
		Function<S, Identifier> textureProvider,
		ColorFunction<S> colorFunction,
		M model,
		Function<Identifier, RenderType> bufferProvider,
		boolean alwaysVisible
	) {
		super(renderer);
		this.textureProvider = textureProvider;
		this.colorFunction = colorFunction;
		this.model = model;
		this.bufferProvider = bufferProvider;
		this.alwaysVisible = alwaysVisible;
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, S renderState, float f, float g) {
		if (renderState.isInvisible && !this.alwaysVisible) return;

		int color = this.colorFunction.apply(renderState, renderState.ageInTicks);
		float alpha = ARGB.alpha(color);

		if (alpha <= 1.0E-5F) return;

		RenderType renderType = this.bufferProvider.apply(this.textureProvider.apply(renderState));
		submitNodeCollector.order(1)
			.submitModel(
				this.model,
				renderState,
				poseStack,
				renderType,
				i,
				LivingEntityRenderer.getOverlayCoords(renderState, 0.0F),
				color,
				null,
				renderState.outlineColor,
				null
			);
	}

	@Environment(EnvType.CLIENT)
	public interface ColorFunction<S extends LivingEntityRenderState> {
		int apply(S renderState, float partialTick);
	}
}
