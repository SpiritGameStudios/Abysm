package dev.spiritstudios.abysm.entity.ecosystem;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

import java.util.List;

public interface EcologicalEntity {
	// TODO - extra interfaces extending this one for herbivores(auto return empty on prey) & carnivores(return empty on plant)
	List<Class<? extends LivingEntity>> definePredators();

	List<Class<? extends LivingEntity>> definePrey();

	List<Block> definePlants();

	EcosystemLogic getEcosystemLogic();

	default void tickEcosystemLogic() {
		this.getEcosystemLogic().tick();
	}

	default EcosystemLogic createEcosystemLogic(MobEntity entity) {
		return this.createEcosystemLogicBuilder(entity).build();
	}

	default EcosystemLogic createEcosystemLogic(MobEntity entity, int huntHungerLevel, int desperateHuntHungerLevel, int scavengeHungerLevel, int desperateScavengeHungerLevel) {
		return this.createEcosystemLogicBuilder(entity)
			.setHuntLevel(huntHungerLevel)
			.setDesperateHuntLevel(desperateHuntHungerLevel)
			.setScavengeLevel(scavengeHungerLevel)
			.setDesperateScavengeLevel(desperateScavengeHungerLevel)
			.build();
	}

	default EcosystemLogic createEcosystemLogic(MobEntity entity, int huntHungerLevel, int desperateHuntHungerLevel, int scavengeHungerLevel, int desperateScavengeHungerLevel, int maxHungerLevel) {
		return this.createEcosystemLogicBuilder(entity)
			.setHuntLevel(huntHungerLevel)
			.setDesperateHuntLevel(desperateHuntHungerLevel)
			.setScavengeLevel(scavengeHungerLevel)
			.setDesperateScavengeLevel(desperateScavengeHungerLevel)
			.setMaxHungerLevel(maxHungerLevel)
			.build();
	}

	default EcosystemLogicBuilder createEcosystemLogicBuilder(MobEntity entity) {
		return new EcosystemLogicBuilder(entity, this.definePredators(), this.definePrey(), this.definePlants());
	}

}
