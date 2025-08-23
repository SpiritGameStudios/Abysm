package dev.spiritstudios.abysm.networking;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.CustomPayload;

public interface AutoSendPayload extends CustomPayload {

	default void send(Entity entity) {
		PlayerLookup.tracking(entity).forEach(player -> ServerPlayNetworking.send(player, this));
	}
}
