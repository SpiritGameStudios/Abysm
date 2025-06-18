package dev.spiritstudios.abysm.entity.ecosystem;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EcosystemLogic {
	public final MobEntity entity;
	protected List<Class<? extends LivingEntity>> predators;
	protected List<Class<? extends LivingEntity>> prey;
	protected List<Block> plants;

	public EcosystemHungerLogic hungerLogic;

	public EcosystemLogic(MobEntity entity, List<Class<? extends LivingEntity>> predators, List<Class<? extends LivingEntity>> prey, List<Block> plants, int huntHungerLevel, int desperateHuntHungerLevel, int scavengeHungerLevel, int desperateScavengeHungerLevel, int exhaustedHungerLevel, int maxHunger, int defaultHunger) {
		this.entity = entity;
		this.predators = predators;
		this.prey = prey;
		this.plants = plants;

		this.hungerLogic = new EcosystemHungerLogic(this.entity,
			huntHungerLevel, desperateHuntHungerLevel,
			scavengeHungerLevel, desperateScavengeHungerLevel,
			exhaustedHungerLevel,
			maxHunger, defaultHunger
		);
	}

	public void tick() {
		this.hungerLogic.tick();
	}

	// Helper methods
	public List<? extends LivingEntity> findNearbyPredators(double distance) {
		if(this.predators.isEmpty()) return List.of();

		World world = this.entity.getWorld();
		Box searchBox = this.getSearchBox(distance);
		return findNearbyEntities(this.predators, world, searchBox);
	}

	public List<? extends LivingEntity> findNearbyPrey(double distance) {
		if(this.prey.isEmpty()) return List.of();

		World world = this.entity.getWorld();
		Box searchBox = this.getSearchBox(distance);
		return findNearbyEntities(this.prey, world, searchBox);
	}

//	public List<BlockPos> findNearbyPlants(double distance) {
//		if(this.plants.isEmpty()) return List.of();
//
//		World world = this.entity.getWorld();
//		Box searchBox = this.getSearchBox(distance);
//	}

	// Helper methods
	protected Box getSearchBox(double distance) {
		return this.entity.getBoundingBox().expand(distance);
	}

	protected double getFollowRange() {
		return this.entity.getAttributeValue(EntityAttributes.FOLLOW_RANGE);
	}

	protected List<? extends LivingEntity> findNearbyEntities(List<Class<? extends LivingEntity>> targetClasses, World world, Box searchBox) {
		return findNearbyEntities(targetClasses, world, searchBox, Entity::isAlive);
	}

	protected List<? extends LivingEntity> findNearbyEntities(List<Class<? extends LivingEntity>> targetClasses, World world, Box searchBox, Predicate<Entity> predicate) {
		List<? extends LivingEntity> list = new ArrayList<>();
		if(targetClasses.isEmpty()) return list;

		list = targetClasses.stream()
			.flatMap(predatorClass -> findNearbyEntities(predatorClass, world, searchBox, predicate).stream())
			.collect(Collectors.toList());
		return list;
	}

	protected List<? extends LivingEntity> findNearbyEntities(Class<? extends LivingEntity> targetClass, World world, Box searchBox) {
		return findNearbyEntities(targetClass, world, searchBox, Entity::isAlive);
	}

	protected List<? extends LivingEntity> findNearbyEntities(Class<? extends LivingEntity> targetClass, World world, Box searchBox, Predicate<Entity> predicate) {
		return world.getEntitiesByClass(targetClass, searchBox, predicate);
	}

	// Generic variable getters & setters
	public EcosystemHungerLogic getHungerLogic() {
		return hungerLogic;
	}
}
