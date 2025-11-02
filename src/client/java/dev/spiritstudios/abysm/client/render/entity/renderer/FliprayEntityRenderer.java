package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.ReticulatedFliprayEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class FliprayEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<ReticulatedFliprayEntity, R> {

	public FliprayEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new EntityModel());
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
			@Override
			protected boolean shouldRespectWorldLighting(R renderState) {
				return true;
			}
		});
	}

	public static class EntityModel extends DefaultedEntityGeoModel<ReticulatedFliprayEntity> {
		public EntityModel() {
			super(Abysm.id("reticulated_flipray"), false);
		}

		@Override
		public void setCustomAnimations(AnimationState<ReticulatedFliprayEntity> animationState) {
			GeoBone body = getAnimationProcessor().getBone("body");


			if (body != null) {
				float pitch = animationState.getData(DataTickets.ENTITY_PITCH);
				float yaw = animationState.getData(DataTickets.ENTITY_YAW);

				body.setRotX(-pitch * Mth.DEG_TO_RAD);
				body.setRotY(-yaw * Mth.DEG_TO_RAD);
			}
		}
	}
}
