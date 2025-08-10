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

	public int huntTicks = 0;

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
		this.breedCooldownTicks = 80; // so much for 20 seconds by default
	}

	public void onSpawn() {
		EcosystemAreaPos ecosystemAreaPos = new EcosystemAreaPos(this.entity.getChunkPos());
		this.tracker.onEcosystemAreaEnter(ecosystemAreaPos);
	}

	public void tick() {
		this.tracker.tick();
		this.breedTicks++;
		if (huntTicks > 0 && this.huntTargetEntity != null) {
			this.huntTicks--;
		}
	}

	public void onDeath() {
		EcosystemAreaPos ecosystemAreaPos = new EcosystemAreaPos(this.entity.getChunkPos());
		this.tracker.onEcosystemAreaLeave(ecosystemAreaPos);
	}

	public boolean canBreed() {
		return this.breedTicks >= this.breedCooldownTicks && this.entity.isAlive();
	}

	//region Getters and Setters
	public boolean isHungry() {
		return this.isHungry;
	}

	public void setHungry(boolean hungry) {
		this.isHungry = hungry;
	}

	public boolean shouldRepopulate() {
		return this.canRepopulate;
	}

	public void setCanRepopulate(boolean canRepopulate) {
		this.canRepopulate = canRepopulate;
	}

	public boolean isFleeing() {
		return this.isFleeing;
	}

	public void setFleeing(boolean fleeing) {
		this.isFleeing = fleeing;
	}

	public int getBreedTicks() {
		return this.breedTicks;
	}

	public void setBreedTicks(int breedTicks) {
		this.breedTicks = breedTicks;
	}

	public boolean canHunt() {
		return this.canHunt;
	}

	public void stopHunt() {
		this.isFavoredInHunt = false;
		this.isHunting = false;
		this.isBeingHunted = false;
		this.hunterEntity = null;
		this.huntTargetEntity = null;
	}

	public void allowHunting() {
		this.canHunt = true;
		this.huntTicks = 1;
	}

	public void theHuntIsOn(MobEntity target, int huntTicks, boolean favor) {
		this.isHunting = true;
		this.huntTicks = huntTicks;
		this.huntTargetEntity = target;
		this.isFavoredInHunt = favor;
	}

	public void alertOfHunt(MobEntity hunter, boolean favor) {
		this.hunterEntity = hunter;
		this.isBeingHunted = true;
		this.isFavoredInHunt = favor;
	}

	public boolean canScavenge() {
		return this.canScavenge;
	}

	public void setCanScavenge(boolean canScavenge) {
		this.canScavenge = canScavenge;
	}

	public boolean canRepopulate() {
		return this.canRepopulate;
	}

	public @Nullable MobEntity getBreedMate() {
		return this.breedMate;
	}

	public void setBreedMate(@Nullable MobEntity breedMate) {
		this.breedMate = breedMate;
	}

	//endregion
}
