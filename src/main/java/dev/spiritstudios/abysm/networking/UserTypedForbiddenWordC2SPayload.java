package dev.spiritstudios.abysm.networking;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public record UserTypedForbiddenWordC2SPayload() implements CustomPayload {

	public static final UserTypedForbiddenWordC2SPayload INSTANCE = new UserTypedForbiddenWordC2SPayload();

	public static final Id<UserTypedForbiddenWordC2SPayload> ID = new Id<>(Abysm.id("user_typed_forbidden_word_c2s"));

	public static final PacketCodec<RegistryByteBuf, UserTypedForbiddenWordC2SPayload> PACKET_CODEC = PacketCodec.unit(INSTANCE);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<UserTypedForbiddenWordC2SPayload> {

		public static final Receiver INSTANCE = new Receiver();

		protected Receiver() {

		}

		@Override
		public void receive(UserTypedForbiddenWordC2SPayload payload, ServerPlayNetworking.Context context) {
			ServerPlayerEntity player = context.player();
			ServerWorld serverWorld = player.getServerWorld();
			context.player().damage(serverWorld,
				new DamageSource(AbysmDamageTypes.getOrThrow(serverWorld, AbysmDamageTypes.PRESSURE)),
				Float.MAX_VALUE);
		}
	}
}
