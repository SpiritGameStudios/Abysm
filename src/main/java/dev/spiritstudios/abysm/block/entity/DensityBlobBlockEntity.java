package dev.spiritstudios.abysm.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class DensityBlobBlockEntity extends BlockEntity {
	public static final String FINAL_STATE_KEY = "final_state";
	private String finalState = "minecraft:air";

	public DensityBlobBlockEntity(BlockPos pos, BlockState state) {
		super(AbysmBlockEntityTypes.DENSITY_BLOB_BLOCK, pos, state);
	}

	public String getFinalState() {
		return this.finalState;
	}

	public void setFinalState(String finalState) {
		this.finalState = finalState;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(nbt, registries);
		nbt.putString(FINAL_STATE_KEY, this.finalState);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(nbt, registries);
		this.finalState = nbt.getString(FINAL_STATE_KEY, "minecraft:air");
	}

	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
		return this.createComponentlessNbt(registries);
	}
}
