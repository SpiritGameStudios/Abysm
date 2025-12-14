package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BloomshroomSprigsBlock extends UnderwaterPlantBlock {
	public static final MapCodec<BloomshroomSprigsBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			ParticleTypes.CODEC.fieldOf("particle").forGetter(block -> block.particle),
			propertiesCodec()
		).apply(instance, BloomshroomSprigsBlock::new)
	);
	private static final VoxelShape SHAPE = Block.column(10.0, 0.0, 11.0);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public final ParticleOptions particle;

	@Override
	public MapCodec<BloomshroomSprigsBlock> codec() {
		return CODEC;
	}

	public BloomshroomSprigsBlock(ParticleOptions particle, Properties settings) {
		super(settings);
		this.particle = particle;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.move(state.getOffset(pos));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);

		boolean waterlogged = state.getValueOrElse(WATERLOGGED, false);

		if (random.nextInt(waterlogged ? 2 : 5) == 0) {
			Vec3 offset = state.getOffset(pos);
			double x = pos.getX() + offset.x + 0.1 + 0.8 * random.nextFloat();
			double y = pos.getY() + offset.y + 0.15 + 0.25 * random.nextFloat();
			double z = pos.getZ() + offset.z + 0.1 + 0.8 * random.nextFloat();

			double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
			double vy = (waterlogged ? 0.02 : 0.01) + random.nextFloat() * (waterlogged ? 0.1F : 0.04F);
			double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);

			world.addParticle(this.particle, x, y, z, vx, vy, vz);
		}
	}
}
