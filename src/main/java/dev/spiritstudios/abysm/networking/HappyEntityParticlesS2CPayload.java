package dev.spiritstudios.abysm.networking;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

public record HappyEntityParticlesS2CPayload(int entityId, ParticleEffect particleEffect, int count) implements AutoSendPayload {

	public static final Id<HappyEntityParticlesS2CPayload> ID = new Id<>(Abysm.id("entity_finished_eating_s2c"));

	public static final PacketCodec<RegistryByteBuf, HappyEntityParticlesS2CPayload> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.VAR_INT, payload -> payload.entityId,
		ParticleTypes.PACKET_CODEC, payload -> payload.particleEffect,
		PacketCodecs.VAR_INT, payload -> payload.count,
		HappyEntityParticlesS2CPayload::new
	);

	public HappyEntityParticlesS2CPayload(Entity entity, ParticleEffect particleEffect, int count) {
		this(entity.getId(), particleEffect, count);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
