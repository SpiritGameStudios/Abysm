package dev.spiritstudios.abysm.networking;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

public record EntityFinishedEatingS2CPayload(int entityId, ParticleEffect particleEffect) implements CustomPayload {

	public static final Id<EntityFinishedEatingS2CPayload> ID = new Id<>(Abysm.id("entity_finished_eating_s2c"));

	public static final PacketCodec<RegistryByteBuf, EntityFinishedEatingS2CPayload> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.VAR_INT, payload -> payload.entityId,
		ParticleTypes.PACKET_CODEC, payload -> payload.particleEffect,
		EntityFinishedEatingS2CPayload::new
	);

	public EntityFinishedEatingS2CPayload(Entity entity, ParticleEffect particleEffect) {
		this(entity.getId(), particleEffect);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
