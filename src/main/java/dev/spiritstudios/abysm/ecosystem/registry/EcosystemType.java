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
	private final ImmutableSet<EntityType<? extends MobEntity>> predators;
	// The prey of this mob
	private final ImmutableSet<EntityType<? extends MobEntity>> prey;
	// The plants of this mob
	private final ImmutableSet<Block> plants;
	// The target population across the chunkSearchRadius amount of chunks
	private final int targetPopulation;
	// The chunk search radius for any chunk to maintain the targetPopulation amount (1 = 3x3 chunks, 2 = 4x4, etc.)
	// If the current population from the chunk area equals or exceeds the targetPopulation,
	// the population is considered okay and can be hunted, otherwise it is considered not okay and needs repopulating
	private final int populationChunkSearchRadius;

	public EcosystemType(
		EntityType<T> entityType,
		ImmutableSet<EntityType<? extends MobEntity>> predators,
		ImmutableSet<EntityType<? extends MobEntity>> prey,
		ImmutableSet<Block> plants,
		int targetPopulation, int populationChunkSearchRadius
	) {
		this.entityType = entityType;
		this.predators = predators;
		this.prey = prey;
		this.plants = plants;
		this.targetPopulation = targetPopulation;
		this.populationChunkSearchRadius = populationChunkSearchRadius;
	}

	public EntityType<T> getEntityType() {
		return entityType;
	}

	public ImmutableSet<EntityType<? extends MobEntity>> getPredators() {
		return predators;
	}

	public ImmutableSet<EntityType<? extends MobEntity>> getPrey() {
		return prey;
	}

	public ImmutableSet<Block> getPlants() {
		return plants;
	}

	public int getTargetPopulation() {
		return targetPopulation;
	}

	public int getPopulationChunkSearchRadius() {
		return populationChunkSearchRadius;
	}

	public static class Builder<T extends MobEntity & EcologicalEntity> {
		private final EntityType<T> entityType;

		private ImmutableSet<EntityType<? extends MobEntity>> predators = ImmutableSet.of();
		private ImmutableSet<EntityType<? extends MobEntity>> prey = ImmutableSet.of();
		private ImmutableSet<Block> plants = ImmutableSet.of();

		private int targetPopulation = 7;
		private int populationChunkSearchRadius = 1;

		private Builder(EntityType<T> entityType) {
			this.entityType = entityType;
		}

		public static <T extends MobEntity & EcologicalEntity> Builder<T> create(EntityType<T> entityType) {
			return new Builder<>(entityType);
		}

		@SafeVarargs
		public final Builder<T> setPredators(EntityType<? extends MobEntity>... predators) {
			this.predators = ImmutableSet.copyOf(predators);
			return this;
		}

		@SafeVarargs
		public final Builder<T> setPrey(EntityType<? extends MobEntity>... prey) {
			this.prey = ImmutableSet.copyOf(prey);
			return this;
		}

		public Builder<T> setPlants(Block... plants) {
			this.plants = ImmutableSet.copyOf(plants);
			return this;
		}

		public Builder<T> setTargetPopulation(int targetPopulation) {
			this.targetPopulation = targetPopulation;
			return this;
		}

		public Builder<T> setPopulationChunkSearchRadius(int populationChunkSearchRadius) {
			this.populationChunkSearchRadius = populationChunkSearchRadius;
			return this;
		}

		public EcosystemType<T> build() {
			return new EcosystemType<>(
				this.entityType, this.predators, this.prey, this.plants,
				this.targetPopulation, this.populationChunkSearchRadius
			);
		}

		public EntityType<T> getEntityType() {
			return entityType;
		}
	}
}
