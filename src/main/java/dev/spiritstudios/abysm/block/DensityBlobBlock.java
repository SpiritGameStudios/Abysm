package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DensityBlobBlock extends Block implements BlockEntityProvider, OperatorBlock {
	public static final MapCodec<FloropumiceBlock> CODEC = createCodec(FloropumiceBlock::new);

	@Override
	public MapCodec<FloropumiceBlock> getCodec() {
		return CODEC;
	}

	public DensityBlobBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DensityBlobBlockEntity(pos, state);
	}
}
