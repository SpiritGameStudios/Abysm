package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
		PopStatus popStatus = getPopStatus(ecosystemType);

		if(popStatus.needsRepopulating()) {
			this.repopulateEcosystemType(ecosystemType, popStatus.repopulatingIsPriority());
		}

		if(popStatus.needsHunting()) {
			this.huntEcosystemType(ecosystemType, popStatus.huntingIsPriority());
		}
	}

	public void repopulateEcosystemType(EcosystemType<?> ecosystemType, boolean priority) {
		// Random number of this ecosystem type is tasked with breeding with a nearby entity
		MobEntity repopulateEntity = getRandomNearbyEcologicalEntity(ecosystemType);
		if (!(repopulateEntity instanceof EcologicalEntity repopulateEcologicalEntity)) return;
		repopulateEcologicalEntity.setShouldRepopulate(true);
	}

	public void huntEcosystemType(EcosystemType<?> ecosystemType, boolean priority) {
		// Random predators of this ecosystem type is tasked with hunting a nearby entity of this type
		int hunters = 1;
		if(priority) {
			hunters = MathHelper.clamp((ecosystemType.overpopulationMark() - ecosystemType.targetPopulation()) / 2, 2, 10);
		}
	}

	@Nullable
	public MobEntity getRandomNearbyEcologicalEntity(EcosystemType<?> ecosystemType) {
		return getRandomNearbyEcologicalEntity(ecosystemType, 5);
	}

	@Nullable
	public MobEntity getRandomNearbyEcologicalEntity(EcosystemType<?> ecosystemType, int searchAttempts) {
		IntList entityIds = getNearbyEcosystemTypeIds(ecosystemType);
		if (entityIds.isEmpty()) return null;

		int size = entityIds.size();
		for (int i = 0; i < searchAttempts; i++) {
			int index = this.world.getRandom().nextInt(size);
			int entityId = entityIds.getInt(index);
			MobEntity entity = (MobEntity) this.world.getEntityById(entityId);
			if(entity == null || entity.isDead()) continue;

			return entity;
		}

		return null;
	}

	// Get the Population Status of this EcosystemType based on this chunk's & nearby chunks' data
	public PopStatus getPopStatus(EcosystemType<?> ecosystemType) {
		int nearbyPopulation = getNearbyEcosystemTypePopulation(ecosystemType);
		return PopStatus.getStatusWithType(ecosystemType, nearbyPopulation);
	}

	public boolean ecosystemTypeNearbyPopOkay(EcosystemType<?> ecosystemType) {
		int population = getNearbyEcosystemTypePopulation(ecosystemType);
		int targetPopulation = ecosystemType.targetPopulation();
		return population >= targetPopulation;
	}

	public int getNearbyEcosystemTypePopulation(EcosystemType<?> ecosystemType) {
		return getNearbyEcosystemTypeIds(ecosystemType).size();
	}

	@SuppressWarnings("UnstableApiUsage")
	public IntList getNearbyEcosystemTypeIds(EcosystemType<?> ecosystemType) {
		int chunkSearchRadius = ecosystemType.populationChunkSearchRadius();
		AtomicReference<IntList> atomicEntityIds = new AtomicReference<>(new IntArrayList());

		ChunkPos.stream(this.pos, chunkSearchRadius).forEach(chunkPos -> {
			// Don't create new EcosystemChunk during search to possibly help reduce created data
			Chunk chunk = this.world.getChunk(chunkPos.x, chunkPos.z);

			if (!chunk.hasAttached(AbysmAttachments.ECOSYSTEM_CHUNK)) return;

			EcosystemChunk ecosystemChunk = chunk.getAttached(AbysmAttachments.ECOSYSTEM_CHUNK);
			if (ecosystemChunk == null) return;
			PopInfo info = ecosystemChunk.getPopInfo(ecosystemType, false);
			if (info == null) return;

			atomicEntityIds.get().addAll(info.getEntityIds());
		});

		return atomicEntityIds.get();
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

	/**
	 * The EcosystemType Population information of a single EcosystemType in <b>this specific chunk</b>. Does <b>NOT</b> account for nearby chunks.<br><br>
	 * Holds the Entity int ids for all of this EcosystemType within this chunk, methods for adding/removing entities, and extra getter methods.
	 */
	public static class PopInfo {
		public final EcosystemType<?> type;
		public final IntSet entityIds = new IntOpenHashSet();
		// TODO - Don't respawn already persistent entities

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

		public IntSet getEntityIds() {
			return entityIds;
		}
	}

	/**
	 * The status of a single EcosystemType, with this chunk in the center and <b>accounting for nearby chunks</b> based on the {@link EcosystemType#populationChunkSearchRadius()}.<br><br>
	 */
	public static enum PopStatus {
		EXTINCT, // No entities nearby
		NEAR_EXTINCT, // At or below near extinct mark - breeding becomes high priority and being hunted is disallowed
		UNDERPOPULATED, // Between extinct mark and target population - breeding still a priority but can now be hunted
		MAINTAINED, // At or above target population, and below overpopulation mark - breeding still allowed
		OVERPOPULATED; // At or above overpopulation mark - being hunted becomes priority and breeding is disallowed

		public static PopStatus getStatusWithType(EcosystemType<?> ecosystemType, int nearbyPopulation) {
			if(nearbyPopulation <= 0) return EXTINCT; // I'd be pretty concerned if it was less than 0 but backup I guess

			int nearExtinctMark = ecosystemType.nearExtinctMark();
			if(nearbyPopulation <= nearExtinctMark) return NEAR_EXTINCT;

			int target = ecosystemType.targetPopulation();
			if(nearbyPopulation < target) return UNDERPOPULATED;

			int overpopulationMark = ecosystemType.overpopulationMark();
			if(nearbyPopulation >= overpopulationMark) return OVERPOPULATED;

			return MAINTAINED;
		}

		// Doing this instead of having a big switch case in EcosystemChunk#onPopChange, because I think that's better?
		public boolean needsRepopulating() {
			return this.isNearExtinct() || this.isUnderpopulated();
		}

		public boolean repopulatingIsPriority() {
			return this.isNearExtinct();
		}

		public boolean needsHunting() {
			return this.isMaintained() || this.isOverpopulated();
		}

		public boolean huntingIsPriority() {
			return this.isOverpopulated();
		}

		// I think that's how you're supposed to do this stuff?
		public boolean isOverpopulated() {
			return this == OVERPOPULATED;
		}

		public boolean isMaintained() {
			return this == MAINTAINED;
		}

		public boolean isUnderpopulated() {
			return this == UNDERPOPULATED;
		}

		public boolean isNearExtinct() {
			return this == NEAR_EXTINCT;
		}

		public boolean isExtinct() {
			return this == EXTINCT;
		}
	}

}
