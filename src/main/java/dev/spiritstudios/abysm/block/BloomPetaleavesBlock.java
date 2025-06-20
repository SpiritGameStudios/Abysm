package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.util.AbysmCodecs;
import net.minecraft.block.LeavesBlock;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BloomPetaleavesBlock extends LeavesBlock {
	public static final MapCodec<BloomPetaleavesBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			AbysmCodecs.SIMPLE_PARTICLE_TYPE.fieldOf("particle").forGetter(block -> block.particle),
			Codecs.rangedInclusiveFloat(0.0F, 1.0F)
				.fieldOf("leaf_particle_chance")
				.forGetter(tintedParticleLeavesBlock -> tintedParticleLeavesBlock.leafParticleChance),
			createSettingsCodec()
		).apply(instance, BloomPetaleavesBlock::new)
	);

	public final SimpleParticleType particle;

	@Override
	public MapCodec<? extends LeavesBlock> getCodec() {
		return CODEC;
	}

	public BloomPetaleavesBlock(SimpleParticleType particle, float leafParticleChance, Settings settings) {
		super(leafParticleChance, settings);
		this.particle = particle;
	}

	@Override
	protected void spawnLeafParticle(World world, BlockPos pos, Random random) {
		double x = pos.getX() + 0.05 + 0.9 * random.nextFloat();
		double y = pos.getY() - 0.1 + 0.1 * random.nextFloat();
		double z = pos.getZ() + 0.05 + 0.9 * random.nextFloat();

		world.addParticleClient(this.particle, x, y, z, 0, 0, 0);
	}
}
