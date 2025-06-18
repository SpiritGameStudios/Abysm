package dev.spiritstudios.abysm.entity.ecosystem;

import net.minecraft.entity.mob.MobEntity;

public class EcosystemHungerLogic {
	public final MobEntity entity;

	// The hunger level this entity can start hunting if there's an easy target really close
	public int huntHungerLevel;
	// The hunger level this entity will start fully hunting, targeting any prey(from its list)
	public int desperateHuntHungerLevel;
	// The hunger level this entity can start scavenging if there's an easy plant really close
	public int scavengeHungerLevel;
	// The hunger level this entity will start fully scavenging, finding any plants(from its list)
	public int desperateScavengeHungerLevel;
	// The hunger level this entity always attempts to avoid
	// By default, when hit, this entity slows down, becoming an easy target for others
	public int exhaustedHungerLevel;

	// The max amount of hunger this entity can have
	public int maxHunger;
	// This entity's current hunger level
	public int hunger;

	public EcosystemHungerLogic(MobEntity entity, int huntHungerLevel, int desperateHuntHungerLevel, int scavengeHungerLevel, int desperateScavengeHungerLevel, int exhaustedHungerLevel, int maxHunger, int defaultHunger) {
		this.entity = entity;
		this.huntHungerLevel = huntHungerLevel;
		this.desperateHuntHungerLevel = desperateHuntHungerLevel;
		this.scavengeHungerLevel = scavengeHungerLevel;
		this.desperateScavengeHungerLevel = desperateScavengeHungerLevel;
		this.exhaustedHungerLevel = exhaustedHungerLevel;
		this.maxHunger = maxHunger;
		this.hunger = defaultHunger;
	}

	public void tick() {

	}

	// Variable-test getters
	public boolean canHunt() {
		return this.hunger <= this.huntHungerLevel;
	}

	public boolean desperateHunt() {
		return this.hunger <= this.desperateHuntHungerLevel;
	}

	public boolean canScavenge() {
		return this.hunger <= this.scavengeHungerLevel;
	}

	public boolean desperateScavenge() {
		return this.hunger <= this.desperateScavengeHungerLevel;
	}

	public boolean isExhausted() {
		return this.hunger <= this.exhaustedHungerLevel;
	}
}
