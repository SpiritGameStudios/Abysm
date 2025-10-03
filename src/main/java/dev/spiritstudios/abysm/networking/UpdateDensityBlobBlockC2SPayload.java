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

public record UpdateDensityBlobBlockC2SPayload(BlockPos pos, String finalState,
											   String blobsSamplerIdentifier) implements CustomPayload {
	public static final PacketCodec<PacketByteBuf, UpdateDensityBlobBlockC2SPayload> PACKET_CODEC = CustomPayload.codecOf(UpdateDensityBlobBlockC2SPayload::write, UpdateDensityBlobBlockC2SPayload::new);
	public static final Id<UpdateDensityBlobBlockC2SPayload> ID = new Id<>(Abysm.id("update_density_blob_block_c2s"));

	private UpdateDensityBlobBlockC2SPayload(PacketByteBuf buf) {
		this(buf.readBlockPos(), buf.readString(), buf.readString());
	}

	private void write(PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
		buf.writeString(this.finalState);
		buf.writeString(this.blobsSamplerIdentifier);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void receive(UpdateDensityBlobBlockC2SPayload payload, ServerPlayNetworking.Context context) {
		PlayerEntity player = context.player();
		if (!player.isCreativeLevelTwoOp()) {
			return;
		}
		World world = player.getWorld();
		BlockPos blockPos = payload.pos;
		if (!(world.getBlockEntity(blockPos) instanceof DensityBlobBlockEntity blockEntity)) {
			return;
		}
		BlockState blockState = world.getBlockState(blockPos);
		blockEntity.setFinalState(payload.finalState);
		blockEntity.setBlobsSamplerIdentifier(payload.blobsSamplerIdentifier);
		blockEntity.markDirty();
		world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
	}
}
