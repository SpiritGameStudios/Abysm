package dev.spiritstudios.abysm.client.mixin.manowar;

import dev.spiritstudios.abysm.client.render.entity.ManOWarEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantValue")
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

	@Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V", shift = At.Shift.AFTER))
	private void renderTentacles(LivingEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		if (!(livingEntityRenderState instanceof ManOWarRenderState state && (LivingEntityRenderer) (Object) this instanceof ManOWarEntityRenderer renderer)) {
			return;
		}
		renderer.renderSpecial(state, matrixStack, vertexConsumerProvider, light);
	}
}
