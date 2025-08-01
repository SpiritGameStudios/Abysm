package dev.spiritstudios.abysm.worldgen.structure.ruins;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlob;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlobHolder;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensitySphere;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructurePieceTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Objects;

public class DeepSeaRuinsGenerator {
	public static final String IDENTIFIER = "identifier";
	public static final String RADIUS = "radius";
	public static final String OUTER_RADIUS = "outer_radius";

	public static class DensitySpherePiece extends StructurePiece implements DensityBlobHolder {
		private final Identifier identifier;
		private final double radius;
		private final double outerRadius;
		private final DensitySphere densitySphere;

		public DensitySpherePiece(String identifier, int chainLength, int x, int y, int z, double radius, double outerRadius) {
			this(
				idFromString(identifier),
				chainLength,
				x,
				y,
				z,
				radius,
				outerRadius
			);
		}

		public DensitySpherePiece(Identifier identifier, int chainLength, int x, int y, int z, double radius, double outerRadius) {
			super(
				AbysmStructurePieceTypes.DEEP_SEA_RUINS_HOLLOW,
				chainLength,
				makeBoundingBox(x, y, z, outerRadius)
			);
			this.identifier = identifier;
			this.radius = radius;
			this.outerRadius = outerRadius;
			this.densitySphere = new DensitySphere(x, y, z, radius, outerRadius);
		}

		public DensitySpherePiece(NbtCompound nbt) {
			super(AbysmStructurePieceTypes.DEEP_SEA_RUINS_HOLLOW, nbt);

			this.identifier = idFromString(nbt.getString(IDENTIFIER, ""));

			this.radius = nbt.getDouble(RADIUS, 0);
			this.outerRadius = nbt.getDouble(OUTER_RADIUS, 0);

			BlockBox box = this.boundingBox;

			this.densitySphere = new DensitySphere(
				(box.getMinX() + box.getMaxX()) / 2.0,
				(box.getMinY() + box.getMaxY()) / 2.0,
				(box.getMinZ() + box.getMaxZ()) / 2.0,
				this.radius,
				this.outerRadius
			);
		}

		public static Identifier idFromString(String string) {
			Identifier idNullable = Identifier.tryParse(string);
			return Objects.requireNonNullElse(idNullable, Abysm.id("empty"));
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
			return this.densitySphere;
		}

		@Override
		public Identifier getIdentifier() {
			return this.identifier;
		}

		@Override
		protected void writeNbt(StructureContext context, NbtCompound nbt) {
			nbt.putString(IDENTIFIER, this.identifier.toString());
			nbt.putDouble(RADIUS, this.radius);
			nbt.putDouble(OUTER_RADIUS, this.outerRadius);
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
			this.densitySphere.translate(x, y, z);
		}
	}
}
