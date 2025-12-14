package dev.spiritstudios.abysm.network;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

public record HappyEntityParticlesS2CPayload(int entityId, ParticleOptions particleEffect, int count) implements AutoSendPayload {

	public static final Type<HappyEntityParticlesS2CPayload> ID = new Type<>(Abysm.id("entity_finished_eating_s2c"));

	public static final StreamCodec<RegistryFriendlyByteBuf, HappyEntityParticlesS2CPayload> PACKET_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, payload -> payload.entityId,
		ParticleTypes.STREAM_CODEC, payload -> payload.particleEffect,
		ByteBufCodecs.VAR_INT, payload -> payload.count,
		HappyEntityParticlesS2CPayload::new
	);

	public HappyEntityParticlesS2CPayload(Entity entity, ParticleOptions particleEffect, int count) {
		this(entity.getId(), particleEffect, count);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
