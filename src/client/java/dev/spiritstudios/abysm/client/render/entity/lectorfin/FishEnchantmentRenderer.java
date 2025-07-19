package dev.spiritstudios.abysm.client.render.entity.lectorfin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public abstract class FishEnchantmentRenderer {

	protected ExtraModel model;

	public FishEnchantmentRenderer(ExtraModel model) {
		this.model = model;
	}

	@SuppressWarnings("unused")
	public <R extends LivingEntityRenderState & GeoRenderState> void render(
		R state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, int color, RecursiveRenderer<R> recursiveRenderer) {

		RenderLayer renderLayer = this.model.getRenderType(state, this.model.getTextureResource(state));

		@Nullable VertexConsumer vertexConsumer;
		if (renderLayer == null) {
			vertexConsumer = null;
		} else {
			vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		}
		BakedGeoModel bakedGeoModel = this.model.getBakedModel(this.model.getModelResource(state));
		//noinspection UnstableApiUsage
		this.model.handleAnimations(new AnimationState<>(state));

		if (renderLayer == null || vertexConsumer == null) {
			return;
		}

		for (GeoBone group : bakedGeoModel.topLevelBones()) {
			recursiveRenderer.render(state, matrices, group, renderLayer, vertexConsumers, vertexConsumer, true, light, overlay, color);
		}
	}

	@FunctionalInterface
	public interface RecursiveRenderer<R extends LivingEntityRenderState & GeoRenderState> {
		void render(R state, MatrixStack matrices, GeoBone bone, @Nullable RenderLayer renderLayer, VertexConsumerProvider vertexConsumers, @Nullable VertexConsumer vertexConsumer, boolean isReRender, int light, int overlay, int color);
	}

	public static class ExtraModel extends LectorfinEntityRenderer.LectorfinEntityModel {

		public ExtraModel(Identifier id) {
			super(id);
		}

		@Override
		protected String subtype() {
			return "fish_enchantment";
		}
	}
}
