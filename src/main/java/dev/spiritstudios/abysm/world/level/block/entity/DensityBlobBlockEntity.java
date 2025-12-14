package dev.spiritstudios.abysm.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DensityBlobBlockEntity extends BlockEntity {
	public static final String FINAL_STATE_KEY = "final_state";
	public static final String BLOBS_SAMPLER_IDENTIFIER = "blobs_sampler_identifier";
	private String finalState = "minecraft:air";
	private String blobsSamplerIdentifier = "abysm:empty";

	public DensityBlobBlockEntity(BlockPos pos, BlockState state) {
		super(AbysmBlockEntityTypes.DENSITY_BLOB_BLOCK, pos, state);
	}

	public String getFinalState() {
		return this.finalState;
	}

	public void setFinalState(String finalState) {
		this.finalState = finalState;
	}

	public String getBlobsSamplerIdentifier() {
		return this.blobsSamplerIdentifier;
	}

	public void setBlobsSamplerIdentifier(String blobsSamplerIdentifier) {
		this.blobsSamplerIdentifier = blobsSamplerIdentifier;
	}

	@Override
	protected void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putString(FINAL_STATE_KEY, this.finalState);
		view.putString(BLOBS_SAMPLER_IDENTIFIER, this.blobsSamplerIdentifier);
	}

	@Override
	protected void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		this.finalState = view.getStringOr(FINAL_STATE_KEY, "minecraft:air");
		this.blobsSamplerIdentifier = view.getStringOr(BLOBS_SAMPLER_IDENTIFIER, "abysm:empty");
	}


	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}
}
