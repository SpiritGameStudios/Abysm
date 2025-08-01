package dev.spiritstudios.abysm.ecosystem.registry;

import com.google.common.collect.ImmutableSet;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.specter.api.registry.SpecterRegistryEvents;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Range;

import java.util.Map;
import java.util.Optional;

/**
 * @param entityType                   This mob's EntityType - used for sorting/finding/tracking.
 * @param predators                    The predators of this mob.
 * @param prey                         The prey of this mob.
 * @param plants                       The plants of this mob.
 * @param targetPopulation             The target population across the chunkSearchRadius amount of chunks.
 * @param overpopulationMark           If the population is over this number, it is considered "overpopulated" - being hunted becomes a priority, along with breeding being disallowed.
 * @param nearExtinctMark              If the population is above 0 and below this number, it is considered "near extinct" - breeding becomes a priority, along with being hunted becoming disallowed.
 * @param populationChunkSearchRadius  The chunk search radius for any chunk to maintain the targetPopulation amount (1 = 3x3 chunks, 2 = 4x4, etc.).
 * @param minLitterSize          The minimum amount of babies that can spawn upon breeding.
 * @param maxLitterSize          The maximum amount of babies that can spawn upon breeding.
 * @param minHuntTicks                 The minimum amount of ticks this entity can hunt for before giving up, if it can hunt.
 * @param maxHuntTicks                 The maximum amount of ticks this entity can hunt for before giving up, if it can hunt.
 * @param huntFavorChance              The chance(0f-1f) of this entity being favored in a hunt - Only used if this entity is the hunter starting the hunt!
 * @param favoredHuntSpeedMultiplier   The (swim) speed multiplier of this entity if it is favored in a hunt.
 * @param unfavoredHuntSpeedMultiplier The (swim) speed multiplier of this entity if it is unfavored in a hunt.
 */
public record EcosystemType<T extends MobEntity & EcologicalEntity>(
	EntityType<T> entityType,
	ImmutableSet<EntityType<? extends MobEntity>> predators,
	ImmutableSet<EntityType<? extends MobEntity>> prey,
	ImmutableSet<Block> plants,
	int targetPopulation, int overpopulationMark, int nearExtinctMark,
	int populationChunkSearchRadius,
	int minLitterSize, int maxLitterSize,
	int minHuntTicks, int maxHuntTicks, float huntFavorChance,
	float favoredHuntSpeedMultiplier, float unfavoredHuntSpeedMultiplier
) {
	private static Map<EntityType<? extends MobEntity>, EcosystemType<? extends MobEntity>> ENTITY_TYPE_MAP;

	public static class Builder<T extends MobEntity & EcologicalEntity> {
		private final EntityType<T> entityType;

		private ImmutableSet<EntityType<? extends MobEntity>> predators = ImmutableSet.of();
		private ImmutableSet<EntityType<? extends MobEntity>> prey = ImmutableSet.of();
		private ImmutableSet<Block> plants = ImmutableSet.of();

		private int targetPopulation = 7;
		private int overPopulationMark = 12;
		private int nearExtinctMark = 2;
		private int populationChunkSearchRadius = 2;
		private int minLitterSize = 1;
		private int maxLitterSize = 1;

		private int minHuntTicks = 1200; // 60 seconds
		private int maxHuntTicks = 1600; // 80 seconds
		private float huntFavorChance = 0.5f;
		private float favoredHuntSpeedMultiplier = 1.1f;
		private float unfavoredHuntSpeedMultiplier = 0.9f;

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
			return this.setTargetPopulation(targetPopulation, (int) (targetPopulation * 1.5), 2);
		}

		public Builder<T> setTargetPopulation(int targetPopulation, int overPopulationMark, int nearExtinctMark) {
			this.targetPopulation = targetPopulation;
			this.overPopulationMark = overPopulationMark;
			this.nearExtinctMark = nearExtinctMark;
			return this;
		}

		public Builder<T> setOverpopulationMark(int overpopulationMark) {
			this.overPopulationMark = overpopulationMark;
			return this;
		}

		public Builder<T> setNearExtinctMark(int nearExtinctMark) {
			this.nearExtinctMark = nearExtinctMark;
			return this;
		}

		public Builder<T> setPopulationChunkSearchRadius(int populationChunkSearchRadius) {
			this.populationChunkSearchRadius = populationChunkSearchRadius;
			return this;
		}

		public Builder<T> setLitterSize(int minEntitiesPerBreed, int maxEntitiesPerBreed) {
			this.minLitterSize = minEntitiesPerBreed;
			this.maxLitterSize = maxEntitiesPerBreed;
			return this;
		}

		public Builder<T> setHuntTicks(int minHuntTicks, int maxHuntTicks) {
			this.minHuntTicks = minHuntTicks;
			this.maxHuntTicks = maxHuntTicks;
			return this;
		}

		public Builder<T> setHuntFavorChance(@Range(from = 0, to = 1) float huntFavorChance) {
			this.huntFavorChance = huntFavorChance;
			return this;
		}

		public Builder<T> setHuntSpeedMultipliers(float favoredHuntSpeedMultiplier, float unfavoredHuntSpeedMultiplier) {
			this.favoredHuntSpeedMultiplier = favoredHuntSpeedMultiplier;
			this.unfavoredHuntSpeedMultiplier = unfavoredHuntSpeedMultiplier;
			return this;
		}

		public Builder<T> setFavoredHuntSpeed(float favoredHuntSpeedMultiplier) {
			this.favoredHuntSpeedMultiplier = favoredHuntSpeedMultiplier;
			return this;
		}

		public Builder<T> setUnfavoredHuntSpeed(float unfavoredHuntSpeedMultiplier) {
			this.unfavoredHuntSpeedMultiplier = unfavoredHuntSpeedMultiplier;
			return this;
		}

		public EcosystemType<T> build() {
			return new EcosystemType<>(
				this.entityType, this.predators, this.prey, this.plants,
				this.targetPopulation, this.overPopulationMark, this.nearExtinctMark,
				this.populationChunkSearchRadius,
				this.minLitterSize, this.maxLitterSize,
				this.minHuntTicks, this.maxHuntTicks, this.huntFavorChance,
				this.favoredHuntSpeedMultiplier, this.unfavoredHuntSpeedMultiplier
			);
		}

		public EntityType<T> getEntityType() {
			return entityType;
		}
	}

	public static Optional<EcosystemType<? extends MobEntity>> get(EntityType<? extends MobEntity> type) {
		return Optional.ofNullable(ENTITY_TYPE_MAP.getOrDefault(type, null));
	}

	public static void init() {
		SpecterRegistryEvents.REGISTRIES_FROZEN.register(() -> {
			ENTITY_TYPE_MAP = new Object2ObjectArrayMap<>(AbysmRegistries.ECOSYSTEM_TYPE.size());

			for (EcosystemType<?> ecosystemType : AbysmRegistries.ECOSYSTEM_TYPE) {
				ENTITY_TYPE_MAP.put(ecosystemType.entityType, ecosystemType);
			}
		});
	}
}
