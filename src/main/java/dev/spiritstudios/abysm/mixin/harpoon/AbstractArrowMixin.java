package dev.spiritstudios.abysm.mixin.harpoon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import dev.spiritstudios.abysm.world.entity.harpoon.HarpoonEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
	@ModifyExpressionValue(method = "addAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;"))
	private Codec<ItemStack> encodeOptional(Codec<ItemStack> original) {
		if (((AbstractArrow) (Object) this) instanceof HarpoonEntity) {
			return ItemStack.OPTIONAL_CODEC;
		}
		return original;
	}
}
