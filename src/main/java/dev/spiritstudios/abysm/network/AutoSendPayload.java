package dev.spiritstudios.abysm.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

public interface AutoSendPayload extends CustomPacketPayload {

	default void send(Entity entity) {
		PlayerLookup.tracking(entity).forEach(player -> ServerPlayNetworking.send(player, this));
	}
}
