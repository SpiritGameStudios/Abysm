package dev.spiritstudios.abysm.mixin.harpoon;

import dev.spiritstudios.abysm.entity.harpoon.HarpoonDrag;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	@Inject(method = "isEntityOnAir", at = @At(value = "HEAD"), cancellable = true)
	private void flyingHarpoon(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof HarpoonDrag harpoonDrag && harpoonDrag.abysm$getDragTicks() > 0) {
			cir.setReturnValue(false);
		}
	}
}
