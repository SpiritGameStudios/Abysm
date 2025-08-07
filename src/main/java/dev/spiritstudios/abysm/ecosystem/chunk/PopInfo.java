package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.entity.mob.MobEntity;

/**
 * The EcosystemType Population information of a single EcosystemType in <b>a specific {@link EcosystemArea}</b>. Does <b>NOT</b> account for nearby ChunkAreas.<br><br>
 * Holds the Entity int ids for all of this EcosystemType within this ChunkArea, methods for adding/removing entities, and extra getter methods.
 */
public class PopInfo {
	public final EcosystemType<?> type;
	public final IntSet entityIds = new IntOpenHashSet();

	public PopInfo(EcosystemType<?> type) {
		this.type = type;
	}

	public void addEntity(MobEntity entity) {
		this.entityIds.add(entity.getId());
	}

	public void removeEntity(MobEntity entity) {
		this.entityIds.remove(entity.getId());
	}

	public int getEntityCount() {
		return this.entityIds.size();
	}

	public boolean popMaintained() {
		return this.getEntityCount() >= this.type.targetPopulation();
	}

	public IntSet getEntityIds() {
		return entityIds;
	}
}
