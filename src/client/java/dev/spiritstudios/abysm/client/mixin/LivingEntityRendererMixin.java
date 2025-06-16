package dev.spiritstudios.abysm.client.mixin;

import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V", shift = At.Shift.AFTER))
	private void renderTentacles(LivingEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		if (!(livingEntityRenderState instanceof ManOWarRenderState state)) {
			return;
		}
		matrixStack.push();
		matrixStack.scale(1 / state.baseScale, 1 / state.baseScale, 1 / state.baseScale);
		int argb = ColorHelper.fullAlpha(new Color(5, 41, 66).getRGB());
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.LINES);
		Vec3d line = new Vec3d(0, 1.5, MathHelper.clamp(state.velocity.horizontalLengthSquared() * 500, 1.0E-7, 1.2));
		ManOWarRenderState.STARTING_OFFSETS.forEach(vector3f -> {
			matrixStack.push();
			matrixStack.translate(0, 3, 0);
			Vector3f offset = vector3f.mul(1, 1, state.baseScale, new Vector3f());
			VertexRendering.drawVector(matrixStack, vertexConsumer, offset, line, argb);
			matrixStack.pop();
		});
		matrixStack.pop();
	}
}
