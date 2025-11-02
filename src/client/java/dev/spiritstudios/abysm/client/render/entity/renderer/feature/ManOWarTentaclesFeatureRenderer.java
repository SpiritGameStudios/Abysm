package dev.spiritstudios.abysm.client.render.entity.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.spiritstudios.abysm.client.render.AbysmRenderLayers;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import org.joml.Vector3f;

import java.awt.*;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ManOWarTentaclesFeatureRenderer extends RenderLayer<ManOWarRenderState, GarbageBagModel> {
	public static final int TENTACLE_ARGB = ARGB.opaque(new Color(5, 41, 66).getRGB());

	public ManOWarTentaclesFeatureRenderer(RenderLayerParent<ManOWarRenderState, GarbageBagModel> context) {
		super(context);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ManOWarRenderState state, float limbAngle, float limbDistance) {
		if (state.tentacleData == null || state.tentacleData.isEmpty()) {
			return;
		}

		VertexConsumer lines = vertexConsumers.getBuffer(AbysmRenderLayers.MAN_O_WAR_TENTACLES);
		Vec3 line = new Vec3(
			0,
			ManOWarEntity.BASE_TENTACLE_LENGTH,
			Mth.clamp(state.velocity.horizontalDistanceSqr() * 500, 1.0E-7, 1.2) * 0.5
		);

		long time = Util.getMillis();
		state.tentacleData.forEach(tentacle -> {
			matrices.pushPose();
			matrices.translate(0, 1.5, 0);
			drawVector(
				matrices,
				lines,
				tentacle.relativePosition().toVector3f(),
				line.add(
					0, 0,
					(Mth.sin((time + tentacle.swayOffset()) * ManOWarEntity.INVERSE_MAX_SWAY_OFFSET) + 1) * 0.2
				),
				TENTACLE_ARGB, light
			);
			matrices.popPose();
		});
	}

	public static void drawVector(PoseStack matrices, VertexConsumer vertexConsumer, Vector3f offset, Vec3 vec, int argb, int light) {
		PoseStack.Pose entry = matrices.last();
		vertexConsumer
			.addVertex(entry, offset)
			.setColor(argb)
			.setNormal(entry, (float) vec.x, (float) vec.y, (float) vec.z)
			.setLight(light);
		vertexConsumer
			.addVertex(entry, (float) (offset.x() + vec.x), (float) (offset.y() + vec.y), (float) (offset.z() + vec.z))
			.setColor(argb)
			.setNormal(entry, (float) vec.x, (float) vec.y, (float) vec.z)
			.setLight(light);
	}
}
