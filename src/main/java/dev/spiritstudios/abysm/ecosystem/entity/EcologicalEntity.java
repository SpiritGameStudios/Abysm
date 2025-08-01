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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Contains methods for handling all entity-related logic that any Ecosystem-related systems may need to call(e.g. {@link EcosystemChunk})<br><br>
 *
 * <b>YOU MUST MANUALLY CALL THE FOLLOWING:</b>
 * <ul>
 *     <li>{@link EcologicalEntity#tickEcosystemLogic()} every tick (e.g. {@link LivingEntity#tick()}).</li>
 *     <li>{@link EcologicalEntity#alertEcosystemOfSpawn()} upon entity spawning (e.g. {@link MobEntity#initialize(ServerWorldAccess, LocalDifficulty, SpawnReason, EntityData)}).</li>
 *     <li>{@link EcologicalEntity#alertEcosystemOfDeath()} upon entity removal(death or despawn) (e.g. {@link LivingEntity#onRemove(Entity.RemovalReason)}).</li>
 * </ul><br>
 *
 * For the EcosystemLogic, have your own variable in your entity class and use {@link EcologicalEntity#createEcosystemLogic(MobEntity)} (e.g. in your entity class constructor) to set the variable once. Then return it in {@link EcologicalEntity#getEcosystemLogic()}.<br><br>
 *
 * {@link EcologicalEntity#createChildEntity(ServerWorld, MobEntity, BlockPos)} can be overridden for custom breeding results(e.g. giving the child one of the parent's entity patterns or variants).<br><br>
 *
 * Beyond that, this interface also contains various getters/setters used in Ecosystem-related systems, which can be manually overridden if desired. Most of them will be towards the bottom of the interface.
 */
@SuppressWarnings("UnstableApiUsage")
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
	 * Alert this Entity's {@link EcosystemLogic} that it has just been killed or despawned(removed), used for removing itself form {@link dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk}s.<br><br>
	 * Intended to be called in {@link LivingEntity#onRemove(Entity.RemovalReason)} to account for death & despawning.
	 */
	default void alertEcosystemOfDeath() {
		this.getEcosystemLogic().onDeath();
	}

	/**
	 * Main method called for breeding this entity with another.<br><br>
	 *
	 * This accounts for the {@link EcosystemType#minLitterSize()} & {@link EcosystemType#maxLitterSize()} numbers, spawning in a random amount of entities between those two numbers.
	 */
	default void breed(ServerWorld world, MobEntity other) {
		this.breed(world, other, false);
	}

	default void breed(ServerWorld world, MobEntity other, boolean overrideCanBreed) {
		if(!overrideCanBreed && !canBreed()) return;

		EcosystemType<?> ecosystemType = this.getEcosystemType();
		MobEntity self = (MobEntity) this;
		int amount = self.getRandom().nextBetween(ecosystemType.minLitterSize(), ecosystemType.maxLitterSize());
		for (int i = 0; i < amount; i++) {
			spawnChildEntity(world, other);
		}
		this.setBreedTicks(0);
		this.setShouldRepopulate(false);
		// TODO - custom packet for this.
		//  Entity status doesn't work unless every single non-PassiveEntity ecological entity manually implements it
		world.spawnParticles(ParticleTypes.HEART, self.getX(), self.getY(), self.getZ(), 7, 0.5, 0.5, 0.5, 1);
	}

	/**
	 * Creates and spawns a child entity into the world.
	 *
	 * @param world The world to spawn the entity into (assumed to be the same world as this entity)
	 * @param other The other entity this entity is breeding with - given to {@link EcologicalEntity#createChildEntity(ServerWorld, MobEntity, BlockPos)}.
	 */
	default void spawnChildEntity(ServerWorld world, MobEntity other) {
		MobEntity self = (MobEntity) this;
		MobEntity child = this.createChildEntity(world, other, self.getBlockPos());
		if(child == null) return;

		// TODO - Should we have any entities that can be bred by the player,
		//  advancement/statistic credit needs to be given here
		child.setBaby(true);
		child.refreshPositionAndAngles(self.getX(), self.getY(), self.getZ(), 0f, 0f);
		world.spawnEntity(child);
	}

	/**
	 * Overridable method for creating the MobEntity used for spawning children. <br><br>
	 *
	 * Use the "other" param entity for mixing and matching whatever data wanted(e.g. choosing between one of the two parent's variants)
	 *
	 * @param world The world the child is spawning into
	 * @param other The other parent entity - use to mix and match whatever data wanted
	 * @return The created child entity
	 */
	default MobEntity createChildEntity(ServerWorld world, MobEntity other, BlockPos spawnPos) {
		MobEntity child = this.getEcosystemType().entityType().create(world, SpawnReason.BREEDING);
		if(child == null) return null;
		child.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0f, 0f);
		child.initialize(world, world.getLocalDifficulty(spawnPos), SpawnReason.BREEDING, null);
		return child;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

	// EcosystemLogic getters/setters

	default boolean canBreed() {
		return this.getEcosystemLogic().canBreed();
	}

	default int getBreedTicks() {
		return this.getEcosystemLogic().getBreedTicks();
	}

	default void setBreedTicks(int breedTicks) {
		this.getEcosystemLogic().setBreedTicks(breedTicks);
	}

	default boolean isHungry() {
		return this.getEcosystemLogic().isHungry();
	}

	default void setHungry(boolean hungry) {
		this.getEcosystemLogic().setHungry(hungry);
	}

	default boolean isFleeing() {
		return this.getEcosystemLogic().isFleeing();
	}

	default void setFleeing(boolean fleeing) {
		this.getEcosystemLogic().setFleeing(fleeing);
	}

	default boolean shouldRepopulate() {
		return this.getEcosystemLogic().shouldRepopulate();
	}

	default void setShouldRepopulate(boolean shouldRepopulate) {
		this.getEcosystemLogic().setCanRepopulate(shouldRepopulate);
	}

	default @Nullable MobEntity getBreedMate() {
		return this.getEcosystemLogic().getBreedMate();
	}

	default void setBreedMate(@Nullable MobEntity breedMate) {
		this.getEcosystemLogic().setBreedMate(breedMate);
	}

}
