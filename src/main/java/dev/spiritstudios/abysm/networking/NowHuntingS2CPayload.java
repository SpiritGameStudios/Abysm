package dev.spiritstudios.abysm.networking;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record NowHuntingS2CPayload(int entityId, boolean hunting) implements AutoSendPayload {
	public static final PacketCodec<PacketByteBuf, NowHuntingS2CPayload> PACKET_CODEC = CustomPayload.codecOf(NowHuntingS2CPayload::write, NowHuntingS2CPayload::new);
	public static final Id<NowHuntingS2CPayload> ID = new Id<>(Abysm.id("now_hunting_s2c"));

	private NowHuntingS2CPayload(PacketByteBuf buf) {
		this(buf.readInt(), buf.readBoolean());
	}

	private void write(PacketByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeBoolean(this.hunting);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
