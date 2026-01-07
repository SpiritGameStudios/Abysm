package dev.spiritstudios.abysm.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record MouseInput(boolean left, boolean right) {
	private static final byte FLAG_LEFT = 1;
	private static final byte FLAG_RIGHT = 1 << 1;



	public static final MouseInput EMPTY = new MouseInput(false, false);

	public static final StreamCodec<FriendlyByteBuf, MouseInput> STREAM_CODEC = new StreamCodec<>() {
		public void encode(FriendlyByteBuf friendlyByteBuf, MouseInput input) {
			byte b = 0;
			b = (byte)(b | (input.left() ? FLAG_LEFT : 0));
			b = (byte)(b | (input.right() ? FLAG_RIGHT : 0));
			friendlyByteBuf.writeByte(b);
		}

		public MouseInput decode(FriendlyByteBuf friendlyByteBuf) {
			byte b = friendlyByteBuf.readByte();
			boolean left = (b & FLAG_LEFT) != 0;
			boolean right = (b & FLAG_RIGHT) != 0;
			return new MouseInput(left, right);
		}
	};
}
