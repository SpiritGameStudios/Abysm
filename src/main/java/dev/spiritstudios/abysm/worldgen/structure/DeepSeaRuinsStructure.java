package dev.spiritstudios.abysm.worldgen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmStructureTypes;
import dev.spiritstudios.abysm.structure.DeepSeaRuinsGenerator;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class DeepSeaRuinsStructure extends Structure {
	public static final MapCodec<DeepSeaRuinsStructure> CODEC = createCodec(DeepSeaRuinsStructure::new);

	protected DeepSeaRuinsStructure(Config config) {
		super(config);
	}

	@Override
	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
		context.random().nextDouble();

		ChunkPos chunkPos = context.chunkPos();
		BlockPos blockPos = new BlockPos(chunkPos.getCenterX(), 18, chunkPos.getCenterZ());

		StructurePiecesCollector structurePiecesCollector = new StructurePiecesCollector();
		int offset = this.addPieces(structurePiecesCollector, context);

		return Optional.of(new Structure.StructurePosition(blockPos.add(0, offset, 0), Either.right(structurePiecesCollector)));
	}

	private int addPieces(StructurePiecesCollector collector, Structure.Context context) {
		ChunkPos chunkPos = context.chunkPos();
		ChunkRandom random = context.random();

		for(int i = 0; i < 8; i++) {
			int spread = 30;
			DeepSeaRuinsGenerator.SphereCave room = new DeepSeaRuinsGenerator.SphereCave(
				0,
				chunkPos.getOffsetX(random.nextBetween(7 - spread, 8 + spread)),
				random.nextBetween(4, 32),
				chunkPos.getOffsetZ(random.nextBetween(7 - spread, 8 + spread)),
				35,
				40
			);
			collector.addPiece(room);
		}

		ChunkGenerator chunkGenerator = context.chunkGenerator();
		int seaLevel = chunkGenerator.getSeaLevel();
		return collector.shiftInto(seaLevel, chunkGenerator.getMinimumY(), random, 10);
	}

	@Override
	public StructureType<?> getType() {
		return AbysmStructureTypes.DEEP_SEA_RUINS;
	}
}
