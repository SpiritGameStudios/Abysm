package dev.spiritstudios.abysm.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BloomshroomSprigsBlock extends UnderwaterPlantBlock {
	protected static final MapCodec<SimpleParticleType> PARTICLE_TYPE_CODEC = Registries.PARTICLE_TYPE
		.getCodec()
		.comapFlatMap(
			particleType -> particleType instanceof SimpleParticleType simpleParticleType
				? DataResult.success(simpleParticleType)
				: DataResult.error(() -> "Not a SimpleParticleType: " + particleType),
			particleType -> particleType
		)
		.fieldOf("particle_options");
	public static final MapCodec<BloomshroomSprigsBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(PARTICLE_TYPE_CODEC.forGetter(block -> block.particle), createSettingsCodec()).apply(instance, BloomshroomSprigsBlock::new)
	);
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public final SimpleParticleType particle;

	@Override
	public MapCodec<? extends UnderwaterPlantBlock> getCodec() {
		return CODEC;
	}

	public BloomshroomSprigsBlock(SimpleParticleType particle, Settings settings) {
		super(settings);
		this.particle = particle;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		boolean waterlogged = state.get(WATERLOGGED, false);

		if(random.nextInt(waterlogged ? 2 : 5) == 0) {
			Vec3d offset = state.getModelOffset(pos);
			double x = pos.getX() + offset.x + 0.1 + 0.8 * random.nextFloat();
			double y = pos.getY() + offset.y + 0.15 + 0.25 * random.nextFloat();
			double z = pos.getZ() + offset.z + 0.1 + 0.8 * random.nextFloat();

			double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
			double vy = (waterlogged ? 0.02 : 0.01) + random.nextFloat() * (waterlogged ? 0.1F : 0.04F);
			double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);

			world.addParticleClient(this.particle, x, y, z, vx, vy, vz);
		}
	}
}
