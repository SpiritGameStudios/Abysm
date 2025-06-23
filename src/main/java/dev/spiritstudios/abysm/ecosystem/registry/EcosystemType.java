package dev.spiritstudios.abysm.ecosystem.registry;

import com.google.common.collect.ImmutableSet;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;

public class EcosystemType<T extends MobEntity & EcologicalEntity> {
	// This mob's EntityType - used for sorting/finding/tracking
	private final EntityType<T> entityType;
	// The predators of this mob
	private final ImmutableSet<EntityType<?>> predators;
	// The prey of this mob
	private final ImmutableSet<EntityType<?>> prey;
	// The plants of this mob
	private final ImmutableSet<Block> plants;
	// The attempted population of this mob per chunk(accounts for adjacent chunks too)
	private final int populationPerChunk;

	public EcosystemType(EntityType<T> entityType, ImmutableSet<EntityType<?>> predators, ImmutableSet<EntityType<?>> prey, ImmutableSet<Block> plants, int populationPerChunk) {
		this.entityType = entityType;
		this.predators = predators;
		this.prey = prey;
		this.plants = plants;
		this.populationPerChunk = populationPerChunk;
	}

	public EntityType<T> getEntityType() {
		return entityType;
	}

	public ImmutableSet<EntityType<?>> getPredators() {
		return predators;
	}

	public ImmutableSet<EntityType<?>> getPrey() {
		return prey;
	}

	public ImmutableSet<Block> getPlants() {
		return plants;
	}

	public int getPopulationPerChunk() {
		return populationPerChunk;
	}

	public static class Builder<T extends MobEntity & EcologicalEntity> {
		private final EntityType<T> entityType;

		private ImmutableSet<EntityType<?>> predators = ImmutableSet.of();
		private ImmutableSet<EntityType<?>> prey = ImmutableSet.of();
		private ImmutableSet<Block> plants = ImmutableSet.of();

		private int populationPerChunk;

		private Builder(EntityType<T> entityType) {
			this.entityType = entityType;
		}

		public static <T extends MobEntity & EcologicalEntity> Builder<T> create(EntityType<T> entityType) {
			return new Builder<>(entityType);
		}

		public Builder<T> setPredators(EntityType<?>... predators) {
			this.predators = ImmutableSet.copyOf(predators);
			return this;
		}

		public Builder<T> setPrey(EntityType<?>... prey) {
			this.prey = ImmutableSet.copyOf(prey);
			return this;
		}

		public Builder<T> setPlants(Block... plants) {
			this.plants = ImmutableSet.copyOf(plants);
			return this;
		}

		public Builder<T> setPopulationPerChunk(int populationPerChunk) {
			this.populationPerChunk = populationPerChunk;
			return this;
		}

		public EcosystemType<T> build() {
			return new EcosystemType<>(this.entityType, this.predators, this.predators, this.plants,
				this.populationPerChunk
			);
		}

		public EntityType<T> getEntityType() {
			return entityType;
		}
	}
}
