package dev.spiritstudios.abysm.world.level.levelgen.structure;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityBlob;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityBlobHolder;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityVoid;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.TranslatedDensityBlob;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.phys.AABB;

import java.util.Objects;
import java.util.Optional;

public class DensityBlobStructurePiece extends StructurePiece implements DensityBlobHolder {
	public static final String KEY_BLOBS_SAMPLER_IDENTIFIER = "bsid";
	public static final String KEY_DENSITY_BLOB = "db";

	private final Identifier identifier;
	private final DensityBlob densityBlob;

	public DensityBlobStructurePiece(Identifier identifier, int x, int y, int z, DensityBlob densityBlob) {
		super(
			AbysmStructurePieceTypes.DENSITY_BLOB,
			0,
			toBlockBox(densityBlob.getBoundingBox().move(x, y, z))
		);
		this.identifier = identifier;
		this.densityBlob = densityBlob;
	}

	public DensityBlobStructurePiece(CompoundTag nbt) {
		super(
			AbysmStructurePieceTypes.DENSITY_BLOB,
			nbt
		);
		this.identifier = idFromString(nbt.getStringOr(KEY_BLOBS_SAMPLER_IDENTIFIER, ""));
		Optional<DensityBlob> blob = nbt.read(KEY_DENSITY_BLOB, DensityBlob.CODEC);
		if (blob.isPresent()) {
			this.densityBlob = blob.get();
		} else {
			Abysm.LOGGER.error("Failed to load density blob for density blob structure piece!");
			this.densityBlob = DensityVoid.INSTANCE;
		}
	}

	public static Identifier idFromString(String string) {
		Identifier idNullable = Identifier.tryParse(string);
		return Objects.requireNonNullElse(idNullable, Abysm.id("empty"));
	}

	public static BoundingBox toBlockBox(AABB box) {
		return new BoundingBox(
			Mth.floor(box.minX),
			Mth.floor(box.minY),
			Mth.floor(box.minZ),
			Mth.ceil(box.maxX) - 1,
			Mth.ceil(box.maxY) - 1,
			Mth.ceil(box.maxZ) - 1
		);
	}

	public static BoundingBox makeBoundingBox(int x, int y, int z, double radius) {
		int r = Mth.ceil(radius);
		return new BoundingBox(
			x - r,
			y - r,
			z - r,
			x + r,
			y + r,
			z + r
		);
	}

	@Override
	public DensityBlob getDensityBlob() {
		// stored blob exists relative to the structure piece, so must be translated by the structure piece's position
		BoundingBox box = this.boundingBox;
		int x = (box.minX() + box.maxX()) / 2;
		int y = (box.minY() + box.maxY()) / 2;
		int z = (box.minZ() + box.maxZ()) / 2;
		return new TranslatedDensityBlob(this.densityBlob, x, y, z);
	}

	@Override
	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
		nbt.putString(KEY_BLOBS_SAMPLER_IDENTIFIER, this.identifier.toString());
		nbt.store(KEY_DENSITY_BLOB, DensityBlob.CODEC, this.densityBlob);
	}

	@Override
	public void postProcess(
		WorldGenLevel level,
		StructureManager structureManager,
		ChunkGenerator chunkGenerator,
		RandomSource random,
		BoundingBox chunkBox,
		ChunkPos chunkPos,
		BlockPos pivot
	) {
		// NO-OP
	}

	@Override
	public void move(int x, int y, int z) {
		super.move(x, y, z);
	}
}
