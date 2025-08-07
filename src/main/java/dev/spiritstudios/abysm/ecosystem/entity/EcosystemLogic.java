package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaPos;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

// The actual implementations for handling & signaling most Entity-related Ecosystem code & calls,
// such as finding food, repopulating, or signaling the Entity's goals/brain to avoid or actively flee from a predator
public class EcosystemLogic {
	public final MobEntity entity;
	public final EcosystemType<?> type;
	public final World world;
	public final EcosystemTracker tracker;

	// Remove for not helpful anymore because it's been split up?
	public boolean isHungry = false;
	public boolean isFleeing = false;

	public boolean canHunt = false;
	public boolean canScavenge = false;
	public boolean canRepopulate = false;

	public boolean isHunting = false;
	public boolean isBeingHunted = false;
	public boolean isFavoredInHunt = false;
	@Nullable
	public MobEntity huntTargetEntity = null;
	@Nullable
	public MobEntity hunterEntity = null;

	public boolean isRepopulating = false;
	@Nullable
	public MobEntity breedMate = null;
	// Ticks until breeding - 20 seconds by default, but should probably be determined by EcosystemType later
	public int breedCooldownTicks = 400;
	public int breedTicks = 0;

	public EcosystemLogic(MobEntity entity, EcosystemType<?> type) {
		this.entity = entity;
		this.type = type;
		this.world = entity.getWorld();
		this.tracker = new EcosystemTracker(this);
		this.breedCooldownTicks = 80;
	}

	public void onSpawn() {
		EcosystemAreaPos ecosystemAreaPos = new EcosystemAreaPos(this.entity.getChunkPos());
		this.tracker.onEcosystemAreaEnter(ecosystemAreaPos);
//		this.tracker.getEcosystemArea(ecosystemAreaPos).addEntity(this.entity);
//		this.tracker.getEcosystemArea()
//		this.chunkTracker.getEcosystemChunk(this.world, this.entity.getChunkPos()).addEntity(this.entity);
	}

	public void tick() {
		this.tracker.tick();
		breedTicks++;
	}

	public void onDeath() {
		EcosystemAreaPos ecosystemAreaPos = new EcosystemAreaPos(this.entity.getChunkPos());
		this.tracker.onEcosystemAreaLeave(ecosystemAreaPos);
//		this.tracker.getEcosystemArea(ecosystemAreaPos).removeEntity(this.entity);
//		this.chunkTracker.getEcosystemChunk(this.world, this.entity.getChunkPos()).removeEntity(this.entity);
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
		return canRepopulate;
	}

	public void setCanRepopulate(boolean canRepopulate) {
		this.canRepopulate = canRepopulate;
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

	public boolean isCanHunt() {
		return canHunt;
	}

	public void setCanHunt(boolean canHunt) {
		this.canHunt = canHunt;
	}

	public boolean isCanScavenge() {
		return canScavenge;
	}

	public void setCanScavenge(boolean canScavenge) {
		this.canScavenge = canScavenge;
	}

	public boolean isCanRepopulate() {
		return canRepopulate;
	}

	public @Nullable MobEntity getBreedMate() {
		return breedMate;
	}

	public void setBreedMate(@Nullable MobEntity breedMate) {
		this.breedMate = breedMate;
	}

	//endregion
}
