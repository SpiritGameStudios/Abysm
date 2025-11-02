package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BloomshroomCapBlock extends Block {
	public static final MapCodec<BloomshroomCapBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			ParticleTypes.CODEC.fieldOf("particle").forGetter(block -> block.particle),
			propertiesCodec()
		).apply(instance, BloomshroomCapBlock::new)
	);

	public final ParticleOptions particle;

	@Override
	public MapCodec<BloomshroomCapBlock> codec() {
		return CODEC;
	}

	public BloomshroomCapBlock(ParticleOptions particle, Properties settings) {
		super(settings);
		this.particle = particle;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);

		BlockPos downPos = pos.below();
		BlockState downState = world.getBlockState(downPos);

		if (!downState.isFaceSturdy(world, pos, Direction.UP)) {
			if (random.nextFloat() < 0.05F) {
				double x = pos.getX() + 0.05 + 0.9 * random.nextFloat();
				double y = pos.getY() - 0.1 + 0.1 * random.nextFloat();
				double z = pos.getZ() + 0.05 + 0.9 * random.nextFloat();

				world.addParticle(this.particle, x, y, z, 0, 0, 0);
			}
		}
	}
}
