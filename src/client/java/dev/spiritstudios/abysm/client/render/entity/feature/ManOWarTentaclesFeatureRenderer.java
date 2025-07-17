package dev.spiritstudios.abysm.client.render.entity.feature;

import dev.spiritstudios.abysm.client.render.AbysmRenderLayers;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.awt.*;

public class ManOWarTentaclesFeatureRenderer extends FeatureRenderer<ManOWarRenderState, GarbageBagModel> {
	public static final int TENTACLE_ARGB = ColorHelper.fullAlpha(new Color(5, 41, 66).getRGB());

	public ManOWarTentaclesFeatureRenderer(FeatureRendererContext<ManOWarRenderState, GarbageBagModel> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ManOWarRenderState state, float limbAngle, float limbDistance) {
		if (state.tentacleData == null || state.tentacleData.isEmpty()) {
			return;
		}

		VertexConsumer lines = vertexConsumers.getBuffer(AbysmRenderLayers.MAN_O_WAR_TENTACLES);
		Vec3d line = new Vec3d(
			0,
			ManOWarEntity.BASE_TENTACLE_LENGTH,
			MathHelper.clamp(state.velocity.horizontalLengthSquared() * 500, 1.0E-7, 1.2) * 0.5
		);

		long time = Util.getMeasuringTimeMs();
		state.tentacleData.forEach(tentacle -> {
			matrices.push();
			matrices.translate(0, 1.5, 0);
			drawVector(
				matrices,
				lines,
				tentacle.relativePosition().toVector3f(),
				line.add(
					0, 0,
					(MathHelper.sin((time + tentacle.swayOffset()) * ManOWarEntity.INVERSE_MAX_SWAY_OFFSET) + 1) * 0.2
				),
				TENTACLE_ARGB, light
			);
			matrices.pop();
		});
	}

	public static void drawVector(MatrixStack matrices, VertexConsumer vertexConsumer, Vector3f offset, Vec3d vec, int argb, int light) {
		MatrixStack.Entry entry = matrices.peek();
		vertexConsumer
			.vertex(entry, offset)
			.color(argb)
			.normal(entry, (float) vec.x, (float) vec.y, (float) vec.z)
			.light(light);
		vertexConsumer
			.vertex(entry, (float) (offset.x() + vec.x), (float) (offset.y() + vec.y), (float) (offset.z() + vec.z))
			.color(argb)
			.normal(entry, (float) vec.x, (float) vec.y, (float) vec.z)
			.light(light);
	}
}
