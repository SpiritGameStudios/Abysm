package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BloomshroomGoopBlock extends WaterloggableTranslucentBlock {
	public static final MapCodec<WaterloggableTranslucentBlock> CODEC = createCodec(BloomshroomGoopBlock::new);

	@Override
	protected MapCodec<? extends WaterloggableTranslucentBlock> getCodec() {
		return CODEC;
	}

	public BloomshroomGoopBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		boolean waterlogged = state.get(WATERLOGGED, false);

		for(Direction direction : DIRECTIONS) {
			BlockPos adjPos = pos.offset(direction);
			BlockState adjState = world.getBlockState(adjPos);

			if(!adjState.isSideSolid(world, adjPos, direction.getOpposite(), SideShapeType.FULL)) {
				if(random.nextInt(waterlogged ? 2 : 4) == 0) {
					double x = pos.getX() + random.nextFloat();
					double y = pos.getY() + random.nextFloat();
					double z = pos.getZ() + random.nextFloat();

					double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
					double vy = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
					double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);

					Vec3i vector = direction.getVector();
					switch(direction.getAxis()) {
						case X -> {
							x = pos.getX() + 0.5F + 0.5F * vector.getX();
							vx *= 0.1F;
							vx += vector.getX() * random.nextFloat() * 0.05F;
						}
						case Y -> {
							y = pos.getY() + 0.5F + 0.5F * vector.getY();
							vy *= 0.1F;
							vy += vector.getY() * random.nextFloat() * 0.05F;
						}
						default -> {
							z = pos.getZ() + 0.5F + 0.5F * vector.getZ();
							vz *= 0.1F;
							vz += vector.getZ() * random.nextFloat() * 0.05F;
						}
					}

					world.addParticleClient(AbysmParticleTypes.ROSEBLOOM_GLIMMER, x, y, z, vx, vy, vz);
				}
			}
		}
	}
}
