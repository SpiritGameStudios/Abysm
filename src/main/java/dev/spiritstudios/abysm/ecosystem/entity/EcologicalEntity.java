package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.EcosystemTypes;
import net.minecraft.entity.mob.MobEntity;

// Contains methods for handling all entity-related logic that all Ecosystem-related classes may need to call(e.g. EcosystemChunk)
public interface EcologicalEntity {

	/**
	 * @return This Entity's created EcosystemLogic. Used for managing Ecosystem related code for Entity actions.
	 * @see	EcologicalEntity#createEcosystemLogic(MobEntity)
	 */
	EcosystemLogic getEcosystemLogic();

	/**
	 * @return This Entity's {@link EcosystemType}. Defined in a non-data-driven Registry, used for knowing an Entity's Ecosystem-related info, such as predators, prey, plants, and more.
	 * @see EcosystemType
	 * @see EcosystemTypes
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
	 * Tick this Entity's {@link EcosystemLogic}, used for handling & manging various Ecosystem related things.
	 */
	default void tickEcosystemLogic() {
		this.getEcosystemLogic().tick();
	}
}
