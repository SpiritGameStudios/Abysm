package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.feature.ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.entity.floral_reef.ElectricOoglyBooglyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

	public ElectricOoglyBooglyRenderer(EntityRendererFactory.Context context) {
		super(context, new ElectricOoglyBooglyModel());

		this.addRenderLayer(new ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer<>(this));
	}

	@Override
	public Identifier getTextureLocation(R renderState) {
		ElectricOoglyBooglyVariant variant = renderState.getGeckolibData(VARIANT_TICKET);
		if (variant != null && variant.getTexture() != null) return variant.getTexture();
		return ElectricOoglyBooglyVariant.DEFAULT.getTexture();
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

			Vec3d velocity = animationState.getDataOrDefault(DataTickets.VELOCITY, Vec3d.ZERO);
			float velX = (float) velocity.getX();
			float velZ = (float) velocity.getZ();

			float yawRadians = yaw * MathHelper.RADIANS_PER_DEGREE;
			float rotX = MathHelper.clamp(MathHelper.sin(yawRadians) * (velX * 15f), -0.75f, 0.75f);
			float rotZ = MathHelper.clamp(MathHelper.cos(yawRadians) * (velZ * 3f), -0.5f, 0.5f);

			for (String tentacleBone : TENTACLE_BONE_NAMES) {
				GeoBone bone = getAnimationProcessor().getBone(tentacleBone);
				if (bone == null) continue;

				bone.setRotX(bone.getRotX() + rotX);
				bone.setRotZ(bone.getRotZ() + rotZ);
			}
		}


	}
}
