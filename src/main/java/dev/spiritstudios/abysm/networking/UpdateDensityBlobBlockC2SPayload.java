package dev.spiritstudios.abysm.networking;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record UpdateDensityBlobBlockC2SPayload(BlockPos pos, String finalState,
											   String blobsSamplerIdentifier) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, UpdateDensityBlobBlockC2SPayload> PACKET_CODEC = CustomPacketPayload.codec(UpdateDensityBlobBlockC2SPayload::write, UpdateDensityBlobBlockC2SPayload::new);
	public static final Type<UpdateDensityBlobBlockC2SPayload> ID = new Type<>(Abysm.id("update_density_blob_block_c2s"));

	private UpdateDensityBlobBlockC2SPayload(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readUtf(), buf.readUtf());
	}

	private void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos);
		buf.writeUtf(this.finalState);
		buf.writeUtf(this.blobsSamplerIdentifier);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void receive(UpdateDensityBlobBlockC2SPayload payload, ServerPlayNetworking.Context context) {
		Player player = context.player();
		if (!player.canUseGameMasterBlocks()) {
			return;
		}
		Level world = player.level();
		BlockPos blockPos = payload.pos;
		if (!(world.getBlockEntity(blockPos) instanceof DensityBlobBlockEntity blockEntity)) {
			return;
		}
		BlockState blockState = world.getBlockState(blockPos);
		blockEntity.setFinalState(payload.finalState);
		blockEntity.setBlobsSamplerIdentifier(payload.blobsSamplerIdentifier);
		blockEntity.setChanged();
		world.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
	}
}
