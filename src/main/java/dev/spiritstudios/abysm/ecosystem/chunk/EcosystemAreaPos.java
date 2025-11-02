package dev.spiritstudios.abysm.ecosystem.chunk;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;

public record EcosystemAreaPos(int x, int z) {
	public static final int CHUNK_DISTANCE = 3;

	public EcosystemAreaPos(ChunkPos chunkPos) {
		this(
			Mth.floor((float) chunkPos.x / CHUNK_DISTANCE), // is using floorDiv better here? - Maybe?
			Mth.floor((float) chunkPos.z / CHUNK_DISTANCE)
		);
	}

	public ChunkPos getCenterChunkPos() {
		int centerAmount = 1;
		int chunkX = this.x * CHUNK_DISTANCE + centerAmount;
		int chunkZ = this.z * CHUNK_DISTANCE + centerAmount;
		return new ChunkPos(chunkX, chunkZ);
	}

	public static Stream<EcosystemAreaPos> stream(EcosystemAreaPos center, int radius) {
		return stream(new EcosystemAreaPos(center.x - radius, center.z - radius), new EcosystemAreaPos(center.x + radius, center.z + radius));
	}

	/**
	 * @see ChunkPos#rangeClosed(ChunkPos, ChunkPos)
	 */
	public static Stream<EcosystemAreaPos> stream(EcosystemAreaPos pos1, EcosystemAreaPos pos2) {
		int xDiff = Math.abs(pos1.x - pos2.x) + 1;
		int zDiff = Math.abs(pos1.z - pos2.z) + 1;
		final int directionX = pos1.x < pos2.x ? 1 : -1;
		final int directionZ = pos1.z < pos2.z ? 1 : -1;
		return StreamSupport.stream(new Spliterators.AbstractSpliterator<>((long) xDiff * zDiff, Spliterator.SIZED) {
			@Nullable
			private EcosystemAreaPos position;

			@Override
			public boolean tryAdvance(Consumer<? super EcosystemAreaPos> consumer) {
				if (this.position == null) {
					this.position = pos1;
				} else {
					int currentX = this.position.x;
					int currentZ = this.position.z;
					if(currentX == pos2.x) {
						if(currentZ == pos2.z) return false;
						this.position = new EcosystemAreaPos(pos1.x, currentZ + directionZ);
					} else {
						this.position = new EcosystemAreaPos(currentX + directionX, currentZ);
					}
				}

				consumer.accept(this.position);
				return true;
			}
		}, false);
	}

	@Override
	public int hashCode() {
		return ChunkPos.hash(this.x, this.z);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		// There's probably a reason why Mojang does it this way with ChunkPos#equals(), right?
		// (Instead of doing `obj instanceof EcosystemAreaPos && this.x == areaPos.x && this.z == areaPos.z;`)
		return obj instanceof EcosystemAreaPos(int x1, int z1) && this.x == x1 && this.z == z1;
	}

	@Override
	public @NotNull String toString() {
		return MoreObjects.toStringHelper(this).add("x", this.x).add("z", this.z).toString();
	}
}
