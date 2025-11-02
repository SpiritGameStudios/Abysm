package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;

public class BloomPetaleavesBlock extends LeavesBlock {
	public static final MapCodec<BloomPetaleavesBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			ParticleTypes.CODEC.fieldOf("particle").forGetter(block -> block.particle),
			ExtraCodecs.floatRange(0.0F, 1.0F)
				.fieldOf("leaf_particle_chance")
				.forGetter(tintedParticleLeavesBlock -> tintedParticleLeavesBlock.leafParticleChance),
			propertiesCodec()
		).apply(instance, BloomPetaleavesBlock::new)
	);

	public final ParticleOptions particle;

	@Override
	public MapCodec<? extends LeavesBlock> codec() {
		return CODEC;
	}

	public BloomPetaleavesBlock(ParticleOptions particle, float leafParticleChance, Properties settings) {
		super(leafParticleChance, settings);
		this.particle = particle;
	}

	@Override
	protected void spawnFallingLeavesParticle(Level world, BlockPos pos, RandomSource random) {
		double x = pos.getX() + 0.05 + 0.9 * random.nextFloat();
		double y = pos.getY() - 0.1 + 0.1 * random.nextFloat();
		double z = pos.getZ() + 0.05 + 0.9 * random.nextFloat();

		world.addParticle(this.particle, x, y, z, 0, 0, 0);
	}
}
