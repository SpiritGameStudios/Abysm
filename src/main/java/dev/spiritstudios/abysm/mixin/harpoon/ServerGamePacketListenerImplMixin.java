package dev.spiritstudios.abysm.mixin.harpoon;

import dev.spiritstudios.abysm.world.entity.harpoon.HarpoonDrag;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
	@Inject(method = "noBlocksAround", at = @At(value = "HEAD"), cancellable = true)
	private void flyingHarpoon(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof HarpoonDrag harpoonDrag && harpoonDrag.abysm$getDragTicks() > 0) {
			cir.setReturnValue(false);
		}
	}
}
