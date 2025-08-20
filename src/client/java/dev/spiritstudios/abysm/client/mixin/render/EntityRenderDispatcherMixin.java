package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.client.duck.EntityRenderStateDuck;
import dev.spiritstudios.abysm.client.render.VertexConsumerProviderOverlay;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/EntityRenderer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
	private <S extends EntityRenderState> void adjustEntityRendering(EntityRenderer<?, S> instance, S state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Operation<Void> original) {
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
