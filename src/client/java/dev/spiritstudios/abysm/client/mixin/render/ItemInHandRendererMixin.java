package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.abysm.client.render.VertexConsumerProviderOverlay;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

	@Inject(method = "renderArmWithItem", at = @At("HEAD"))
	private void turnArmsAndItemsBlueWhenBlue(AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) LocalRef<MultiBufferSource> providerRef) {
		if (BlueEffect.hasBlueEffect(player)) {
			providerRef.set(new VertexConsumerProviderOverlay(vertexConsumers, 31, 63, 255));
		}
	}
}
