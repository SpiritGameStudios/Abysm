package dev.spiritstudios.abysm.worldgen.structure.ruins;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.mixin.worldgen.SinglePoolElementAccessor;
import dev.spiritstudios.abysm.mixin.worldgen.StructureAccessor;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureTypes;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructures;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.ListPoolElement;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeepSeaRuinsStructure extends Structure {
	public static final MapCodec<DeepSeaRuinsStructure> CODEC = createCodec(DeepSeaRuinsStructure::new);

	public DeepSeaRuinsStructure(Config config) {
		super(config);
	}

	@Override
	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
		context.random().nextDouble();

		ChunkPos chunkPos = context.chunkPos();
		BlockPos blockPos = new BlockPos(chunkPos.getCenterX(), 18, chunkPos.getCenterZ());
		Random random = context.random();

		BlockBox maxBoundingBox = getMaxBoundingBox(chunkPos, context.world());

		// generate base pieces
		StructurePiecesCollector structurePiecesCollector = new StructurePiecesCollector();
		int offset = this.addPieces(structurePiecesCollector, context);

		// add sub-structure pieces
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				addStructure(
					contextWithChunkPos(context, new ChunkPos(context.chunkPos().x + i * 3, context.chunkPos().z + j * 3)),
					AbysmStructures.DEEP_SEA_RUINS_BASIC_RUIN,
					structurePiecesCollector,
					maxBoundingBox
				);
			}
		}

		// collect density blob infos and convert into structure pieces
		List<StructureTemplate.StructureBlockInfo> blobInfos = getBlobInfos(structurePiecesCollector.toList().pieces(), context.structureTemplateManager());
		for (StructureTemplate.StructureBlockInfo info : blobInfos) {
			structurePiecesCollector.addPiece(makePieceFromBlobInfo(info, random, info.pos()));
		}

		// remove any cutoff structure pieces entirely
		// this may or may not be an improvement
		// this shouldn't be needed since structures get shifted in bounds anyways
		//structurePiecesCollector = trimCutoffPieces(structurePiecesCollector, maxBoundingBox);

		return Optional.of(new Structure.StructurePosition(
			blockPos.add(0, offset, 0),
			Either.right(structurePiecesCollector)
		));
	}

	protected BlockBox getMaxBoundingBox(ChunkPos chunkPos, HeightLimitView world) {
		// structure needs to be at max 17x17 chunks otherwise it gets cut off
		ChunkPos minChunkPos = new ChunkPos(chunkPos.x - 8, chunkPos.z - 8);
		ChunkPos maxChunkPos = new ChunkPos(chunkPos.x + 8, chunkPos.z + 8);

		// shrink box a bit to avoid beardify getting cutoff (not actually needed for deep sea ruins, but do it anyways in case this is needed later)
		int beardifyRange = 12;

		return new BlockBox(
			minChunkPos.getStartX() + beardifyRange,
			world.getBottomY(),
			minChunkPos.getStartZ() + beardifyRange,
			maxChunkPos.getEndX() - beardifyRange,
			world.getTopYInclusive() - beardifyRange,
			maxChunkPos.getEndZ() - beardifyRange
		);
	}

	protected Context contextWithChunkPos(Context context, ChunkPos chunkPos) {
		return new Context(
			context.dynamicRegistryManager(),
			context.chunkGenerator(),
			context.biomeSource(),
			context.noiseConfig(),
			context.structureTemplateManager(),
			context.random(),
			context.seed(),
			chunkPos,
			context.world(),
			context.biomePredicate()
		);
	}

	protected StructurePiecesCollector trimCutoffPieces(StructurePiecesCollector collector, BlockBox maxBoundingBox) {
		// remove any structure pieces that would be partially or fully cutoff
		StructurePiecesCollector spc = new StructurePiecesCollector();
		for (StructurePiece piece : collector.toList().pieces()) {
			BlockBox box = piece.getBoundingBox();
			if (boxContainedIn(box, maxBoundingBox)) {
				spc.addPiece(piece);
			}
		}
		return spc;
	}

	public static boolean boxContainedIn(BlockBox small, BlockBox big) {
		if (small.getMinY() < big.getMinY()) return false;
		if (small.getMaxY() > big.getMaxY()) return false;

		if (small.getMinX() < big.getMinX()) return false;
		if (small.getMaxX() > big.getMaxX()) return false;

		if (small.getMinZ() < big.getMinZ()) return false;
		if (small.getMaxZ() > big.getMaxZ()) return false;

		return true;
	}

	protected boolean addStructure(Structure.Context context, RegistryKey<Structure> structureKey, StructurePiecesCollector collector, BlockBox maxBoundingBox) {
		Structure structure = context.dynamicRegistryManager().getOrThrow(RegistryKeys.STRUCTURE).get(structureKey);

		if (structure instanceof StructureAccessor structureAccessor) {
			Optional<StructurePosition> structurePositionOptional = structureAccessor.invokeGetStructurePosition(context);
			if (structurePositionOptional.isPresent()) {
				StructurePosition structurePosition = structurePositionOptional.get();

				// get pieces
				StructurePiecesList structurePiecesList = structurePosition.generate().toList();

				// shift structure around
				//int dy = context.random().nextBetween(-10, 10);
				//structurePiecesList.pieces().forEach(piece -> piece.translate(0, dy, 0));

				// shift structure inside bounds if out of bounds, and if valid add to main structure
				Optional<Vec3i> shiftOptional = getRequiredShift(structurePiecesList.getBoundingBox(), maxBoundingBox);
				if (shiftOptional.isPresent()) {
					Vec3i shift = shiftOptional.get();
					structurePiecesList.pieces().forEach(piece -> piece.translate(shift.getX(), shift.getY(), shift.getZ()));

					structurePiecesList.pieces().forEach(collector::addPiece);
					return true;
				}
			}
		}

		return false;
	}

	protected Optional<Vec3i> getRequiredShift(BlockBox small, BlockBox big) {
		Optional<Integer> xShift = getRequiredShift(small.getMinX(), small.getMaxX(), big.getMinX(), big.getMaxX());
		if (xShift.isEmpty()) {
			return Optional.empty();
		}

		Optional<Integer> yShift = getRequiredShift(small.getMinY(), small.getMaxY(), big.getMinY(), big.getMaxY());
		if (yShift.isEmpty()) {
			return Optional.empty();
		}

		Optional<Integer> zShift = getRequiredShift(small.getMinZ(), small.getMaxZ(), big.getMinZ(), big.getMaxZ());
		if (zShift.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new Vec3i(xShift.get(), yShift.get(), zShift.get()));
	}

	protected Optional<Integer> getRequiredShift(int smallMin, int smallMax, int bigMin, int bigMax) {
		// find border sizes on either side
		int minBorder = smallMin - bigMin;
		int maxBorder = bigMax - smallMax;

		if (minBorder < 0 && maxBorder < 0) {
			// not possible to place in bounds
			return Optional.empty();
		}

		int shift = 0;
		if (minBorder < 0) {
			shift = -minBorder;
		} else if (maxBorder < 0) {
			shift = maxBorder;
		}

		return Optional.of(shift);
	}

	@SuppressWarnings("deprecation")
	protected int addPieces(StructurePiecesCollector collector, Structure.Context context) {
		ChunkPos chunkPos = context.chunkPos();
		ChunkRandom random = context.random();

		for (int i = 0; i < 85; i++) {
			int rad = random.nextBetween(10, 40);
			int spread = 120 - rad;
			DeepSeaRuinsGenerator.SphereCave room = new DeepSeaRuinsGenerator.SphereCave(
				0,
				chunkPos.getOffsetX(random.nextBetween(7 - spread, 8 + spread)),
				random.nextBetween(-14, 36),
				chunkPos.getOffsetZ(random.nextBetween(7 - spread, 8 + spread)),
				rad,
				rad + random.nextBetween(4, 6)
			);
			collector.addPiece(room);
		}

		ChunkGenerator chunkGenerator = context.chunkGenerator();
		int seaLevel = chunkGenerator.getSeaLevel();
		return collector.shiftInto(seaLevel, chunkGenerator.getMinimumY(), random, 10);
	}

	protected List<StructureTemplate.StructureBlockInfo> getBlobInfos(List<StructurePiece> structurePieces, StructureTemplateManager structureTemplateManager) {
		List<StructureTemplate.StructureBlockInfo> blobInfos = new ArrayList<>();
		for (StructurePiece piece : structurePieces) {
			if (piece instanceof PoolStructurePiece poolStructurePiece) {
				StructurePoolElement structurePoolElement = poolStructurePiece.getPoolElement();
				BlockPos pos = poolStructurePiece.getPos();
				StructurePlacementData structurePlacementData = new StructurePlacementData().setRotation(poolStructurePiece.getRotation());

				blobInfos.addAll(getBlobInfos(structurePoolElement, pos, structurePlacementData, structureTemplateManager));
			}
		}
		return blobInfos;
	}

	protected List<StructureTemplate.StructureBlockInfo> getBlobInfos(StructurePoolElement structurePoolElement, BlockPos pos, StructurePlacementData structurePlacementData, StructureTemplateManager structureTemplateManager) {
		if (structurePoolElement instanceof SinglePoolElement singlePoolElement) {
			StructureTemplate structureTemplate = ((SinglePoolElementAccessor) singlePoolElement).invokeGetStructure(structureTemplateManager);
			return structureTemplate.getInfosForBlock(
				pos, structurePlacementData, AbysmBlocks.DENSITY_BLOB_BLOCK, true
			);
		} else if (structurePoolElement instanceof ListPoolElement listPoolElement) {
			List<StructurePoolElement> elements = listPoolElement.getElements();
			if (elements.isEmpty()) {
				return List.of();
			} else {
				StructurePoolElement firstChild = elements.getFirst();
				return getBlobInfos(firstChild, pos, structurePlacementData, structureTemplateManager);
			}
		} else {
			return List.of();
		}
	}

	protected StructurePiece makePieceFromBlobInfo(StructureTemplate.StructureBlockInfo info, Random random, BlockPos pos) {
		int rad = random.nextBetween(18, 30);
		return new DeepSeaRuinsGenerator.SphereCave(
			0,
			pos.getX(),
			pos.getY(),
			pos.getZ(),
			rad,
			rad + random.nextBetween(4, 6)
		);
	}

	@Override
	public StructureType<?> getType() {
		return AbysmStructureTypes.DEEP_SEA_RUINS;
	}
}
