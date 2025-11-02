package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BloomshroomCrownBlock extends RotatableWaterloggableFlowerBlock {
	public static final MapCodec<BloomshroomCrownBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			ParticleTypes.CODEC.fieldOf("glimmer").forGetter(block -> block.glimmerParticle),
			ParticleTypes.CODEC.fieldOf("thorns").forGetter(block -> block.thornsParticle),
			propertiesCodec()
		).apply(instance, BloomshroomCrownBlock::new)
	);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

	public final ParticleOptions glimmerParticle;
	public final ParticleOptions thornsParticle;

	@Override
	public MapCodec<? extends RotatableWaterloggableFlowerBlock> codec() {
		return CODEC;
	}

	public BloomshroomCrownBlock(ParticleOptions glimmerParticle, ParticleOptions thornsParticle, Properties settings) {
		super(settings);
		this.glimmerParticle = glimmerParticle;
		this.thornsParticle = thornsParticle;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);

		boolean waterlogged = state.getValueOrElse(WATERLOGGED, false);
		Direction direction = state.getValueOrElse(FACING, Direction.UP);

		int glimmerCount = waterlogged ? 1 + random.nextInt(3) : random.nextInt(2);
		for (int i = 0; i < glimmerCount; i++) {
			spawnParticles(world, pos, direction, waterlogged, random, this.glimmerParticle, 0.4F, 1F, 1F);
		}

		int thornsCount = waterlogged ? 2 + random.nextInt(2) : 1;
		for (int i = 0; i < thornsCount; i++) {
			spawnParticles(world, pos, direction, waterlogged, random, this.thornsParticle, 0.9F, 2.6F, 1.4F);
		}
	}

	protected void spawnParticles(Level world, BlockPos pos, Direction direction, boolean waterlogged, RandomSource random, ParticleOptions particle, float width, float orthogonalVelocityMultiplier, float normalVelocityMultiplier) {
		double x = pos.getX() + 0.5 + width * (random.nextFloat() - 0.5);
		double y = pos.getY() + 0.5 + width * (random.nextFloat() - 0.5);
		double z = pos.getZ() + 0.5 + width * (random.nextFloat() - 0.5);

		double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008) * orthogonalVelocityMultiplier;
		double vy = random.nextGaussian() * (waterlogged ? 0.015 : 0.008) * orthogonalVelocityMultiplier;
		double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008) * orthogonalVelocityMultiplier;

		Vec3i vector = direction.getUnitVec3i();
		switch (direction.getAxis()) {
			case X -> {
				x = pos.getX() + 0.5F - 0.45F * vector.getX();
				vx *= 0.1F;
				vx += vector.getX() * random.nextFloat() * 0.12F * normalVelocityMultiplier;
			}
			case Y -> {
				y = pos.getY() + 0.5F - 0.45F * vector.getY();
				vy *= 0.1F;
				vy += vector.getY() * random.nextFloat() * 0.12F * normalVelocityMultiplier;
			}
			default -> {
				z = pos.getZ() + 0.5F - 0.45F * vector.getZ();
				vz *= 0.1F;
				vz += vector.getZ() * random.nextFloat() * 0.12F * normalVelocityMultiplier;
			}
		}

		world.addParticle(particle, x, y, z, vx, vy, vz);
	}
}
