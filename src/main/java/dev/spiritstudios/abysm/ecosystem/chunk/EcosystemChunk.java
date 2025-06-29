package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

// TODO - look over this code for performance/optimizations once we're happy with a system,
//  because I've really struggled mentally breaking this system down into smaller problems
public class EcosystemChunk {
	public final World world;
	public final ChunkPos pos;
	public final Map<EcosystemType<?>, PopInfo> entityPopulation = new HashMap<>();

	public EcosystemChunk(World world, ChunkPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public void onEntityEnter(MobEntity entity) {
		this.addEntity(entity);
	}

	public void onEntityLeave(MobEntity entity) {
		this.removeEntity(entity);
	}

	// Assumed to happen before the chunk enter method - this entity technically gets added twice but PopInfo uses a Set so whatever
	public void onEntitySpawn(MobEntity entity) {
		this.addEntity(entity);
		this.handlePopIncrease(entity);
	}

	// Assumed to happen before the chunk leave method, and to be the last method called by the entity(meaning no chunk leave)
	public void onEntityDeath(MobEntity entity) {
		this.removeEntity(entity);
		this.handlePopDecrease(entity);
	}

	public void addEntity(MobEntity entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getPopInfo(type);
		popInfo.addEntity(entity);
	}

	public void removeEntity(MobEntity entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getPopInfo(type);
		popInfo.removeEntity(entity);
	}

	public void handlePopIncrease(MobEntity entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;
		boolean popOkay = this.entityNearbyPopOkay(ecologicalEntity);

		if (popOkay) {
			// Choose random (closet?) predator to start hunting

		} else {
			// Notify self type to repopulate

		}
	}

	public void handlePopDecrease(MobEntity entity) {

	}


	@SuppressWarnings("UnstableApiUsage")
	public boolean entityNearbyPopOkay(EcologicalEntity entity) {
		EcosystemType<?> ecosystemType = entity.getEcosystemType();

		int chunkSearchRadius = ecosystemType.populationChunkSearchRadius();
		int totalAmount = 0;

		for (ChunkPos chunkPos : ChunkPos.stream(this.pos, chunkSearchRadius).toList()) {
			// Don't create new EcosystemChunk during search to possibly help reduce created data
			Chunk chunk = this.world.getChunk(chunkPos.x, chunkPos.z);
			if (!accountChunkForPopCount(chunk)) continue;

			if (!chunk.hasAttached(AbysmAttachments.ECOSYSTEM_CHUNK)) continue;
			EcosystemChunk ecosystemChunk = chunk.getAttached(AbysmAttachments.ECOSYSTEM_CHUNK);
			if (ecosystemChunk == null) continue;
			PopInfo info = ecosystemChunk.getPopInfo(ecosystemType, false);
			if (info == null) continue;

			totalAmount += info.getEntityCount();
		}

		int targetPopulation = ecosystemType.targetPopulation();

		return totalAmount >= targetPopulation;
	}

	private boolean accountChunkForPopCount(Chunk chunk) {
		int seaLevel = world.getSeaLevel();
		BlockPos centerPos = chunk.getPos().getCenterAtY(seaLevel - 5);
		RegistryEntry<Biome> biome = world.getBiome(centerPos);

		// Don't include vanilla biomes for now because it's safe to assume we're only checking for Abysm entities
		return biome.isIn(ConventionalBiomeTags.IS_OCEAN) || biome.isIn(ConventionalBiomeTags.IS_DEEP_OCEAN) || biome.isIn(ConventionalBiomeTags.IS_SHALLOW_OCEAN);
//		return (biome.isIn(ConventionalBiomeTags.IS_OCEAN) || biome.isIn(BiomeTags.IS_OCEAN) || biome.isIn(BiomeTags.IS_DEEP_OCEAN) || biome.isIn(BiomeTags.IS_RIVER));
	}

	public PopInfo getPopInfo(EcosystemType<?> type) {
		return this.getPopInfo(type, true);
	}

	@Nullable
	public PopInfo getPopInfo(EcosystemType<?> type, boolean createIfEmpty) {
		if (createIfEmpty) return this.entityPopulation.computeIfAbsent(type, PopInfo::new);
		return this.entityPopulation.getOrDefault(type, null);
	}

	private Box getEntitySearchBox(MobEntity entity) {
		return entity.getBoundingBox().expand(getEntityFollowRange(entity));
	}

	private Box getEntitySearchBox(MobEntity entity, double distance) {
		return entity.getBoundingBox().expand(distance);
	}

	private double getEntityFollowRange(MobEntity entity) {
		return entity.getAttributeValue(EntityAttributes.FOLLOW_RANGE);
	}

	public static class PopInfo {
		public final EcosystemType<?> type;
		public final Set<Integer> entityIds = new IntOpenHashSet();

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

		public boolean populationOkay() {
			return this.getEntityCount() >= this.type.targetPopulation();
		}
	}

}
