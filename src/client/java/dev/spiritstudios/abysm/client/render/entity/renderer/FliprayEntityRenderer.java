package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.GeoUtil;
import dev.spiritstudios.abysm.entity.ReticulatedFliprayEntity;
import dev.spiritstudios.abysm.entity.depths.MysteriousBlobEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class FliprayEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends AbstractFishEntityRenderer<ReticulatedFliprayEntity, R> {

	public FliprayEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new EntityModel());
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
			@Override
			protected boolean shouldRespectWorldLighting(R renderState) {
				return true;
			}
		});
	}

	@Override
	protected void applyRotations(R renderState, MatrixStack matrixStack, float nativeScale) {
		super.applyRotations(renderState, matrixStack, nativeScale);
	}

	@Override
	protected boolean shouldApplyFishLandTransforms() {
		return false;
	}

	public static class EntityModel extends DefaultedEntityGeoModel<ReticulatedFliprayEntity> {
		public EntityModel() {
			super(Abysm.id("reticulated_flipray"), false);
		}
	}
}
