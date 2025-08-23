package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.leviathan.test.Lehydrathan;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static dev.spiritstudios.abysm.client.render.entity.renderer.SkeletonSharkRenderer.PARTS;

public class LehydrathanEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Lehydrathan, R> {

	public LehydrathanEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new Model());
	}

	public static class Model extends DefaultedEntityGeoModel<Lehydrathan> {
		public Model() {
			super(Abysm.id("lehydrathan"), false);
		}

		@Override
		public void addAdditionalStateData(Lehydrathan animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			renderState.addGeckolibData(PARTS, animatable.getSpecterEntityParts());
		}
	}
}
