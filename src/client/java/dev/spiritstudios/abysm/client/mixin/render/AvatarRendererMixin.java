package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
	@ModifyExpressionValue(method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;isCharged(Lnet/minecraft/world/item/ItemStack;)Z"))
	private static boolean harpoonHold(boolean original, @Local(argsOnly = true) ItemStack handItem) {
		return original && handItem.is(AbysmItems.HARPOON);
	}
}
