package dev.spiritstudios.abysm.client.mixin.manowar;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.client.render.entity.ManOWarEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.state.EntityHitboxAndView;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Inject(method = "renderHitboxes(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/entity/state/EntityHitboxAndView;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderHitboxes(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/entity/state/EntityHitboxAndView;Lnet/minecraft/client/render/VertexConsumer;F)V", ordinal = 0, shift = At.Shift.AFTER))
	private void renderTentacleBox(MatrixStack matrices, EntityRenderState entityRenderState, EntityHitboxAndView hitbox, VertexConsumerProvider vertexConsumers, CallbackInfo ci, @Local VertexConsumer lines) {
		if (!(entityRenderState instanceof ManOWarRenderState state)) {
			return;
		}
		ManOWarEntityRenderer.renderTentacleBox(matrices, lines, state);
	}
}
