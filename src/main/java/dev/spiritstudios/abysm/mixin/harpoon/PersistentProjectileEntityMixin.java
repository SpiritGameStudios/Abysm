package dev.spiritstudios.abysm.mixin.harpoon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
	@ModifyExpressionValue(method = "writeCustomData", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;"))
	private Codec<ItemStack> encodeOptional(Codec<ItemStack> original) {
		if ((PersistentProjectileEntity) (Object) this instanceof HarpoonEntity) {
			return ItemStack.OPTIONAL_CODEC;
		}
		return original;
	}
}
