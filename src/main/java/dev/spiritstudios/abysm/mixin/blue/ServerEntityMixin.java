package dev.spiritstudios.abysm.mixin.blue;

import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import dev.spiritstudios.abysm.network.EntityUpdateBlueS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "addPairing", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.AFTER))
	private void sendBlue(ServerPlayer player, CallbackInfo ci) {
		if (this.entity instanceof LivingEntityDuck duck) {
			if (duck.abysm$checkWasBlue() != duck.abysm$isBlue()) {
				ServerPlayNetworking.send(player, new EntityUpdateBlueS2CPayload(this.entity.getId(), duck.abysm$isBlue()));
			}
		}
	}
}
