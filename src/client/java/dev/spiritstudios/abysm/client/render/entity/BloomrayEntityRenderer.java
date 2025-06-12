package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.BloomrayEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BloomrayEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<BloomrayEntity, R> {
	public BloomrayEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BloomrayEntityModel());
	}

	public static class BloomrayEntityModel extends DefaultedEntityGeoModel<BloomrayEntity> {
		public BloomrayEntityModel() {
			super(Abysm.id("bloomray"), true);

			// TODO - (Data-driven?) entity variants for different bloomshrooms
			this.withAltTexture(Abysm.id("rosy_bloomray"));
		}
	}
}
