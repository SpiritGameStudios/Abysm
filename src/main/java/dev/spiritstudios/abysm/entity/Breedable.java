package dev.spiritstudios.abysm.entity;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

/**
 * Requires implementing class to be of {@link MobEntity}
 */
/* help it's really bad what am I doing
   this is a half-unfinished garbage interface
*/
public interface Breedable {

	@Nullable
	Breedable createChild(ServerWorld serverWorld, Breedable other);

	default void breed(ServerWorld world, Breedable other) {
		MobEntity child = (MobEntity) this.createChild(world, other);
		if (child != null) {
			child.setBaby(true);
			MobEntity thisEntity = (MobEntity) this;
			child.refreshPositionAndAngles(thisEntity.getX(), thisEntity.getY(), thisEntity.getZ(), 0.0F, 0.0F);
			this.breed(world, other, (Breedable) child);
			world.spawnEntityAndPassengers(child);
		}
	}

	default void breed(ServerWorld world, Breedable other, @Nullable Breedable baby) {
		this.setBreedingAge(6000);
		other.setBreedingAge(6000);
		this.resetLoveTicks();
		other.resetLoveTicks();
		MobEntity thisEntity = (MobEntity) this;
		world.sendEntityStatus(thisEntity, EntityStatuses.ADD_BREEDING_PARTICLES);
		if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
			world.spawnEntity(new ExperienceOrbEntity(world, thisEntity.getX(), thisEntity.getY(), thisEntity.getZ(), thisEntity.getRandom().nextInt(7) + 1));
		}
	}

	void resetLoveTicks();

	void setBreedingAge(int age);
}
