package dev.spiritstudios.abysm.worldgen.structure;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlob;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlobHolder;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityVoid;
import dev.spiritstudios.abysm.worldgen.densityfunction.TranslatedDensityBlob;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

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
			toBlockBox(densityBlob.getBoundingBox().offset(x, y, z))
		);
		this.identifier = identifier;
		this.densityBlob = densityBlob;
	}

	public DensityBlobStructurePiece(NbtCompound nbt) {
		super(
			AbysmStructurePieceTypes.DENSITY_BLOB,
			nbt
		);
		this.identifier = idFromString(nbt.getString(KEY_BLOBS_SAMPLER_IDENTIFIER, ""));
		Optional<DensityBlob> blob = nbt.get(KEY_DENSITY_BLOB, DensityBlob.CODEC);
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

	public static BlockBox toBlockBox(Box box) {
		return new BlockBox(
			MathHelper.floor(box.minX),
			MathHelper.floor(box.minY),
			MathHelper.floor(box.minZ),
			MathHelper.ceil(box.maxX) - 1,
			MathHelper.ceil(box.maxY) - 1,
			MathHelper.ceil(box.maxZ) - 1
		);
	}

	public static BlockBox makeBoundingBox(int x, int y, int z, double radius) {
		int r = MathHelper.ceil(radius);
		return new BlockBox(
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
		BlockBox box = this.boundingBox;
		int x = (box.getMinX() + box.getMaxX()) / 2;
		int y = (box.getMinY() + box.getMaxY()) / 2;
		int z = (box.getMinZ() + box.getMaxZ()) / 2;
		return new TranslatedDensityBlob(this.densityBlob, x, y, z);
	}

	@Override
	public Identifier getIdentifier() {
		return this.identifier;
	}

	@Override
	protected void writeNbt(StructureContext context, NbtCompound nbt) {
		nbt.putString(KEY_BLOBS_SAMPLER_IDENTIFIER, this.identifier.toString());
		nbt.put(KEY_DENSITY_BLOB, DensityBlob.CODEC, this.densityBlob);
	}

	@Override
	public void generate(
		StructureWorldAccess world,
		StructureAccessor structureAccessor,
		ChunkGenerator chunkGenerator,
		Random random,
		BlockBox chunkBox,
		ChunkPos chunkPos,
		BlockPos pivot
	) {
		// NO-OP
	}

	@Override
	public void translate(int x, int y, int z) {
		super.translate(x, y, z);
	}
}
