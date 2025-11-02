package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.duck.DensityPlayerEntityDuck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DensityBlobBlock extends Block implements EntityBlock, GameMasterBlock {
	public static final MapCodec<FloropumiceBlock> CODEC = simpleCodec(FloropumiceBlock::new);

	@Override
	public MapCodec<FloropumiceBlock> codec() {
		return CODEC;
	}

	public DensityBlobBlock(Properties settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DensityBlobBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof DensityBlobBlockEntity densityBlobBlockEntity) || !player.canUseGameMasterBlocks()) {
			return InteractionResult.PASS;
		}
		if (player instanceof DensityPlayerEntityDuck clientPlayer) {
			clientPlayer.abysm$openDensityBlobScreen(densityBlobBlockEntity);
		}
		return InteractionResult.SUCCESS;
	}
}
