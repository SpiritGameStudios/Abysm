package dev.spiritstudios.abysm.client.mixin.harpoon;

import dev.spiritstudios.abysm.client.duck.HarpoonItemRenderState;
import dev.spiritstudios.abysm.client.render.entity.harpoon.HarpoonEntityRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {

	@Shadow
	@Final
	private ItemRenderState itemRenderState;

	@Shadow
	@Final
	private MatrixStack matrices;

	@Shadow
	@Final
	private VertexConsumerProvider.Immediate vertexConsumers;

	@Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE))
	private void renderHarpoonInGUI(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci) {
		if (!((HarpoonItemRenderState) this.itemRenderState).abysm$shouldRenderHarpoon()) {
			return;
		}
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld clientWorld = client.world;
		if (clientWorld == null) {
			return;
		}
		HarpoonEntityRenderer.renderInStack(this.itemRenderState, client, clientWorld, this.matrices, this.vertexConsumers, 15728880);
	}
}
