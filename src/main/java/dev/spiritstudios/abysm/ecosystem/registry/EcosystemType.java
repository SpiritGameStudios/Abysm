package dev.spiritstudios.abysm.ecosystem.registry;

import com.google.common.collect.ImmutableSet;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.specter.api.registry.SpecterRegistryEvents;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.jetbrains.annotations.Range;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;

/**
 * @param entityType         This mob's EntityType - used for sorting/finding/tracking.
 * @param predators          The predators of this mob.
 * @param prey               The prey of this mob.
 * @param plants             The plants of this mob.
 * @param targetPopulation   The target population across the chunkSearchRadius amount of chunks.
 * @param minHuntTicks       The minimum amount of ticks this entity can hunt for before giving up, if it can hunt.
 * @param maxHuntTicks       The maximum amount of ticks this entity can hunt for before giving up, if it can hunt.
 * @param huntFavorChance    The chance(0f-1f) of this entity being favored in a hunt - Only used if this entity is the hunter starting the hunt!
 * @param favoredHuntSpeed   The (swim) speed ADDED to this entity if it is favored in a hunt.
 * @param unfavoredHuntSpeed The (swim) speed SUBTRACTED from this entity if it is unfavored in a hunt.
 * @param breedCooldownTicks The amount of ticks until this entity can breed after spawning and breeding.
 * @param minLitterSize      The minimum amount of babies that can spawn upon breeding.
 * @param maxLitterSize      The maximum amount of babies that can spawn upon breeding.
 */
public record EcosystemType<T extends LivingEntity & EcologicalEntity>(
	EntityType<T> entityType,
	Set<EntityType<? extends LivingEntity>> predators,
	Set<EntityType<? extends LivingEntity>> prey,
	Set<Block> plants,
	int targetPopulation,
	int minHuntTicks, int maxHuntTicks, float huntFavorChance,
	float favoredHuntSpeed, float unfavoredHuntSpeed,
	int breedCooldownTicks, int minLitterSize, int maxLitterSize
) {
	private static Map<EntityType<? extends LivingEntity>, EcosystemType<? extends LivingEntity>> ENTITY_TYPE_MAP;

	public static class Builder<T extends LivingEntity & EcologicalEntity> {
		private final EntityType<T> entityType;

		private ImmutableSet<EntityType<? extends LivingEntity>> predators = ImmutableSet.of();
		private ImmutableSet<EntityType<? extends LivingEntity>> prey = ImmutableSet.of();
		private ImmutableSet<Block> plants = ImmutableSet.of();

		private int targetPopulation = 7;

		private int minHuntTicks = 1200; // 60 seconds
		private int maxHuntTicks = 1600; // 80 seconds
		private float huntFavorChance = 0.85f;
		private float favoredHuntSpeed = 0.5f;
		private float unfavoredHuntSpeed = -0.25f;

		private int breedCooldownTicks = 300; // 15 seconds
		private int minLitterSize = 1;
		private int maxLitterSize = 1;

		private Builder(EntityType<T> entityType) {
			this.entityType = entityType;
		}

		public static <T extends Mob & EcologicalEntity> Builder<T> create(EntityType<T> entityType) {
			return new Builder<>(entityType);
		}

		@SafeVarargs
		public final Builder<T> setPredators(EntityType<? extends LivingEntity>... predators) {
			this.predators = ImmutableSet.copyOf(predators);
			return this;
		}

		@SafeVarargs
		public final Builder<T> setPrey(EntityType<? extends LivingEntity>... prey) {
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

		public Builder<T> setHuntTicks(int minHuntTicks, int maxHuntTicks) {
			this.minHuntTicks = minHuntTicks;
			this.maxHuntTicks = maxHuntTicks;
			return this;
		}

		public Builder<T> setHuntFavorChance(@Range(from = 0, to = 1) float huntFavorChance) {
			this.huntFavorChance = huntFavorChance;
			return this;
		}

		public Builder<T> setHuntSpeedModifiers(float favoredHuntSpeed, float unfavoredHuntSpeed) {
			return this.setFavoredHuntSpeed(favoredHuntSpeed).setUnfavoredHuntSpeed(unfavoredHuntSpeed);
		}

		public Builder<T> setFavoredHuntSpeed(float favoredHuntSpeed) {
			this.favoredHuntSpeed = favoredHuntSpeed;
			return this;
		}

		public Builder<T> setUnfavoredHuntSpeed(float unfavoredHuntSpeed) {
			this.unfavoredHuntSpeed = unfavoredHuntSpeed;
			return this;
		}

		public Builder<T> setBreedCooldownTicks(int breedCooldownTicks) {
			this.breedCooldownTicks = breedCooldownTicks;
			return this;
		}

		public Builder<T> setLitterSize(int minEntitiesPerBreed, int maxEntitiesPerBreed) {
			this.minLitterSize = minEntitiesPerBreed;
			this.maxLitterSize = maxEntitiesPerBreed;
			return this;
		}

		public EcosystemType<T> build() {
			return new EcosystemType<>(
				this.entityType, this.predators, this.prey, this.plants,
				this.targetPopulation,
				this.minHuntTicks, this.maxHuntTicks, this.huntFavorChance,
				this.favoredHuntSpeed, this.unfavoredHuntSpeed,
				this.breedCooldownTicks, this.minLitterSize, this.maxLitterSize
			);
		}

		public EntityType<T> getEntityType() {
			return entityType;
		}
	}

	public static Optional<EcosystemType<? extends LivingEntity>> get(EntityType<? extends LivingEntity> type) {
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
