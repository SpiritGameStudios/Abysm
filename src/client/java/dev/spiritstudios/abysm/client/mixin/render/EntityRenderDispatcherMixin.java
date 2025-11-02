package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.client.duck.EntityRenderStateDuck;
import dev.spiritstudios.abysm.client.render.VertexConsumerProviderOverlay;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	@WrapOperation(method = "render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
	private <S extends EntityRenderState> void adjustEntityRendering(EntityRenderer<?, S> instance, S state, PoseStack matrices, MultiBufferSource vertexConsumers, int light, Operation<Void> original) {
		if (state instanceof EntityRenderStateDuck duck) {
			if (duck.abysm$isBlue()) {
				// some people may claim this is questionable and janky. these people may be correct. it's a joke feature though it's fine right

				// wrap the VertexConsumerProvider in an overlay to turn things blue
				vertexConsumers = new VertexConsumerProviderOverlay(vertexConsumers, 31, 63, 255);
			}
		}

		// for some reason intellij think this is wrong, but it works so it must be fine i guess
		original.call(instance, state, matrices, vertexConsumers, light);
	}
}
