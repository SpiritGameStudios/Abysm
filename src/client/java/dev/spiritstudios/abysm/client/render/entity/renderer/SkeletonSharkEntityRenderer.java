package dev.spiritstudios.abysm.client.render.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.GeoUtil;
import dev.spiritstudios.abysm.entity.leviathan.pseudo.SkeletonSharkEntity;
import dev.spiritstudios.abysm.entity.leviathan.pseudo.SkeletonSharkPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.spiritstudios.abysm.entity.leviathan.pseudo.SkeletonSharkEntity.HUNTING;

public class SkeletonSharkEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends AbstractFishEntityRenderer<SkeletonSharkEntity, R> {

	public static final DataTicket<Boolean> SANS = DataTicket.create("sans", Boolean.class);
	public static final DataTicket<List> PARTS = DataTicket.create("parts", List.class);
	public static final DataTicket<Boolean> HACKY_ROTATE_ANYWAY = DataTicket.create("hacky_rotate_anyway", Boolean.class);

	public SkeletonSharkEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new EntityModel());
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
			@Override
			protected boolean shouldRespectWorldLighting(R renderState) {
				return true;
			}

			@Override
			public void render(R renderState, PoseStack poseStack, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
				if (!GeoUtil.getOrDefaultGeoData(renderState, SANS, false)) {
					return;
				}
				renderState.addGeckolibData(HACKY_ROTATE_ANYWAY, true);
				super.render(renderState, poseStack, bakedModel, renderType, bufferSource, buffer, packedLight, packedOverlay, renderColor);
				renderState.addGeckolibData(HACKY_ROTATE_ANYWAY, false);
			}
		});
	}

	@Override
	protected float getDeathMaxRotation(GeoRenderState renderState) {
		return super.getDeathMaxRotation(renderState);
	}

	@SuppressWarnings({"DataFlowIssue", "unchecked"})
	@Override
	public void actuallyRender(R state, PoseStack matrices, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource vertexConsumers, @Nullable VertexConsumer buffer, boolean isReRender, int packedLight, int packedOverlay, int renderColor) {
		matrices.pushPose();
		if (GeoUtil.getOrDefaultGeoData(state, HACKY_ROTATE_ANYWAY, false)) {
			applyRotations(state, matrices, state.scale);
		}
		super.actuallyRender(state, matrices, model, renderType, vertexConsumers, buffer, isReRender, packedLight, packedOverlay, renderColor);
		matrices.popPose();

		float originalPitch = state.getGeckolibData(DataTickets.ENTITY_PITCH);
		float originalYaw = state.getGeckolibData(DataTickets.ENTITY_YAW);
		float bodyYaw = state.getGeckolibData(DataTickets.ENTITY_BODY_YAW);

		float tickProgress = state.getGeckolibData(DataTickets.PARTIAL_TICK);
		Vec3 skelepos = state.getGeckolibData(DataTickets.POSITION);

		List<Object> parts = state.getGeckolibData(PARTS);
		Vec3 prevPos = Vec3.ZERO;
		final float inverseScale = 1 / state.scale;
		for (Object o : parts) {
			SkeletonSharkPart skeletonSharkPart = (SkeletonSharkPart) o;
			PitchYawPair pair;
			if (skeletonSharkPart.name.contains("fin")) {
				pair = lookAt(prevPos, Vec3.ZERO);
			} else {
				Vec3 relativePos = skeletonSharkPart.getRelativePos();
				pair = lookAt(relativePos, prevPos);
				prevPos = relativePos;
			}

			state.addGeckolibData(DataTickets.ENTITY_PITCH, pair.pitch);
			state.addGeckolibData(DataTickets.ENTITY_YAW, pair.yaw);
			state.addGeckolibData(DataTickets.ENTITY_BODY_YAW, pair.yaw);

			BodyPartRenderer renderer = BodyPartRenderer.RENDERERS.get(skeletonSharkPart.name);
			if (renderer != null) {
				Vec3 pos = skeletonSharkPart.getPosition(tickProgress).subtract(skelepos).scale(inverseScale);
				matrices.pushPose();
				matrices.translate(pos.x, pos.y, pos.z);

				matrices.pushPose();
				applyRotations(state, matrices, state.scale);
				matrices.mulPose(Axis.XP.rotationDegrees(-pair.pitch));
				renderer.render(state, renderType, matrices, vertexConsumers, packedLight, packedOverlay, renderColor, RecursiveRenderer.create(this));
				matrices.popPose();

				matrices.popPose();
			}
		}

		state.addGeckolibData(DataTickets.ENTITY_PITCH, originalPitch);
		state.addGeckolibData(DataTickets.ENTITY_YAW, originalYaw);
		state.addGeckolibData(DataTickets.ENTITY_BODY_YAW, bodyYaw);
	}

	@Override
	protected boolean shouldApplyFishLandTransforms() {
		return false;
	}

	public static class EntityModel extends DefaultedEntityGeoModel<SkeletonSharkEntity> {
		public EntityModel() {
			super(Abysm.id("skeleton_shark"), true);
		}

		@Override
		public void addAdditionalStateData(SkeletonSharkEntity skeleshark, GeoRenderState renderState) {
			super.addAdditionalStateData(skeleshark, renderState);
			renderState.addGeckolibData(SANS, skeleshark.getAttachedOrElse(HUNTING, false));
			renderState.addGeckolibData(PARTS, skeleshark.getSpecterEntityParts());
		}
	}

	public static class BodyPartRenderer {

		public static final Map<String, BodyPartRenderer> RENDERERS = new HashMap<>();

		@SuppressWarnings("unused")
		public static final BodyPartRenderer BODY_RENDERER = new BodyPartRenderer("body");
		@SuppressWarnings("unused")
		public static final BodyPartRenderer TAIL_RENDERER = new BodyPartRenderer("tail");
		@SuppressWarnings("unused")
		public static final BodyPartRenderer RFIN_RENDERER = new BodyPartRenderer("rfin");
		@SuppressWarnings("unused")
		public static final BodyPartRenderer LFIN_RENDERER = new BodyPartRenderer("lfin");

		protected final BodyPartModel model;

		public BodyPartRenderer(String subpath) {
			this.model = new BodyPartModel(subpath);
			RENDERERS.put(subpath, this);
		}

		@SuppressWarnings("unused")
		public <R extends LivingEntityRenderState & GeoRenderState> void render(
			R state, @Nullable RenderType renderLayer, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, int color, RecursiveRenderer<R> recursiveRenderer) {

			if (renderLayer == null) {
				renderLayer = this.model.getRenderType(state, this.model.getTextureResource(state));
			}

			@Nullable VertexConsumer vertexConsumer;
			if (renderLayer == null) {
				vertexConsumer = null;
			} else {
				vertexConsumer = vertexConsumers.getBuffer(renderLayer);
			}
			BakedGeoModel bakedGeoModel = this.model.getBakedModel(this.model.getModelResource(state));
			//noinspection UnstableApiUsage
			this.model.handleAnimations(new AnimationState<>(state));

			if (renderLayer == null || vertexConsumer == null) {
				return;
			}

			for (GeoBone group : bakedGeoModel.topLevelBones()) {
				recursiveRenderer.render(state, matrices, group, renderLayer, vertexConsumers, vertexConsumer, true, light, overlay, color);
			}
		}
	}

	public static class BodyPartModel extends DefaultedEntityGeoModel<SkeletonSharkPart> {
		protected final String subpath;

		public BodyPartModel(String subpath) {
			super(Abysm.id("skeleton_shark_" + subpath), false);
			this.subpath = subpath;
		}

		@Override
		public void addAdditionalStateData(SkeletonSharkPart part, GeoRenderState renderState) {
			super.addAdditionalStateData(part, renderState);
		}

		@Override
		public ResourceLocation getTextureResource(GeoRenderState renderState) {
			return super.getTextureResource(renderState).withPath(path -> {
				int index = path.indexOf(this.subpath);
				if (index < 0) {
					return path;
				}
				return new StringBuilder(path).delete(index - 1, index + this.subpath.length()).toString();
			});
		}
	}

	public static PitchYawPair lookAt(Vec3 vec3d, Vec3 target) {
		double d = target.x - vec3d.x;
		double e = target.y - vec3d.y;
		double f = target.z - vec3d.z;
		double g = Math.sqrt(d * d + f * f);
		float pitch = Mth.wrapDegrees((float)(-(Mth.atan2(e, g) * 180.0F / (float)Math.PI)));
		float yaw = Mth.wrapDegrees((float)(Mth.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);
		return new PitchYawPair(pitch, yaw);
	}

	public record PitchYawPair(float pitch, float yaw) {

	}
}
