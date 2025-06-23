package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import net.minecraft.entity.mob.MobEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Handles all entities within a specific chunk, accounting for adjacent chunks too.
// This class will tell entities how they should be feeling based on the data available.

// If there's plenty of common prey available, common predators will be told to feel hungry and start hunting,
// and if there's too little common prey, the prey will be told to start repopulating and common predators will be less hungry.
// The current idea for this implementation is that the EcosystemLogic class will have signals(booleans methods?) available saying how
// the entity should be feeling, and this class will enable/disable those signals as needed.
// Each EcologicalEntity can then use those signals as desired, either with a brain or AI goals.
// Or, ignore them completely(the giant leviathans probably won't care about their EntityType population)

// As it stands, I'm unsure how much of this data will be persistent upon world close & rejoin.
// All of this data is pretty easy to regain very quickly after reloading without persistence,
// but it could help to have some numbers retained (e.g. EcosystemType population numbers per chunk)
// just to help reduce false alarms(e.g. possibly throwing off the ecosystem because of a fake low number of x entity)
public class EcosystemChunk {
	public final Map<EcosystemType<?>, PopInfo> entityPopulation = new HashMap<>();

	public void onEntityEnter(MobEntity entity) {
		if(!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getPopInfo(type);
		popInfo.addEntity(entity);
	}

	public void onEntityLeave(MobEntity entity) {
		if(!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getPopInfo(type);
		popInfo.removeEntity(entity);
	}

	public void onEntitySpawn(MobEntity entity) {

	}

	public void onEntityDeath(MobEntity entity) {

	}

	public PopInfo getPopInfo(EcosystemType<?> type) {
		return this.entityPopulation.getOrDefault(type, new PopInfo(type));
	}

	public static class PopInfo {
		public final EcosystemType<?> type;
		public List<MobEntity> entities = new ArrayList<>();

		public PopInfo(EcosystemType<?> type) {
			this.type = type;
		}

		public void addEntity(MobEntity entity) {
			this.entities.add(entity);
		}

		public void removeEntity(MobEntity entity) {
			this.entities.remove(entity);
		}

		public int getEntityCount() {
			return entities.size();
		}

		// Name pending
		public boolean populationOkay() {
			return this.getEntityCount() >= this.type.getPopulationPerChunk();
		}
	}

}
