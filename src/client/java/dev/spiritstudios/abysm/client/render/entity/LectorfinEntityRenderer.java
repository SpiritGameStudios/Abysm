package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.model.AbstractFishEntityModel;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class LectorfinEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends AbstractFishEntityRenderer<LectorfinEntity, R> {

	public LectorfinEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new LectorfinEntityModel());

		// render with enchantment data somehow
	}

	public static class LectorfinEntityModel extends AbstractFishEntityModel<LectorfinEntity> {
		public LectorfinEntityModel() {
			super(Abysm.id("lectorfin"), false);
		}

		@Override
		public void addAdditionalStateData(LectorfinEntity animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			// attach enchantment data when I'm ready
		}
	}
}
