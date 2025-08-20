package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.spiritstudios.abysm.client.render.VertexConsumerProviderOverlay;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
	private void turnArmsAndItemsBlueWhenBlue(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) LocalRef<VertexConsumerProvider> providerRef) {
		if (BlueEffect.hasBlueEffect(player)) {
			providerRef.set(new VertexConsumerProviderOverlay(vertexConsumers, 31, 63, 255));
		}
	}
}
