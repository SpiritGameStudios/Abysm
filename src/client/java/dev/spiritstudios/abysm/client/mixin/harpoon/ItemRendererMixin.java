package dev.spiritstudios.abysm.client.mixin.harpoon;

import dev.spiritstudios.abysm.client.render.entity.harpoon.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.component.BlessedComponent;
import dev.spiritstudios.abysm.item.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	@Final
	private ItemRenderState itemRenderState;

	@Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At("RETURN"))
	private void renderHarpoonInLauncher(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
		if (!stack.isOf(AbysmItems.NOOPRAH)) {
			return;
		}
		if (!stack.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY).loaded()) {
			return;
		}
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld clientWorld = client.world;
		if (clientWorld == null) {
			return;
		}
		HarpoonEntityRenderer.renderInStack(this.itemRenderState, client, clientWorld, matrices, vertexConsumers, light);
	}
}
