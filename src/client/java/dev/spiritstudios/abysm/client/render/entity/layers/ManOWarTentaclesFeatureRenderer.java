package dev.spiritstudios.abysm.client.render.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.client.render.AbysmRenderTypes;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.world.entity.floralreef.ManOWarEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class ManOWarTentaclesFeatureRenderer extends RenderLayer<ManOWarRenderState, GarbageBagModel> {
	public static final int TENTACLE_ARGB = ARGB.opaque(new Color(5, 41, 66).getRGB());

	public ManOWarTentaclesFeatureRenderer(RenderLayerParent<ManOWarRenderState, GarbageBagModel> context) {
		super(context);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, ManOWarRenderState state, float yRot, float xRot) {
		if (state.tentacleData == null || state.tentacleData.isEmpty()) {
			return;
		}
		poseStack.pushPose();
		poseStack.translate(0, 1.5, 0);

		nodeCollector.submitCustomGeometry(
			poseStack,
			AbysmRenderTypes.MAN_O_WAR_TENTACLES,
			(pose, buffer) -> {
				Vec3 line = new Vec3(
					0,
					ManOWarEntity.BASE_TENTACLE_LENGTH,
					Mth.clamp(state.velocity.horizontalDistanceSqr() * 500, 1.0E-7, 1.2) * 0.5
				);


				long time = Util.getMillis();
				for (ManOWarEntity.TentacleData tentacle : state.tentacleData) {
					var offset = tentacle.relativePosition().toVector3f();

					// TODO: This is not how you RenderState
					var vec = line.add(
						0, 0,
						(Mth.sin((time + tentacle.swayOffset()) * ManOWarEntity.INVERSE_MAX_SWAY_OFFSET) + 1) * 0.2
					);

					buffer
						.addVertex(pose, offset)
						.setColor(TENTACLE_ARGB)
						.setNormal(pose, (float) vec.x, (float) vec.y, (float) vec.z)
						.setLight(state.lightCoords);
					buffer
						.addVertex(pose, (float) (offset.x() + vec.x), (float) (offset.y() + vec.y), (float) (offset.z() + vec.z))
						.setColor(TENTACLE_ARGB)
						.setNormal(pose, (float) vec.x, (float) vec.y, (float) vec.z)
						.setLight(state.lightCoords);
				}
				poseStack.popPose();
			}
		);

	}
}
