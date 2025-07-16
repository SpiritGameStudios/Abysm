package dev.spiritstudios.abysm.client.render.entity.lectorfin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public abstract class FishEnchantmentRenderer {

	public static final DataTicket<Boolean> RENDERING_ENCHANTMENT = DataTicket.create("rendering_enchantment", Boolean.class);

	protected ExtraModel model;

	public FishEnchantmentRenderer(ExtraModel model) {
		this.model = model;
	}

	@SuppressWarnings("unused")
	public <R extends LivingEntityRenderState & GeoRenderState> void render(
		R state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, int color, ReRenderer<R> reRenderer) {

		RenderLayer renderLayer = this.model.getRenderType(state, this.model.getTextureResource(state));

		@Nullable VertexConsumer vertexConsumer;
		if (renderLayer == null) {
			vertexConsumer = null;
		} else {
			vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		}
		BakedGeoModel bakedGeoModel = this.model.getBakedModel(this.model.getModelResource(state));
		state.addGeckolibData(RENDERING_ENCHANTMENT, true);
		reRenderer.render(state, matrices, bakedGeoModel, vertexConsumers, renderLayer, vertexConsumer, light, overlay, color);
		state.addGeckolibData(RENDERING_ENCHANTMENT, false);
	}

	@FunctionalInterface
	public interface ReRenderer<R extends LivingEntityRenderState & GeoRenderState> {
		void render(R state, MatrixStack matrices, BakedGeoModel model, VertexConsumerProvider vertexConsumers, @Nullable RenderLayer renderLayer, @Nullable VertexConsumer vertexConsumer, int light, int overlay, int color);
	}

	public static class ExtraModel extends LectorfinEntityRenderer.LectorfinEntityModel {

		public ExtraModel(Identifier id) {
			super(id);
		}

		@Override
		protected String subtype() {
			return "fishenchantment";
		}
	}
}
