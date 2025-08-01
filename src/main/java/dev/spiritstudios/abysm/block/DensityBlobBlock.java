package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.duck.PlayerEntityDuck;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof DensityBlobBlockEntity densityBlobBlockEntity && player.isCreativeLevelTwoOp()) {
			if (player instanceof PlayerEntityDuck playerEntityDuck) {
				playerEntityDuck.abysm$openDensityBlobScreen(densityBlobBlockEntity);
			}
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.PASS;
		}
	}
}
