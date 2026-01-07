package dev.spiritstudios.abysm.network;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.duck.MouseInputPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public record MouseInputC2SPayload(MouseInput input) implements CustomPacketPayload {
	public static final Type<MouseInputC2SPayload> TYPE = new Type<>(Abysm.id("mouse_input"));

	public static final StreamCodec<FriendlyByteBuf, MouseInputC2SPayload> STREAM_CODEC = MouseInput.STREAM_CODEC.map(
		MouseInputC2SPayload::new,
		MouseInputC2SPayload::input
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void receive(MouseInputC2SPayload payload, ServerPlayNetworking.Context context) {
		Player player = context.player();
		((MouseInputPlayer) player).spectre$latestInput(payload.input);
	}
}
