package dev.spiritstudios.abysm.client.render.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.GeoUtil;
import dev.spiritstudios.abysm.entity.depths.MysteriousBlobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class MysteriousBlobEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends AbstractFishEntityRenderer<MysteriousBlobEntity, R> {

	public static final DataTicket<Boolean> HAPPY_BLOB = DataTicket.create("happy_blob", Boolean.class);

	public MysteriousBlobEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new EntityModel());
	}

	@Override
	public ResourceLocation getTextureLocation(R renderState) {
		ResourceLocation original = super.getTextureLocation(renderState);
		if (!GeoUtil.getOrDefaultGeoData(renderState, HAPPY_BLOB, true)) {
			return original.withPath(string -> {
				int extension = string.indexOf('.');
				return string.substring(0, extension) + "_unhappy" + string.substring(extension);
			});
		}
		return original;
	}

	@Override
	protected void applyRotations(R renderState, PoseStack matrixStack, float nativeScale) {
		super.applyRotations(renderState, matrixStack, nativeScale);
	}

	@SuppressWarnings("OverrideOnly")
	@Override
	public void extractRenderState(MysteriousBlobEntity blob, R entityRenderState, float partialTick) {
		super.extractRenderState(blob, entityRenderState, partialTick);
		this.scaleWidth = blob.lerpScaleXZ(partialTick);
		this.scaleHeight = blob.lerpScaleY(partialTick);
	}

	@Override
	protected boolean shouldApplyFishLandTransforms() {
		return false;
	}

	public static class EntityModel extends DefaultedEntityGeoModel<MysteriousBlobEntity> {
		public EntityModel() {
			super(Abysm.id("mysterious_blob"), true);
		}

		@Override
		public void addAdditionalStateData(MysteriousBlobEntity blob, GeoRenderState renderState) {
			super.addAdditionalStateData(blob, renderState);
			renderState.addGeckolibData(HAPPY_BLOB, blob.isHappy());
		}
	}
}
