package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.renderer.feature.ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.entity.ElectricOoglyBooglyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class ElectricOoglyBooglyRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<ElectricOoglyBooglyEntity, R> {
	public static final DataTicket<ElectricOoglyBooglyVariant> VARIANT_TICKET = DataTicket.create("oogly_boogly_variant_ticket", ElectricOoglyBooglyVariant.class);
	public static final DataTicket<Boolean> IS_BLOWING_UP_WITH_MIND = DataTicket.create("oogly_boogly_blowing_up_with_mind_ticket", Boolean.class);
	public static final DataTicket<Integer> BLOWING_UP_WITH_MIND_TICKS = DataTicket.create("oogly_boogly_blowing_up_with_mind_ticks_ticket", Integer.class);

	public ElectricOoglyBooglyRenderer(EntityRendererProvider.Context context) {
		super(context, new ElectricOoglyBooglyModel());

		this.addRenderLayer(new ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(R renderState) {
		ElectricOoglyBooglyVariant variant = renderState.getGeckolibData(VARIANT_TICKET);
		if (variant != null && variant.getTexture() != null) return variant.getTexture();

		return MissingTextureAtlasSprite.getLocation();
	}

	public static class ElectricOoglyBooglyModel extends DefaultedEntityGeoModel<ElectricOoglyBooglyEntity> {
		public static final String[] TENTACLE_BONE_NAMES = new String[]{"NW", "N", "NE", "E", "SE", "S", "SW", "W"};
		public boolean animateTentacles = true;

		public ElectricOoglyBooglyModel() {
			super(Abysm.id("electric_oogly_boogly"));
		}

		@Override
		public void addAdditionalStateData(ElectricOoglyBooglyEntity animatable, GeoRenderState renderState) {
			renderState.addGeckolibData(IS_BLOWING_UP_WITH_MIND, animatable.isBlowingUpWithMind());
			renderState.addGeckolibData(BLOWING_UP_WITH_MIND_TICKS, animatable.ticksSinceBlowingUp);
			renderState.addGeckolibData(VARIANT_TICKET, animatable.getVariant());
		}

		@Override
		public void setCustomAnimations(AnimationState<ElectricOoglyBooglyEntity> animationState) {
			// Head turning animation
			super.setCustomAnimations(animationState);
			if (!animateTentacles) return;

			float yaw = animationState.getDataOrDefault(DataTickets.ENTITY_BODY_YAW, 0f);

			Vec3 velocity = animationState.getDataOrDefault(DataTickets.VELOCITY, Vec3.ZERO);
			float velX = (float) velocity.x();
			float velZ = (float) velocity.z();

			float yawRadians = yaw * Mth.DEG_TO_RAD;
			float rotX = Mth.clamp(Mth.sin(yawRadians) * (velX * 15f), -0.75f, 0.75f);
			float rotZ = Mth.clamp(Mth.cos(yawRadians) * (velZ * 3f), -0.5f, 0.5f);

			for (String tentacleBone : TENTACLE_BONE_NAMES) {
				GeoBone bone = getAnimationProcessor().getBone(tentacleBone);
				if (bone == null) continue;

				bone.setRotX(bone.getRotX() + rotX);
				bone.setRotZ(bone.getRotZ() + rotZ);
			}
		}
	}
}
