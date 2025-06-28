package dev.spiritstudios.abysm.client.mixin.harpoon;

import dev.spiritstudios.abysm.client.duck.HarpoonItemRenderState;
import dev.spiritstudios.abysm.client.render.entity.harpoon.HarpoonEntityRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {

	@Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE))
	private void renderHarpoon(ArmedEntityRenderState entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (!((HarpoonItemRenderState) itemState).abysm$shouldRenderHarpoon()) {
			return;
		}
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld clientWorld = client.world;
		if (clientWorld == null) {
			return;
		}
		HarpoonEntityRenderer.renderInStack(itemState, client, clientWorld, matrices, vertexConsumers, light);
	}
}
