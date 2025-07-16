package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BloomshroomCrownBlock extends RotatableWaterloggableFlowerBlock {
	public static final MapCodec<BloomshroomCrownBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			ParticleTypes.TYPE_CODEC.fieldOf("glimmer").forGetter(block -> block.glimmerParticle),
			ParticleTypes.TYPE_CODEC.fieldOf("thorns").forGetter(block -> block.thornsParticle),
			createSettingsCodec()
		).apply(instance, BloomshroomCrownBlock::new)
	);
	public static final EnumProperty<Direction> FACING = Properties.FACING;

	public final ParticleEffect glimmerParticle;
	public final ParticleEffect thornsParticle;

	@Override
	public MapCodec<? extends RotatableWaterloggableFlowerBlock> getCodec() {
		return CODEC;
	}

	public BloomshroomCrownBlock(ParticleEffect glimmerParticle, ParticleEffect thornsParticle, Settings settings) {
		super(settings);
		this.glimmerParticle = glimmerParticle;
		this.thornsParticle = thornsParticle;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		boolean waterlogged = state.get(WATERLOGGED, false);
		Direction direction = state.get(FACING, Direction.UP);

		int glimmerCount = waterlogged ? 1 + random.nextInt(3) : random.nextInt(2);
		for(int i = 0; i < glimmerCount; i++) {
			spawnParticles(world, pos, direction, waterlogged, random, this.glimmerParticle, 0.4F, 1F, 1F);
		}

		int thornsCount = waterlogged ? 2 + random.nextInt(2) : 1;
		for(int i = 0; i < thornsCount; i++) {
			spawnParticles(world, pos, direction, waterlogged, random, this.thornsParticle, 0.9F, 2.6F, 1.4F);
		}
	}

	protected void spawnParticles(World world, BlockPos pos, Direction direction, boolean waterlogged, Random random, ParticleEffect particle, float width, float orthogonalVelocityMultiplier, float normalVelocityMultiplier) {
		double x = pos.getX() + 0.5 + width * (random.nextFloat() - 0.5);
		double y = pos.getY() + 0.5 + width * (random.nextFloat() - 0.5);
		double z = pos.getZ() + 0.5 + width * (random.nextFloat() - 0.5);

		double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008) * orthogonalVelocityMultiplier;
		double vy = random.nextGaussian() * (waterlogged ? 0.015 : 0.008) * orthogonalVelocityMultiplier;
		double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008) * orthogonalVelocityMultiplier;

		Vec3i vector = direction.getVector();
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

		world.addParticleClient(particle, x, y, z, vx, vy, vz);
	}
}
