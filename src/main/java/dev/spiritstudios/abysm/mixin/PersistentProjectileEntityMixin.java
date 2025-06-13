package dev.spiritstudios.abysm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.component.BlessedComponent;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.registry.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.registry.AbysmItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

	@ModifyExpressionValue(method = "writeCustomDataToNbt", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;"))
	private Codec<ItemStack> encodeOptional(Codec<ItemStack> original) {
		if ((PersistentProjectileEntity) (Object) this instanceof HarpoonEntity) {
			return ItemStack.OPTIONAL_CODEC;
		}
		return original;
	}

	@WrapOperation(method = "tryPickup", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
	private boolean reloadHarpoon(PlayerInventory instance, ItemStack stack, Operation<Boolean> original) {
		if ((PersistentProjectileEntity) (Object) this instanceof HarpoonEntity harpoon) {
			try {
				ItemStack invStack = instance.getStack(harpoon.getSlot());
				if (!invStack.isOf(AbysmItems.NOOPRAH)) {
					return true;
				}
				BlessedComponent component = invStack.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY);
				if (component.isLoaded()) {
					return true;
				}
				invStack.set(AbysmDataComponentTypes.BLESSED, component.buildNew().loaded(true).build());
				return true;
			} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
				Abysm.LOGGER.debug("An error occurred while picking up a harpoon!", indexOutOfBoundsException);
				return true;
			}
		}
		return original.call(instance, stack);
	}

	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setPitch(F)V"))
	private boolean letMeUpdateMyPitch(PersistentProjectileEntity instance, float v) {
		return !((PersistentProjectileEntity) (Object) this instanceof HarpoonEntity);
	}

	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setYaw(F)V"))
	private boolean letMeUpdateMyYaw(PersistentProjectileEntity instance, float v) {
		return !((PersistentProjectileEntity) (Object) this instanceof HarpoonEntity);
	}
}
