package dev.spiritstudios.abysm.ecosystem.entity;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface PlantEater extends EcologicalEntity {

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	boolean hasPlant();

	void clearPlantPos();

	@Nullable
	BlockPos getPlantPos();

	default void resetTicksUntilHunger() {
		this.setTicksUntilHunger(0);
	}

	/**
	 * It is recommended for implementations to take a maximum of the current ticks until hunger and the provided value
	 * @param ticks Minecraft ticks
	 * @see dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FindPlantsGoal
	 * @see dev.spiritstudios.abysm.entity.ruins.LectorfinEntity#setTicksUntilHunger(int)
	 */
	void setTicksUntilHunger(int ticks);

	int ticksUntilHunger();

	@Override
	default boolean isHungry() {
		return EcologicalEntity.super.isHungry() || this.ticksUntilHunger() <= 0;
	}

	default void setNotHungryAnymoreYay() {
		// I'm not insane
		this.setTicksUntilHunger(3600);
	}
}
