package dev.spiritstudios.abysm.structure;

import dev.spiritstudios.abysm.worldgen.sdf.SDFSphere;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class DeepSeaRuinsGenerator {

	public static class SphereCave extends StructurePiece {

		private final SDFSphere sphereObject;
		private final double radius;
		private final double outerRadius;

		public SphereCave(int chainLength, int x, int y, int z, double radius, double outerRadius) {
			super(
				AbysmStructurePieceTypes.DEEP_SEA_RUINS_HOLLOW,
				chainLength,
				makeBoundingBox(x, y, z, outerRadius)
			);
			this.radius = radius;
			this.outerRadius = outerRadius;
			this.sphereObject = new SDFSphere(x, y, z, radius, outerRadius);
		}

		public SphereCave(NbtCompound nbt) {
			super(AbysmStructurePieceTypes.DEEP_SEA_RUINS_HOLLOW, nbt);
			this.radius = nbt.getDouble("rad", 0);
			this.outerRadius = nbt.getDouble("out_rad", 0);
			BlockBox box = this.boundingBox;
			this.sphereObject = new SDFSphere(
				(box.getMinX() + box.getMaxX()) / 2.0,
				(box.getMinY() + box.getMaxY()) / 2.0,
				(box.getMinZ() + box.getMaxZ()) / 2.0,
				this.radius,
				this.outerRadius
			);
		}

		public static BlockBox makeBoundingBox(int x, int y, int z, double radius) {
			int r = MathHelper.ceil(radius);
			return new BlockBox(
				x - MathHelper.ceil(r),
				y - MathHelper.ceil(r),
				z - MathHelper.ceil(r),
				x + MathHelper.ceil(r),
				y + MathHelper.ceil(r),
				z + MathHelper.ceil(r)
			);
		}

		public SDFSphere getSphereObject() {
			return this.sphereObject;
		}

		@Override
		protected void writeNbt(StructureContext context, NbtCompound nbt) {
			nbt.putDouble("rad", this.radius);
			nbt.putDouble("out_rad", this.outerRadius);
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
			this.sphereObject.translate(x, y, z);
		}
	}
}
