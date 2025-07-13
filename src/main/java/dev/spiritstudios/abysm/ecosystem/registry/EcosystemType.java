package dev.spiritstudios.abysm.ecosystem.registry;

import com.google.common.collect.ImmutableSet;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.specter.api.registry.SpecterRegistryEvents;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;

import java.util.Map;
import java.util.Optional;

/**
 * @param entityType                  This mob's EntityType - used for sorting/finding/tracking
 * @param predators                   The predators of this mob
 * @param prey                        The prey of this mob
 * @param plants                      The plants of this mob
 * @param targetPopulation            The target population across the chunkSearchRadius amount of chunks
 * @param populationChunkSearchRadius The chunk search radius for any chunk to maintain the targetPopulation amount (1 = 3x3 chunks, 2 = 4x4, etc.) If the current population from the chunk area equals or exceeds the targetPopulation, the population is considered okay and can be hunted, otherwise it is considered not okay and needs repopulating
 */
public record EcosystemType<T extends MobEntity & EcologicalEntity>(
	EntityType<T> entityType,
	ImmutableSet<EntityType<? extends MobEntity>> predators,
	ImmutableSet<EntityType<? extends MobEntity>> prey,
	ImmutableSet<Block> plants, int targetPopulation,
	int populationChunkSearchRadius
) {
	private static Map<EntityType<? extends MobEntity>, EcosystemType<? extends MobEntity>> ENTITY_TYPE_MAP;

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
