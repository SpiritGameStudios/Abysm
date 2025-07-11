package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;

import java.util.List;
import java.util.Optional;

// Contains methods for handling all entity-related logic that all Ecosystem-related classes may need to call(e.g. EcosystemChunk)
public interface EcologicalEntity {

	/**
	 * @return This Entity's created EcosystemLogic. Used for managing Ecosystem related code for Entity actions.
	 * @see EcologicalEntity#createEcosystemLogic(MobEntity)
	 */
	EcosystemLogic getEcosystemLogic();

	/**
	 * @return This Entity's {@link EcosystemType}. Defined in a non-data-driven Registry, used for knowing an Entity's Ecosystem-related info, such as predators, prey, plants, and more.
	 * @see EcosystemType
	 * @see AbysmEcosystemTypes
	 */
	EcosystemType<?> getEcosystemType();

	/**
	 * @param self This Entity.
	 * @return Create an EcosystemLogic for this Entity, using itself as the MobEntity. This can happen in the Entity's constructor.
	 */
	default EcosystemLogic createEcosystemLogic(MobEntity self) {
		return new EcosystemLogic(self, this.getEcosystemType());
	}

	/**
	 * Tick this Entity's {@link EcosystemLogic}, used for handling & manging various Ecosystem related things.<br><br>
	 * Intended to be called in {@link LivingEntity#tick()}
	 */
	default void tickEcosystemLogic() {
		this.getEcosystemLogic().tick();
	}

	/**
	 * Alert this Entity's {@link EcosystemLogic} that it has just spawned, used for adding itself to {@link dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk}s.<br><br>
	 * Intended to be called in {@link MobEntity#initialize(ServerWorldAccess, LocalDifficulty, SpawnReason, EntityData)}
	 */
	default void alertEcosystemOfSpawn() {
		this.getEcosystemLogic().onSpawn();
	}

	/**
	 * Alert this Entity's {@link EcosystemLogic} that it has just been killed or despawned, used for removing itself form {@link dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk}s.<br><br>
	 * Intended to be called in {@link LivingEntity#onRemove(Entity.RemovalReason)} to account for death & despawning.
	 */
	default void alertEcosystemOfDeath() {
		this.getEcosystemLogic().onDeath();
	}

	@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "UnstableApiUsage"})
	default boolean isHungryCarnivore() {
		World world = ((MobEntity) this).getWorld();
		Chunk chunk = world.getChunk(((MobEntity) this).getBlockPos());

		EcosystemChunk ecosystemChunk = chunk.getAttached(AbysmAttachments.ECOSYSTEM_CHUNK);
		if (ecosystemChunk == null) {
			return false;
		}

		for (EntityType<? extends MobEntity> prey : this.getEcosystemType().prey()) {
			Optional<EcosystemType<? extends MobEntity>> optional = EcosystemType.get(prey);

			if (optional.isEmpty()) {
				continue;
			}
			if (ecosystemChunk.ecosystemTypeNearbyPopOkay(optional.get())) {
				return true;
			}
		}

		return false;
	}

	static <T extends MobEntity> boolean canSpawnInEcosystem(EntityType<T> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
		Chunk chunk = world.getChunk(pos);

		EcosystemChunk ecosystemChunk = chunk.getAttached(AbysmAttachments.ECOSYSTEM_CHUNK);
		if (ecosystemChunk == null) {
			return true;
		}

		Optional<EcosystemType<? extends MobEntity>> optional = EcosystemType.get(type);

		if (optional.isEmpty()) {
			return true;
		}

		EcosystemType<? extends MobEntity> ecosystemType = optional.get();

		int nearbyPopulation = ecosystemChunk.getNearbyEcosystemTypePopulation(ecosystemType);
		return nearbyPopulation < ecosystemType.targetPopulation() * 1.2;
	}

	// Will probably commit crimes by casting self(this) to MobEntity for all of these
	default List<? extends MobEntity> getNearbyPredators() {
		return List.of();
	}

	default List<? extends MobEntity> getNearbyPrey() {
		return List.of();
	}

	default List<? extends MobEntity> getNearbySameType() {
		return List.of();
	}

	default List<BlockPos> getNearbyPlants() {
		return List.of();
	}

}
