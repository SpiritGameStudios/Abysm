package dev.spiritstudios.abysm.entity.ecosystem;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

import java.util.List;

public class EcosystemLogicBuilder {
	private MobEntity entity;
	private List<Class<? extends LivingEntity>> predators;
	private List<Class<? extends LivingEntity>> prey;
	private List<Block> plants;
	private int huntLevel = 15;
	private int desperateHuntLevel = 5;
	private int scavengeLevel = 15;
	private int desperateScavengeLevel = 5;
	private int exhaustedLevel = 3;
	private int maxHungerLevel = 20;
	private int defaultHungerLevel = 20;

	public EcosystemLogicBuilder(MobEntity entity, List<Class<? extends LivingEntity>> predators, List<Class<? extends LivingEntity>> prey, List<Block> plants) {
		this.entity = entity;
		this.predators = predators;
		this.prey = prey;
		this.plants = plants;
	}

	public EcosystemLogicBuilder setHuntLevel(int huntLevel) {
		this.huntLevel = huntLevel;
		return this;
	}

	public EcosystemLogicBuilder setDesperateHuntLevel(int desperateHuntLevel) {
		this.desperateHuntLevel = desperateHuntLevel;
		return this;
	}

	public EcosystemLogicBuilder setScavengeLevel(int scavengeLevel) {
		this.scavengeLevel = scavengeLevel;
		return this;
	}

	public EcosystemLogicBuilder setDesperateScavengeLevel(int desperateScavengeLevel) {
		this.desperateScavengeLevel = desperateScavengeLevel;
		return this;
	}

	public EcosystemLogicBuilder setExhaustedLevel(int exhaustedLevel) {
		this.exhaustedLevel = exhaustedLevel;
		return this;
	}

	public EcosystemLogicBuilder setMaxHungerLevel(int maxHungerLevel) {
		this.maxHungerLevel = maxHungerLevel;
		return this;
	}

	public EcosystemLogicBuilder setDefaultHungerLevel(int defaultHungerLevel) {
		this.defaultHungerLevel = defaultHungerLevel;
		return this;
	}

	public EcosystemLogic build() {
		return new EcosystemLogic(entity, predators, prey, plants, huntLevel, desperateHuntLevel, scavengeLevel, desperateScavengeLevel, exhaustedLevel, maxHungerLevel, defaultHungerLevel);
	}
}
