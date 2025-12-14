package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;

public class NectarsapBlock extends WaterloggableTranslucentBlock {
	public static final MapCodec<NectarsapBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				ParticleTypes.CODEC.fieldOf("particle").forGetter(block -> block.particle),
				propertiesCodec()
			)
			.apply(instance, NectarsapBlock::new)
	);

	@Override
	protected MapCodec<? extends WaterloggableTranslucentBlock> codec() {
		return CODEC;
	}

	public final ParticleOptions particle;

	public NectarsapBlock(ParticleOptions particle, Properties settings) {
		super(settings);
		this.particle = particle;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);

		boolean waterlogged = state.getValueOrElse(WATERLOGGED, false);

		for (Direction direction : UPDATE_SHAPE_ORDER) {
			BlockPos adjPos = pos.relative(direction);
			BlockState adjState = world.getBlockState(adjPos);

			if (!adjState.isFaceSturdy(world, adjPos, direction.getOpposite(), SupportType.FULL)) {
				if (random.nextInt(waterlogged ? 2 : 4) == 0) {
					double x = pos.getX() + random.nextFloat();
					double y = pos.getY() + random.nextFloat();
					double z = pos.getZ() + random.nextFloat();

					double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
					double vy = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
					double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);

					Vec3i vector = direction.getUnitVec3i();
					switch (direction.getAxis()) {
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

					world.addParticle(this.particle, x, y, z, vx, vy, vz);
				}
			}
		}
	}
}
