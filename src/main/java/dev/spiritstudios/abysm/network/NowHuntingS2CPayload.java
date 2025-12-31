package dev.spiritstudios.abysm.network;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record NowHuntingS2CPayload(int entityId, boolean hunting) implements AutoSendPayload {
	public static final StreamCodec<FriendlyByteBuf, NowHuntingS2CPayload> STREAM_CODEC = CustomPacketPayload.codec(NowHuntingS2CPayload::write, NowHuntingS2CPayload::new);
	public static final Type<NowHuntingS2CPayload> ID = new Type<>(Abysm.id("now_hunting_s2c"));

	private NowHuntingS2CPayload(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean());
	}

	private void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeBoolean(this.hunting);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
