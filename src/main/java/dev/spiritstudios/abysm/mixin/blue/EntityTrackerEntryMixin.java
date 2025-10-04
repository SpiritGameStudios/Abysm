package dev.spiritstudios.abysm.mixin.blue;

import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import dev.spiritstudios.abysm.networking.EntityUpdateBlueS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin {

	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "startTracking", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", shift = At.Shift.AFTER))
	private void sendBlue(ServerPlayerEntity player, CallbackInfo ci) {
		if (this.entity instanceof LivingEntityDuck duck) {
			if (duck.abysm$checkWasBlue() != duck.abysm$isBlue()) {
				ServerPlayNetworking.send(player, new EntityUpdateBlueS2CPayload(this.entity.getId(), duck.abysm$isBlue()));
			}
		}
	}
}
