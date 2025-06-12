package dev.spiritstudios.abysm.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BloomshroomCapBlock extends Block {
	protected static final MapCodec<SimpleParticleType> PARTICLE_TYPE_CODEC = Registries.PARTICLE_TYPE
		.getCodec()
		.comapFlatMap(
			particleType -> particleType instanceof SimpleParticleType simpleParticleType
				? DataResult.success(simpleParticleType)
				: DataResult.error(() -> "Not a SimpleParticleType: " + particleType),
			particleType -> particleType
		)
		.fieldOf("particle_options");
	public static final MapCodec<BloomshroomCapBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(PARTICLE_TYPE_CODEC.forGetter(block -> block.particle), createSettingsCodec()).apply(instance, BloomshroomCapBlock::new)
	);

	public final SimpleParticleType particle;

	@Override
	public MapCodec<BloomshroomCapBlock> getCodec() {
		return CODEC;
	}

	public BloomshroomCapBlock(SimpleParticleType particle, Settings settings) {
		super(settings);
		this.particle = particle;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		BlockPos downPos = pos.down();
		BlockState downState = world.getBlockState(downPos);

		if(!downState.isSideSolid(world, pos, Direction.UP, SideShapeType.FULL)) {
			if (random.nextInt(4) == 0) {
				double x = pos.getX() + 0.05 + 0.9 * random.nextFloat();
				double y = pos.getY() - 0.1 + 0.1 * random.nextFloat();
				double z = pos.getZ() + 0.05 + 0.9 * random.nextFloat();

				world.addParticleClient(this.particle, x, y, z, 0, 0, 0);
			}
		}
	}
}
