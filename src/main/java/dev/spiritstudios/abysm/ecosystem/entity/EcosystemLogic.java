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

	public boolean isHungry = false;
	public boolean shouldRepopulate = false;
	public boolean isFleeing = false;

	// Ticks until breeding - 1 minute by default
//	public int breedCooldownTicks = 1200;
	public int breedCooldownTicks = 80; // testing purposes
	public int breedTicks = 0;

	public EcosystemLogic(MobEntity entity, EcosystemType<?> type) {
		this.entity = entity;
		this.type = type;
		this.world = entity.getWorld();
		this.chunkTracker = new EcologicalEntityChunkTracker(this);
	}

	public void onSpawn() {
		this.chunkTracker.getEcosystemChunk(this.world, this.entity.getChunkPos()).addEntity(this.entity);
	}

	public void tick() {
		this.chunkTracker.tick();
		breedTicks++;
	}

	public void onDeath() {
		this.chunkTracker.getEcosystemChunk(this.world, this.entity.getChunkPos()).removeEntity(this.entity);
	}

	public boolean canBreed() {
		return this.breedTicks >= breedCooldownTicks && this.entity.isAlive();
	}

	//region Getters and Setters
	public boolean isHungry() {
		return isHungry;
	}

	public void setHungry(boolean hungry) {
		isHungry = hungry;
	}

	public boolean shouldRepopulate() {
		return shouldRepopulate;
	}

	public void setShouldRepopulate(boolean shouldRepopulate) {
		this.shouldRepopulate = shouldRepopulate;
	}

	public boolean isFleeing() {
		return isFleeing;
	}

	public void setFleeing(boolean fleeing) {
		isFleeing = fleeing;
	}

	public int getBreedTicks() {
		return breedTicks;
	}

	public void setBreedTicks(int breedTicks) {
		this.breedTicks = breedTicks;
	}

	//endregion
}
