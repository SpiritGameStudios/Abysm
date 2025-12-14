package dev.spiritstudios.abysm.world.ecosystem.entity;

import dev.spiritstudios.abysm.network.HappyEntityParticlesS2CPayload;
import dev.spiritstudios.abysm.world.ecosystem.chunk.EcosystemArea;
import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Contains methods for handling all entity-related logic that any Ecosystem-related systems may need to call(e.g. {@link EcosystemLogic})<br><br>
 *
 * <b>YOU MUST MANUALLY CALL THE FOLLOWING:</b>
 * <ul>
 *     <li>{@link EcologicalEntity#tickEcosystemLogic()} every tick (e.g. {@link LivingEntity#tick()}).</li>
 *     <li>{@link EcologicalEntity#alertEcosystemOfSpawn()} upon entity spawning (e.g. {@link Mob#finalizeSpawn(ServerLevelAccessor, DifficultyInstance, EntitySpawnReason, SpawnGroupData)}).</li>
 *     <li>{@link EcologicalEntity#alertEcosystemOfDeath()} upon entity removal(death or despawn) (e.g. {@link LivingEntity#onRemoval(Entity.RemovalReason)}).</li>
 * </ul><br>
 * <p>
 * For the EcosystemLogic, have your own variable in your entity class and use {@link EcologicalEntity#createEcosystemLogic(Mob)} (e.g. in your entity class constructor) to set the variable once. Then return it in {@link EcologicalEntity#getEcosystemLogic()}.<br><br>
 * <p>
 * {@link EcologicalEntity#createChildEntity(ServerLevel, LivingEntity, BlockPos)} can be overridden for custom breeding results(e.g. giving the child one of the parent's entity patterns or variants).<br><br>
 * <p>
 * Beyond that, this interface also contains various getters/setters used in Ecosystem-related systems, which can be manually overridden if desired. Most of them will be towards the bottom of the interface.
 *
 * @see EcologicalEntity#tickEcosystemLogic()
 * @see EcologicalEntity#alertEcosystemOfSpawn()
 * @see EcologicalEntity#alertEcosystemOfDeath()
 * @see EcologicalEntity#createChildEntity(ServerLevel, LivingEntity, BlockPos)
 */
public interface EcologicalEntity {

	/**
	 * @return This Entity's created EcosystemLogic. Used for managing Ecosystem related code for Entity actions.
	 * @see EcologicalEntity#createEcosystemLogic(Mob)
	 */
	EcosystemLogic getEcosystemLogic();

	EcosystemData getEcosystemData();


	/**
	 * @param self This Entity.
	 * @return Create an EcosystemLogic for this Entity, using itself as the MobEntity. This can happen in the Entity's constructor.
	 */
	default EcosystemLogic createEcosystemLogic(Mob self) {
		return new EcosystemLogic(self);
	}

	/**
	 * Tick this Entity's {@link EcosystemLogic}, used for handling & manging various Ecosystem related things.<br><br>
	 * Intended to be called in {@link LivingEntity#tick()}
	 */
	default void tickEcosystemLogic() {
		this.getEcosystemLogic().tick();
	}

	/**
	 * Alert this Entity's {@link EcosystemLogic} that it has just spawned, used for adding itself to {@link EcosystemArea}s.<br><br>
	 * Intended to be called in {@link Mob#finalizeSpawn(ServerLevelAccessor, DifficultyInstance, EntitySpawnReason, SpawnGroupData)}
	 */
	default void alertEcosystemOfSpawn() {
		this.getEcosystemLogic().onSpawn();
	}

	/**
	 * Alert this Entity's {@link EcosystemLogic} that it has just been killed or despawned(removed), used for removing itself from {@link EcosystemArea}s.<br><br>
	 * Intended to be called in {@link LivingEntity#onRemoval(Entity.RemovalReason)} to account for death & despawning.
	 */
	default void alertEcosystemOfDeath() {
		this.getEcosystemLogic().onDeath();
	}

	/**
	 * Main method called for starting a hunt as the predator.<br><br>
	 * <p>
	 * Determines hunt favor and length, applies hunt's favor buffs/debuffs, and alerts the target prey about the hunt.
	 *
	 * @see dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.HuntPreyGoal
	 */
	default void theHuntIsOn(Mob self, Mob target) {
		var data = getEcosystemData();
		var level = self.level();

		float hunterFavorChance = data.favorChance();
		boolean hunterFavored = level.getRandom().nextFloat() <= hunterFavorChance;

		int huntTicks = data.huntDuration().sample(level.getRandom());

		// Allow for non-EcologicalEntity targets if that happened for some reason
		if ((target instanceof EcologicalEntity ecologicalTarget)) {
			// Alert the prey of the hunt, and activate relevant effects
			ecologicalTarget.onBeingHunted(level, hunterFavored);
		}

		this.setShouldHunt(false);
		this.setHunting(true);
		this.setHuntTicks(huntTicks);
		this.setFavoredInHunt(hunterFavored);
		this.applyHuntSpeeds(hunterFavored);
	}

	/**
	 * Main method called for getting included in a hunt as the prey - called from the predator who started the hunt.<br><br>
	 * <p>
	 * Applies hunt's favor buffs/debuffs.
	 *
	 * @see dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.FleePredatorsGoal
	 */
	default void onBeingHunted(Level world, boolean hunterFavored) {
		this.setBeingHunted(true);
		this.setFavoredInHunt(!hunterFavored);
		this.applyHuntSpeeds(!hunterFavored);
	}

	// FIXME - Figure out how we want to do movement speed changes for fish,
	//  or leave the mechanic scrapped for now
	default void applyHuntSpeeds(boolean selfIsFavored) {
		// FIXME - Sometimes entities crash with the custom attributes modifier
		// FIXME - Movement speed increases doesn't do anything for the fish
		// FIXME - Hunters give up on hunting prey too easily because they get too far away too fast (partially above issue)
		// FIXME - Fish repopulating can't get close enough to their chosen mate because they don't speed up
//		EcosystemType<?> ecosystemType = this.getEcosystemType();
//		MobEntity self = (MobEntity) this;
//		EntityAttributeInstance speedAttributeInstance = self.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
//		if (speedAttributeInstance != null) {
//			if(selfIsFavored) {
//				float favoredSpeed = ecosystemType.favoredHuntSpeed();
//				speedAttributeInstance.addTemporaryModifier(AbysmEntityAttributeModifiers.ofFavoredSpeed(favoredSpeed));
//			} else {
//				float unfavoredSpeed = ecosystemType.unfavoredHuntSpeed();
//				speedAttributeInstance.addTemporaryModifier(AbysmEntityAttributeModifiers.ofUnfavoredSpeed(unfavoredSpeed));
//			}
//		}
	}

	/**
	 * Main method called for ending hunts, as either the target prey or hunter predator. <br><br>
	 * <p>
	 * Resets hunt related fields.<br><br>
	 */
	default void onHuntEnd() {
		this.setHunting(false);
		this.setBeingHunted(false);
		this.setHuntTicks(0);
		this.setFavoredInHunt(false);
//
//		MobEntity self = (MobEntity) this;
//		EntityAttributeInstance speedAttributeInstance = self.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
//		speedAttributeInstance.removeModifier(AbysmEntityAttributeModifiers.FAVORED_HUNT_SPEED_MODIFIER_ID);
//		speedAttributeInstance.removeModifier(AbysmEntityAttributeModifiers.UNFAVORED_HUNT_SPEED_MODIFIER_ID);
	}

	default boolean shouldFailHunt() {
		return this.getHuntTicks() <= 0;
	}

	default boolean canBreedAndRepopulate() {
		return this.shouldRepopulate() && this.canBreed();
	}

	default boolean canBreed() {
		Mob self = ((Mob) this);
		boolean alive = self.isAlive();
		boolean beingHunted = this.isBeingHunted();
		int breedTicks = this.getBreedTicks();
		int breedCooldownTicks = this.getEcosystemData().breedingCooldown().sample(self.getRandom());
		return breedTicks >= breedCooldownTicks && !beingHunted && alive;
	}

	/**
	 * Main method called for breeding this entity with another.<br><br>
	 *
	 * @see dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.RepopulateGoal
	 */
	default void breed(ServerLevel world, Mob other) {
		this.breed(world, other, false);
	}

	default void breed(ServerLevel level, Mob other, boolean overrideCanBreed) {
		if (!overrideCanBreed && !canBreed()) return;

		Mob self = (Mob) this;

		EcosystemData ecosystemData = this.getEcosystemData();
		int amount = ecosystemData.breedingCooldown().sample(self.getRandom());

		for (int i = 0; i < amount; i++) {
			spawnChildEntity(level, other);
		}

		this.setBreedTicks(0);
		this.setShouldRepopulate(false);
		new HappyEntityParticlesS2CPayload(self, ParticleTypes.HEART, 7).send(self);
	}

	/**
	 * Creates and spawns a child entity into the world.
	 *
	 * @param world The world to spawn the entity into (assumed to be the same world as this entity)
	 * @param other The other entity this entity is breeding with - given to {@link EcologicalEntity#createChildEntity(ServerLevel, LivingEntity, BlockPos)}.
	 */
	default void spawnChildEntity(ServerLevel world, LivingEntity other) {
		LivingEntity self = (LivingEntity) this;
		Entity child = this.createChildEntity(world, other, self.blockPosition());
		if (child == null) return;

		// TODO - Should we have any entities that can be bred by the player,
		//  advancement/statistic credit needs to be given here
		if (child instanceof Mob mob) mob.setBaby(true);
		child.snapTo(self.getX(), self.getY(), self.getZ(), 0f, 0f);
		world.addFreshEntity(child);
	}

	/**
	 * Overridable method for creating the MobEntity used for spawning children. <br><br>
	 * <p>
	 * Use the "other" param entity for mixing and matching whatever data wanted(e.g. choosing between one of the two parent's variants)
	 *
	 * @param world The world the child is spawning into
	 * @param other The other parent entity - use to mix and match whatever data wanted
	 * @return The created child entity
	 */
	default Entity createChildEntity(ServerLevel world, LivingEntity other, BlockPos spawnPos) {
		Entity child = ((Mob) this).getType().create(world, EntitySpawnReason.BREEDING);
		if (child == null) return null;
		child.snapTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0f, 0f);
		if (child instanceof Mob mob)
			mob.finalizeSpawn(world, world.getCurrentDifficultyAt(spawnPos), EntitySpawnReason.BREEDING, null);

		return child;
	}

	@SuppressWarnings("unused")
	static <T extends Mob> boolean canSpawnInEcosystem(EntityType<T> type, LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		return true;
	}

	// EcosystemLogic getters/setters - overridable for custom behaviours if wanted

	/**
	 * @return If an EcosystemArea has tasked this entity to hunt.
	 */
	default boolean shouldHunt() {
		return this.getEcosystemLogic().shouldHunt();
	}

	/**
	 * Called when the current EcosystemArea allows this entity to hunt based on nearby population data, or when this entity succeeds/fails at its hunt.
	 */
	default void setShouldHunt(boolean shouldHunt) {
		this.getEcosystemLogic().setShouldHunt(shouldHunt);
	}

	/**
	 * @return If an EcosystemArea has tasked this entity to repopulate.
	 */
	default boolean shouldRepopulate() {
		return this.getEcosystemLogic().shouldRepopulate();
	}

	/**
	 * Called when the current EcosystemArea allows this entity to repopulate based on nearby population data, or after this entity has breed.
	 */
	default void setShouldRepopulate(boolean shouldRepopulate) {
		this.getEcosystemLogic().setShouldRepopulate(shouldRepopulate);
	}

	/**
	 * @return If an EcosystemArea has tasked this entity to scavenge for plants (mostly determined randomly for now).
	 */
	default boolean shouldScavenge() {
		return this.getEcosystemLogic().shouldScavenge();
	}

	/**
	 * Called when the current EcosystemArea allows this entity to scavenge for plants, or after this entity has finished eating plants.
	 */
	default void setShouldScavenge(boolean shouldScavenge) {
		this.getEcosystemLogic().setShouldScavenge(shouldScavenge);
	}


	default boolean isHunting() {
		return this.getEcosystemLogic().isHunting();
	}

	default void setHunting(boolean hunting) {
		this.getEcosystemLogic().setHunting(hunting);
	}

	default boolean isBeingHunted() {
		return this.getEcosystemLogic().isBeingHunted();
	}

	default void setBeingHunted(boolean beingHunted) {
		this.getEcosystemLogic().setBeingHunted(beingHunted);
	}

	default int getHuntTicks() {
		return this.getEcosystemLogic().getHuntTicks();
	}

	default void setHuntTicks(int huntTicks) {
		this.getEcosystemLogic().setHuntTicks(huntTicks);
	}

	default boolean isFavoredInHunt() {
		return this.getEcosystemLogic().isFavoredInHunt();
	}

	default void setFavoredInHunt(boolean favoredInHunt) {
		this.getEcosystemLogic().setFavoredInHunt(favoredInHunt);
	}

	default int getBreedTicks() {
		return this.getEcosystemLogic().getBreedTicks();
	}

	default void setBreedTicks(int breedTicks) {
		this.getEcosystemLogic().setBreedTicks(breedTicks);
	}

}
