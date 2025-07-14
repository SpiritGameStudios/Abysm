package dev.spiritstudios.abysm.client.mixin.harpoon;

import dev.spiritstudios.abysm.client.duck.HarpoonItemRenderState;
import dev.spiritstudios.abysm.component.HarpoonComponent;
import dev.spiritstudios.abysm.item.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelManager.class)
public abstract class ItemModelManagerMixin {

	@Inject(method = "update", at = @At("HEAD"))
	private void updateHarpoon(ItemRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, World world, LivingEntity entity, int seed, CallbackInfo ci) {
		boolean harpoon = false;
		if (stack.isOf(AbysmItems.NOOPRAH)) {
			if (stack.getOrDefault(AbysmDataComponentTypes.BLESSED, HarpoonComponent.EMPTY).loaded()) {
				harpoon = true;
			}
		}
		((HarpoonItemRenderState) renderState).abysm$setShouldRenderHarpoon(harpoon);
	}
}
