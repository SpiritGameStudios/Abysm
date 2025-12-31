package dev.spiritstudios.abysm.network;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record EntityUpdateBlueS2CPayload(int entityId, boolean isBlue) implements AutoSendPayload {
	public static final StreamCodec<FriendlyByteBuf, EntityUpdateBlueS2CPayload> STREAM_CODEC = CustomPacketPayload.codec(EntityUpdateBlueS2CPayload::write, EntityUpdateBlueS2CPayload::new);
	public static final Type<EntityUpdateBlueS2CPayload> ID = new Type<>(Abysm.id("entity_update_blue_s2c"));

	private EntityUpdateBlueS2CPayload(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean());
	}

	private void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeBoolean(this.isBlue);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
