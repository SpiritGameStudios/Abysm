package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

// Handles most of the EcosystemChunk communication from the EcosystemLogic - entering, leaving, spawning, and dying
// And then there's Chunky... ... ... ... ... ... ... ... ... he's doing fine actually just enjoying some coffee
public class EcologicalEntityChunkTracker {
	public final EcosystemLogic logic;
	public final MobEntity entity;

	public ChunkPos currentPos = ChunkPos.ORIGIN;
	public ChunkPos prevPos = ChunkPos.ORIGIN;

	public EcologicalEntityChunkTracker(EcosystemLogic logic) {
		this.logic = logic;
		this.entity = logic.entity;
	}

	public void tick() {
		// Possible performance increase by only checking this every 5 or 10 ticks?
		this.prevPos = this.currentPos;
		this.currentPos = this.entity.getChunkPos();
		if(this.currentPos != this.prevPos) {
			this.onChunkEnter(this.currentPos);
			this.onChunkLeave(this.prevPos);
		}
	}

	// Notify EcosystemChunks
	public void onChunkEnter(ChunkPos pos) {
		World world = this.entity.getWorld();
		if(world.isClient) return;

		EcosystemChunk chunk = getEcosystemChunk(world, pos);
		chunk.onEntityEnter(this.entity);
	}

	public void onChunkLeave(ChunkPos pos) {
		World world = this.entity.getWorld();
		if(world.isClient) return;

		EcosystemChunk chunk = getEcosystemChunk(world, pos);
		chunk.onEntityLeave(this.entity);
	}

	@SuppressWarnings("UnstableApiUsage")
	public EcosystemChunk getEcosystemChunk(World world, ChunkPos pos) {
		Chunk worldChunk = world.getChunk(pos.x, pos.z);
		return worldChunk.getAttachedOrCreate(AbysmAttachments.ECOSYSTEM_CHUNK, () -> new EcosystemChunk(world, pos));
	}
}
