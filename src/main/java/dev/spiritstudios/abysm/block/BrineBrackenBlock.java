package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.fluids.AbysmFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class BrineBrackenBlock extends UnderwaterPlantBlock {

    private static final MapCodec<BrineBrackenBlock> CODEC = createCodec(BrineBrackenBlock::new);
    private static final VoxelShape SHAPE = createColumnShape(10, 0, 13);

    public BrineBrackenBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE.offset(state.getModelOffset(pos));
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (super.canPlaceAt(state, world, pos))
            return true;

        for (Direction direction : Direction.Type.HORIZONTAL) {
            FluidState fluidState = world.getFluidState(pos.down().offset(direction));

            if (fluidState.getFluid().matchesType(AbysmFluids.BRINE))
                return true;
        }

        return false;
    }

}
