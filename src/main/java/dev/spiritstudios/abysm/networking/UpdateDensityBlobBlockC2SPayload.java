package dev.spiritstudios.abysm.networking;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record UpdateDensityBlobBlockC2SPayload(BlockPos pos, String finalState) implements CustomPayload {
	public static final PacketCodec<PacketByteBuf, UpdateDensityBlobBlockC2SPayload> PACKET_CODEC = CustomPayload.codecOf(UpdateDensityBlobBlockC2SPayload::write, UpdateDensityBlobBlockC2SPayload::new);
	public static final Id<UpdateDensityBlobBlockC2SPayload> ID = new Id<>(Abysm.id("update_density_blob_block_c2s"));

	private UpdateDensityBlobBlockC2SPayload(PacketByteBuf buf) {
		this(buf.readBlockPos(), buf.readString());
	}

	private void write(PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
		buf.writeString(this.finalState);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateDensityBlobBlockC2SPayload> {
		public static final Receiver INSTANCE = new Receiver();

		protected Receiver() {
		}

		@Override
		public void receive(UpdateDensityBlobBlockC2SPayload payload, ServerPlayNetworking.Context context) {
			PlayerEntity player = context.player();
			if (player.isCreativeLevelTwoOp()) {
				World world = player.getWorld();
				BlockPos blockPos = payload.pos;
				BlockState blockState = world.getBlockState(blockPos);
				if (world.getBlockEntity(blockPos) instanceof DensityBlobBlockEntity blockEntity) {
					blockEntity.setFinalState(payload.finalState);
					blockEntity.markDirty();
					world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
				}
			}
		}
	}
}
