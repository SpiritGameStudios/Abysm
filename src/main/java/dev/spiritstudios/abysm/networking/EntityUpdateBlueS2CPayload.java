package dev.spiritstudios.abysm.networking;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record EntityUpdateBlueS2CPayload(int entityId, boolean isBlue) implements AutoSendPayload {
	public static final PacketCodec<PacketByteBuf, EntityUpdateBlueS2CPayload> PACKET_CODEC = CustomPayload.codecOf(EntityUpdateBlueS2CPayload::write, EntityUpdateBlueS2CPayload::new);
	public static final Id<EntityUpdateBlueS2CPayload> ID = new Id<>(Abysm.id("entity_update_blue_s2c"));

	private EntityUpdateBlueS2CPayload(PacketByteBuf buf) {
		this(buf.readInt(), buf.readBoolean());
	}

	private void write(PacketByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeBoolean(this.isBlue);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
