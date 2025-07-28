package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

// Handles all entities within a specific chunk, accounting for adjacent chunks too.
// This class keeps track of how many EcologicalEntities are within a chunk,
// tells them how to feel & what to do based on available data(accounts for adjacent chunks),
// as well as keep that data persistent across world reloads.

// If there's plenty of common prey available, common predators will be told to feel hungry and start hunting,
// and if there's too little common prey, the prey will be told to start repopulating and common predators will be less hungry.
// The current idea for this implementation is that the EcosystemLogic class will have signals(booleans methods?) available saying how
// the entity should be feeling, and this class will enable/disable those signals as needed.
// Each EcologicalEntity can then use those signals as desired, either with a brain or AI goals.
// Or, ignore them completely(the giant leviathans probably won't care about their EntityType population)

// Persistence handling:
// Persistent EcosystemChunks will keep track of the number of each EcosystemType in it,
// and will respawn that number of entities upon that chunk being reloaded by a player.
// This replaces having each individual entity persistent. But, we must account for entities that would normally
// be persistent, e.g. nametagged entities and entities near players upon world leave.

// Each EcosystemChunk will be checked for the biome (likely by checking the center block of the chunk).
// If it is an Abysm biome, the EcosystemChunk will be persistent.
// If not, the closest persistent EcosystemChunk will be found and given all the entity counts instead.

public class EcosystemChunk {
	public final World world;
	public final ChunkPos pos;
	public final Map<EcosystemType<?>, PopInfo> entityPopulation = new Object2ObjectOpenHashMap<>();

	public EcosystemChunk(World world, ChunkPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public void addEntity(MobEntity entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getPopInfo(type);
		popInfo.addEntity(entity);

		this.onPopChange(entity);
	}

	public void removeEntity(MobEntity entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getPopInfo(type);
		popInfo.removeEntity(entity);

		this.onPopChange(entity);
	}

	public void onPopChange(MobEntity entity) {
		if (!(this.world instanceof ServerWorld serverWorld)) return;
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;
		EcosystemType<?> ecosystemType = ecologicalEntity.getEcosystemType();
		boolean popOkay = this.ecosystemTypeNearbyPopOkay(ecosystemType);

		if (popOkay) {
			// A random nearby entity with the same EcosystemType should be hunted
		} else {
			// A random nearby entity with the same EcosystemType should repopulate
//			MobEntity repopulateTaskedEntity = getRandomNearbyEcologicalEntity(ecosystemType);
			ecologicalEntity.breed(serverWorld, entity);
		}
	}

	public MobEntity getRandomNearbyEcologicalEntity(EcosystemType<?> ecosystemType) {
		return null;
	}

	public boolean entityNearbyPopOkay(EcologicalEntity entity) {
		EcosystemType<?> ecosystemType = entity.getEcosystemType();
		return ecosystemTypeNearbyPopOkay(ecosystemType);
	}

	public boolean ecosystemTypeNearbyPopOkay(EcosystemType<?> ecosystemType) {
		int population = getNearbyEcosystemTypePopulation(ecosystemType);
		int targetPopulation = ecosystemType.targetPopulation();
		return population >= targetPopulation;
	}

	@SuppressWarnings("UnstableApiUsage")
	public int getNearbyEcosystemTypePopulation(EcosystemType<?> ecosystemType) {
		int chunkSearchRadius = ecosystemType.populationChunkSearchRadius();
		AtomicInteger totalAmount = new AtomicInteger();

		ChunkPos.stream(this.pos, chunkSearchRadius).forEach(chunkPos -> {
			// Don't create new EcosystemChunk during search to possibly help reduce created data
			Chunk chunk = this.world.getChunk(chunkPos.x, chunkPos.z);

			if (!chunk.hasAttached(AbysmAttachments.ECOSYSTEM_CHUNK)) return;

			EcosystemChunk ecosystemChunk = chunk.getAttached(AbysmAttachments.ECOSYSTEM_CHUNK);
			if (ecosystemChunk == null) return;
			PopInfo info = ecosystemChunk.getPopInfo(ecosystemType, false);
			if (info == null) return;

			totalAmount.addAndGet(info.getEntityCount());
		});

		return totalAmount.get();
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
		public final IntSet entityIds = new IntOpenHashSet();
		// TODO - Don't respawn nametagged entities

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
