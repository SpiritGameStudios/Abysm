package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

// The actual implementations for handling & signaling most Entity-related Ecosystem code & calls,
// such as finding food, repopulating, or signaling the Entity's goals/brain to avoid or actively flee from a predator
public class EcosystemLogic {
	public final MobEntity entity;
	public final EcosystemType<?> type;
	public final World world;
	public final EcologicalEntityChunkTracker chunkTracker;

	public EcosystemLogic(MobEntity entity, EcosystemType<?> type) {
		this.entity = entity;
		this.type = type;
		this.world = entity.getWorld();
		this.chunkTracker = new EcologicalEntityChunkTracker(this);
	}

	public void onSpawn() {
		this.chunkTracker.getEcosystemChunk(this.world, this.entity.getChunkPos()).onEntitySpawn(this.entity);
	}

	public void tick() {
		this.chunkTracker.tick();
	}

	public void onDeath() {
		this.chunkTracker.getEcosystemChunk(this.world, this.entity.getChunkPos()).onEntityDeath(this.entity);
	}

}
