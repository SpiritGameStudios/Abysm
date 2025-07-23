package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.floralreef.PaddlefishEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class PaddlefishEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<PaddlefishEntity, R> {

	public PaddlefishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new PaddlefishEntityModel());
	}

	public static class PaddlefishEntityModel extends DefaultedEntityGeoModel<PaddlefishEntity> {
		public PaddlefishEntityModel() {
			super(Abysm.id("paddlefish"), false);
		}
	}
}
