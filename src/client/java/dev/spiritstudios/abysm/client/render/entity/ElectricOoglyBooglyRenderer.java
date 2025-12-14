package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.layers.ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer;
import dev.spiritstudios.abysm.client.render.entity.state.ElectricOoglyBooglyRenderState;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.world.entity.ElectricOoglyBooglyEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class ElectricOoglyBooglyRenderer extends VariantMobRenderer<ElectricOoglyBooglyEntity, ElectricOoglyBooglyVariant, ElectricOoglyBooglyRenderState, ElectricOoglyBooglyRenderer.Model> {
	public ElectricOoglyBooglyRenderer(EntityRendererProvider.Context context) {
		super(
			context,
			new Model(
				context.bakeLayer(new ModelLayerLocation(Abysm.id("electric_oogly_boogly"), "main"))
			),
			1F
		);

		this.layers.add(new ElectricOoglyBooglyBlowingUpWithMindFeatureRenderer(
			this,
			new Model(
				context.bakeLayer(new ModelLayerLocation(Abysm.id("electric_oogly_boogly"), "main"))
			) // TODO: Special layer
		));
	}

	@Override
	public ElectricOoglyBooglyRenderState createRenderState() {
		return new ElectricOoglyBooglyRenderState();
	}

	@Override
	public void extractRenderState(ElectricOoglyBooglyEntity entity, ElectricOoglyBooglyRenderState renderState, float partialTick) {
		super.extractRenderState(entity, renderState, partialTick);

		renderState.query.set(entity, partialTick);
		renderState.bonk.copyFrom(entity.bonk);

		renderState.isBlowingUpWithMind = entity.isBlowingUpWithMind();
		renderState.blowingUpWithMindTicks = entity.ticksSinceBlowingUp + partialTick;

		renderState.velocity = entity.getDeltaMovement();
	}

	@Override
	public Identifier getTextureLocation(ElectricOoglyBooglyRenderState renderState) {
		return renderState.variant.texture;
	}

	public static class Model extends EntityModel<ElectricOoglyBooglyRenderState> {
		public static final String[] TENTACLE_BONE_NAMES = new String[]{"NW", "N", "NE", "E", "SE", "S", "SW", "W"};
		public boolean animateTentacles = true;

		protected Model(ModelPart root) {
			super(root);
		}

		@Override
		public void setupAnim(ElectricOoglyBooglyRenderState renderState) {
			super.setupAnim(renderState);

			// TODO: Anims
			if (!animateTentacles) return;

			float velX = (float) renderState.velocity.x();
			float velZ = (float) renderState.velocity.z();

			float rotX = Mth.clamp(Mth.sin(renderState.bodyRot) * (velX * 15f), -0.75f, 0.75f);
			float rotZ = Mth.clamp(Mth.cos(renderState.bodyRot) * (velZ * 3f), -0.5f, 0.5f);

			for (String tentacleBone : TENTACLE_BONE_NAMES) {
				if (!root.hasChild(tentacleBone)) continue;

				ModelPart part = root.getChild(tentacleBone);

				part.offsetRotation(new Vector3f(rotX, 0, rotZ));
			}
		}
	}
}
