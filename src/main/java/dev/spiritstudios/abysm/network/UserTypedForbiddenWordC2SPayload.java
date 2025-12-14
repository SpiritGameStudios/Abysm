package dev.spiritstudios.abysm.network;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.entity.AbysmDamageTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public record UserTypedForbiddenWordC2SPayload() implements CustomPacketPayload {

	public static final UserTypedForbiddenWordC2SPayload INSTANCE = new UserTypedForbiddenWordC2SPayload();

	public static final Type<UserTypedForbiddenWordC2SPayload> ID = new Type<>(Abysm.id("user_typed_forbidden_word_c2s"));

	public static final StreamCodec<RegistryFriendlyByteBuf, UserTypedForbiddenWordC2SPayload> PACKET_CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void receive(UserTypedForbiddenWordC2SPayload ignored, ServerPlayNetworking.Context context) {
		ServerPlayer player = context.player();
		ServerLevel world = player.level();
		context.player().hurtServer(
			world,
			new DamageSource(AbysmDamageTypes.getOrThrow(world, AbysmDamageTypes.PRESSURE)),
			Float.MAX_VALUE
		);
	}
}
