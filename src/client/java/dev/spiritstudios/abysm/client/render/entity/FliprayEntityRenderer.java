package dev.spiritstudios.abysm.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.entity.ReticulatedFliprayEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class FliprayEntityRenderer extends MobRenderer<ReticulatedFliprayEntity, LivingEntityRenderState, FliprayEntityRenderer.Model> {
	public static final Identifier TEXTURE = Abysm.id("textures/entity/reticulated_flipray.png");
	public static final Identifier BIOLUMINESCENT_LAYER_TEXTURE = Abysm.id("textures/entity/reticulated_flipray_bioluminescent_layer.png");

	public FliprayEntityRenderer(EntityRendererProvider.Context context) {
		super(
			context,
			new Model(context.bakeLayer(
				new ModelLayerLocation(Abysm.id("reticulated_flipray"), "main")
			)),
			1f
		);
		this.layers.add(new LivingEntityEmissiveLayer<>(
			this,
			state -> BIOLUMINESCENT_LAYER_TEXTURE,
			(state, f) -> 1F,
			// TODO: Maybe make another layer to only render the bits we need
			new Model(context.bakeLayer(new ModelLayerLocation(Abysm.id("reticulated_flipray"), "main"))),
			RenderTypes::entityTranslucentEmissive,
			true
		));
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState livingEntityRenderState) {
		return TEXTURE;
	}

	@Override
	protected void setupRotations(LivingEntityRenderState renderState, PoseStack poseStack, float bodyRot, float scale) {
		super.setupRotations(renderState, poseStack, bodyRot, scale);
		poseStack.mulPose(Axis.XP.rotationDegrees(-renderState.xRot));
	}

	public static class Model extends EntityModel<LivingEntityRenderState> {
		public Model(
			ModelPart part
		) {
			super(part);
		}
	}
}
