package dev.spiritstudios.abysm.worldgen.structure.ruins;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.mixin.worldgen.SinglePoolElementAccessor;
import dev.spiritstudios.abysm.mixin.worldgen.StructureAccessor;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlob;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensitySphere;
import dev.spiritstudios.abysm.worldgen.densityfunction.LayeredDensitySphere;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureTypes;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructures;
import dev.spiritstudios.abysm.worldgen.structure.DensityBlobStructurePiece;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.ListPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class DeepSeaRuinsStructure extends Structure {
	public static final MapCodec<DeepSeaRuinsStructure> CODEC = simpleCodec(DeepSeaRuinsStructure::new);

	public DeepSeaRuinsStructure(StructureSettings config) {
		super(config);
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		context.random().nextDouble();

		ChunkPos chunkPos = context.chunkPos();
		BlockPos blockPos = new BlockPos(chunkPos.getMiddleBlockX(), 18, chunkPos.getMiddleBlockZ());
		RandomSource random = context.random();

		BoundingBox maxBoundingBox = getMaxBoundingBox(chunkPos, context.heightAccessor());

		// generate base pieces
		StructurePiecesBuilder structurePiecesCollector = new StructurePiecesBuilder();
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
		List<StructureTemplate.StructureBlockInfo> blobInfos = getBlobInfos(structurePiecesCollector.build().pieces(), context.structureTemplateManager());
		for (StructureTemplate.StructureBlockInfo info : blobInfos) {
			StructurePiece piece = makePieceFromBlobInfo(info, random, info.pos());
			if (piece != null) {
				structurePiecesCollector.addPiece(piece);
			}
		}

		// remove any cutoff structure pieces entirely
		// this may or may not be an improvement
		// this shouldn't be needed since structures get shifted in bounds anyways
		//structurePiecesCollector = trimCutoffPieces(structurePiecesCollector, maxBoundingBox);

		return Optional.of(new Structure.GenerationStub(
			blockPos.offset(0, offset, 0),
			Either.right(structurePiecesCollector)
		));
	}

	protected BoundingBox getMaxBoundingBox(ChunkPos chunkPos, LevelHeightAccessor world) {
		// structure needs to be at max 17x17 chunks otherwise it gets cut off
		ChunkPos minChunkPos = new ChunkPos(chunkPos.x - 8, chunkPos.z - 8);
		ChunkPos maxChunkPos = new ChunkPos(chunkPos.x + 8, chunkPos.z + 8);

		// shrink box a bit to avoid beardify getting cutoff (not actually needed for deep sea ruins, but do it anyways in case this is needed later)
		int beardifyRange = 12;

		return new BoundingBox(
			minChunkPos.getMinBlockX() + beardifyRange,
			world.getMinY(),
			minChunkPos.getMinBlockZ() + beardifyRange,
			maxChunkPos.getMaxBlockX() - beardifyRange,
			world.getMaxY() - beardifyRange,
			maxChunkPos.getMaxBlockZ() - beardifyRange
		);
	}

	protected GenerationContext contextWithChunkPos(GenerationContext context, ChunkPos chunkPos) {
		return new GenerationContext(
			context.registryAccess(),
			context.chunkGenerator(),
			context.biomeSource(),
			context.randomState(),
			context.structureTemplateManager(),
			context.random(),
			context.seed(),
			chunkPos,
			context.heightAccessor(),
			context.validBiome()
		);
	}

	protected StructurePiecesBuilder trimCutoffPieces(StructurePiecesBuilder collector, BoundingBox maxBoundingBox) {
		// remove any structure pieces that would be partially or fully cutoff
		StructurePiecesBuilder spc = new StructurePiecesBuilder();
		for (StructurePiece piece : collector.build().pieces()) {
			BoundingBox box = piece.getBoundingBox();
			if (boxContainedIn(box, maxBoundingBox)) {
				spc.addPiece(piece);
			}
		}
		return spc;
	}

	public static boolean boxContainedIn(BoundingBox small, BoundingBox big) {
		if (small.minY() < big.minY()) return false;
		if (small.maxY() > big.maxY()) return false;

		if (small.minX() < big.minX()) return false;
		if (small.maxX() > big.maxX()) return false;

		if (small.minZ() < big.minZ()) return false;
		if (small.maxZ() > big.maxZ()) return false;

		return true;
	}

	protected boolean addStructure(Structure.GenerationContext context, ResourceKey<Structure> structureKey, StructurePiecesBuilder collector, BoundingBox maxBoundingBox) {
		Structure structure = context.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(structureKey);

		if (structure instanceof StructureAccessor structureAccessor) {
			Optional<GenerationStub> structurePositionOptional = structureAccessor.invokeFindGenerationPoint(context);
			if (structurePositionOptional.isPresent()) {
				GenerationStub structurePosition = structurePositionOptional.get();

				// get pieces
				PiecesContainer structurePiecesList = structurePosition.getPiecesBuilder().build();

				// shift structure around
				//int dy = context.random().nextBetween(-10, 10);
				//structurePiecesList.pieces().forEach(piece -> piece.translate(0, dy, 0));

				// shift structure inside bounds if out of bounds, and if valid add to main structure
				Optional<Vec3i> shiftOptional = getRequiredShift(structurePiecesList.calculateBoundingBox(), maxBoundingBox);
				if (shiftOptional.isPresent()) {
					Vec3i shift = shiftOptional.get();
					structurePiecesList.pieces().forEach(piece -> piece.move(shift.getX(), shift.getY(), shift.getZ()));

					structurePiecesList.pieces().forEach(collector::addPiece);
					return true;
				}
			}
		}

		return false;
	}

	protected Optional<Vec3i> getRequiredShift(BoundingBox small, BoundingBox big) {
		Optional<Integer> xShift = getRequiredShift(small.minX(), small.maxX(), big.minX(), big.maxX());
		if (xShift.isEmpty()) {
			return Optional.empty();
		}

		Optional<Integer> yShift = getRequiredShift(small.minY(), small.maxY(), big.minY(), big.maxY());
		if (yShift.isEmpty()) {
			return Optional.empty();
		}

		Optional<Integer> zShift = getRequiredShift(small.minZ(), small.maxZ(), big.minZ(), big.maxZ());
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
	protected int addPieces(StructurePiecesBuilder collector, Structure.GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		WorldgenRandom random = context.random();

		for (int i = 0; i < 85; i++) {
			int radius = random.nextIntBetweenInclusive(10, 40);
			int outerRadius = radius + random.nextIntBetweenInclusive(4, 6);
			int spread = 120 - radius;
			DensityBlob densityBlob = new LayeredDensitySphere(radius, outerRadius);
			DensityBlobStructurePiece room = new DensityBlobStructurePiece(
				Abysm.id("ruins_shell"),
				chunkPos.getBlockX(random.nextIntBetweenInclusive(7 - spread, 8 + spread)),
				random.nextIntBetweenInclusive(-14, 36),
				chunkPos.getBlockZ(random.nextIntBetweenInclusive(7 - spread, 8 + spread)),
				densityBlob
			);
			collector.addPiece(room);
		}

		ChunkGenerator chunkGenerator = context.chunkGenerator();
		int seaLevel = chunkGenerator.getSeaLevel();
		return collector.moveBelowSeaLevel(seaLevel, chunkGenerator.getMinY(), random, 10);
	}

	protected List<StructureTemplate.StructureBlockInfo> getBlobInfos(List<StructurePiece> structurePieces, StructureTemplateManager structureTemplateManager) {
		List<StructureTemplate.StructureBlockInfo> blobInfos = new ArrayList<>();
		for (StructurePiece piece : structurePieces) {
			if (piece instanceof PoolElementStructurePiece poolStructurePiece) {
				StructurePoolElement structurePoolElement = poolStructurePiece.getElement();
				BlockPos pos = poolStructurePiece.getPosition();
				StructurePlaceSettings structurePlacementData = new StructurePlaceSettings().setRotation(poolStructurePiece.getRotation());

				blobInfos.addAll(getBlobInfos(structurePoolElement, pos, structurePlacementData, structureTemplateManager));
			}
		}
		return blobInfos;
	}

	protected List<StructureTemplate.StructureBlockInfo> getBlobInfos(StructurePoolElement structurePoolElement, BlockPos pos, StructurePlaceSettings structurePlacementData, StructureTemplateManager structureTemplateManager) {
		if (structurePoolElement instanceof SinglePoolElement singlePoolElement) {
			StructureTemplate structureTemplate = ((SinglePoolElementAccessor) singlePoolElement).invokeGetTemplate(structureTemplateManager);
			return structureTemplate.filterBlocks(
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

	@Nullable
	protected StructurePiece makePieceFromBlobInfo(StructureTemplate.StructureBlockInfo info, RandomSource random, BlockPos pos) {
		CompoundTag nbt = info.nbt();
		if (nbt == null) {
			return null;
		} else {
			String id = nbt.getStringOr(DensityBlobBlockEntity.BLOBS_SAMPLER_IDENTIFIER, "");

			// TODO better size/shape control
			boolean shell = id.equals("abysm:ruins_shell");
			int radius = shell ? random.nextIntBetweenInclusive(18, 30) : random.nextIntBetweenInclusive(1, 2);
			int outerRadius = radius + (shell ? random.nextIntBetweenInclusive(4, 6) : random.nextIntBetweenInclusive(2, 3));
			DensityBlob densityBlob = shell ? new LayeredDensitySphere(radius, outerRadius) : new DensitySphere(radius, outerRadius, 1.0 + radius);

			return new DensityBlobStructurePiece(
				DensityBlobStructurePiece.idFromString(id),
				pos.getX(),
				pos.getY(),
				pos.getZ(),
				densityBlob
			);
		}
	}

	@Override
	public StructureType<?> type() {
		return AbysmStructureTypes.DEEP_SEA_RUINS;
	}
}
