package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaPos;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

// The actual implementations for handling & signaling most Entity-related Ecosystem code & calls,
// such as finding food, repopulating, or signaling the Entity's goals/brain to avoid or actively flee from a predator
public class EcosystemLogic {
	public final MobEntity entity;
	public final EcosystemType<?> type;
	public final World world;
	public final EcosystemTracker tracker;

	public boolean canHunt = false;
	public boolean canScavenge = false;
	public boolean canRepopulate = false;

	public boolean isHunting = false;
	public boolean isBeingHunted = false;
	public int huntTicks = 0;
	public boolean isFavoredInHunt = false;

	// TODO - determine by EcosystemType
	public int breedCooldownTicks;
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

		if (this.isHunting) {
			this.huntTicks--;

			if(this.world.isClient) return;
		}

		if(this.isHunting || this.isBeingHunted) {
			if(this.isFavoredInHunt()) {
				((ServerWorld) this.world).spawnParticles(ParticleTypes.WAX_ON, this.entity.getX(), this.entity.getY() + 1, this.entity.getZ(), 1, 0, 0, 0, 0);
			} else {
				((ServerWorld) this.world).spawnParticles(ParticleTypes.WAX_OFF, this.entity.getX(), this.entity.getY() + 1, this.entity.getZ(), 1, 0, 0, 0, 0);
			}
		}
	}

	public void onDeath() {
		EcosystemAreaPos ecosystemAreaPos = new EcosystemAreaPos(this.entity.getChunkPos());
		this.tracker.onEcosystemAreaLeave(ecosystemAreaPos);
	}

	public void theHuntIsOn(MobEntity target, int huntTicks, boolean favor) {
		this.isHunting = true;
		this.huntTicks = huntTicks;
		this.isFavoredInHunt = favor;
	}

	public void alertOfHunt(MobEntity hunter, boolean favor) {
		this.isBeingHunted = true;
		this.isFavoredInHunt = favor;
	}

	public void allowHunting() {
		this.canHunt = true;
		this.huntTicks = 1;
	}

	//region Getters and Setters

	public boolean canHunt() {
		return this.canHunt;
	}

	public void setCanHunt(boolean canHunt) {
		this.canHunt = canHunt;
	}

	public boolean canRepopulate() {
		return this.canRepopulate;
	}

	public void setCanRepopulate(boolean canRepopulate) {
		this.canRepopulate = canRepopulate;
	}

	public boolean canScavenge() {
		return this.canScavenge;
	}

	public void setCanScavenge(boolean canScavenge) {
		this.canScavenge = canScavenge;
	}

	public boolean isHunting() {
		return this.isHunting;
	}

	public void setHunting(boolean hunting) {
		this.isHunting = hunting;
	}

	public boolean isBeingHunted() {
		return this.isBeingHunted;
	}

	public void setBeingHunted(boolean beingHunted) {
		this.isBeingHunted = beingHunted;
	}

	public int getHuntTicks() {
		return this.huntTicks;
	}

	public void setHuntTicks(int huntTicks) {
		this.huntTicks = huntTicks;
	}

	public boolean isFavoredInHunt() {
		return isFavoredInHunt;
	}

	public void setFavoredInHunt(boolean favoredInHunt) {
		isFavoredInHunt = favoredInHunt;
	}

	public int getBreedTicks() {
		return this.breedTicks;
	}

	public void setBreedTicks(int breedTicks) {
		this.breedTicks = breedTicks;
	}

	//endregion
}
