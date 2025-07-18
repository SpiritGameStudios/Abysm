package dev.spiritstudios.abysm.client.mixin.harpoon;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
	@WrapMethod(method = "getArmPose(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;")
	private static BipedEntityModel.ArmPose harpoonPose(PlayerEntity player, ItemStack stack, Hand hand, Operation<BipedEntityModel.ArmPose> original) {
		return !player.handSwinging && stack.isOf(AbysmItems.HARPOON) ?
			BipedEntityModel.ArmPose.CROSSBOW_HOLD :
			original.call(player, stack, hand);
	}
}
