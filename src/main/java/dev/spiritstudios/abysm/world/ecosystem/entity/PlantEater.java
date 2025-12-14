package dev.spiritstudios.abysm.world.ecosystem.entity;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface PlantEater extends EcologicalEntity {

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	default boolean hasPlant() {
		return this.getPlantPos() != null;
	}

	void setPlantPos(@Nullable BlockPos pos);

	void clearPlantPos();

	@Nullable
	BlockPos getPlantPos();

	default void resetTicksUntilHunger() {
		this.setTicksUntilHunger(0);
	}

	/**
	 * It is recommended for implementations to take a maximum of the current ticks until hunger and the provided value
	 *
	 * @param ticks Minecraft ticks
	 * @see dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.FindPlantsGoal
	 * @see dev.spiritstudios.abysm.world.entity.ruins.LectorfinEntity#setTicksUntilHunger(int)
	 */
	void setTicksUntilHunger(int ticks);

	int ticksUntilHunger();

	default boolean isHungryHerbivore() {
		return this.ticksUntilHunger() <= 0;
	}

	default void setNotHungryAnymoreYay() {
		// I'm not insane
		this.setTicksUntilHunger(3600);
	}
}
