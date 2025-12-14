package dev.spiritstudios.abysm.world.ecosystem.chunk;

import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class EcosystemArea {
	// TODO - Sometimes task entities with eating plants, if they can

	// TODO - Tweak these numbers
	public static final int MAX_TASK_WAIT_TICKS = 600; // 30 seconds
	public static final int MIN_TASK_WAIT_TICKS = 200; // 10 seconds
	public static final int FORCE_TASK_TICKS = 1200; // 60 seconds
	// When giving an EcosystemType a task, chance of tasking another entity with eating plants
	public static final float SCAVENGE_CHANCE = 0.05f;

	public final ServerLevel level;
	public final EcosystemAreaPos pos;
	public final Map<EntityType<?>, PopInfo> populations = new Object2ObjectOpenHashMap<>();

	public int taskTicks = 0; // ticks until task
	public int ticksUntilForceTask = 0; // ticks until a task is forced - reset every time a task is successfully created

	public EcosystemArea(ServerLevel world, EcosystemAreaPos pos) {
		this.level = world;
		this.pos = pos;
	}

	public void addEntity(Mob entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		PopInfo popInfo = getOrCreatePopInfo(entity.getType());
		popInfo.addEntity(entity);
	}

	public void removeEntity(Mob entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		PopInfo popInfo = getOrCreatePopInfo(entity.getType());
		popInfo.removeEntity(entity);
	}

	@SuppressWarnings("unused")
	public void tick(ServerLevel serverWorld, EcosystemAreaPos pos) {
		this.taskTicks--;
		this.ticksUntilForceTask--;
		if (taskTicks <= 0 || ticksUntilForceTask <= 0) {
			this.createTasks();
		}
	}

	/**
	 * Creates tasks to hand out to EcosystemType(s) within this EcosystemArea. The type of task is determined using nearby population data via {@link EcosystemArea#getNearbyPopulationSize(EntityType)}, or forced if none have been given out in a while.
	 */
	public void createTasks() {
		this.taskTicks = this.level.random.nextIntBetweenInclusive(MIN_TASK_WAIT_TICKS, MAX_TASK_WAIT_TICKS);

		// Could be influenced by the amount of residing EcosystemTypes?
		int tasks = this.level.getRandom().nextIntBetweenInclusive(1, 3);
		// Forcing the task forces a random EcosystemType to get hunted and reproduce despite population numbers
		// This was done to create action if nothing happens for too long
		boolean force = this.ticksUntilForceTask <= 0;
		if (force) this.ticksUntilForceTask = 100; // If no tasks were successfully forced, try again in 5 seconds

		for (int i = 0; i < tasks; i++) {
			this.createSingleTask(force);
		}
	}

	/**
	 * Creates a single task for a randomly chosen EcosystemType from this EcosystemArea
	 */
	public void createSingleTask(boolean force) {
		// Choose random EcosystemType for what the task will be about (hunt or reproduce said EcosystemType)
		PopInfo targetPop = this.randomPop();
		if (targetPop == null) return;

		int targetPopSize = getNearbyPopulationSize(targetPop.type);
		boolean maintained = targetPopSize >= targetPop.data.targetPopulation();

		if (force || maintained) {
			// Hunt this EcosystemType - choose random predator to accept the task
			this.huntEcosystemType(targetPop.data);
		}

		if (force || !maintained) {
			// Reproduce this EcosystemType - choose random entity of said EcosystemType to accept the task
			this.reproduceEcosystemType(targetPop.data);
		}

		boolean canScavenge = !targetPop.data.plants().isEmpty();
		if (!canScavenge) return;

		// don't worry about the force variable, because this is mostly a visual thing
		// Commented out for now because of bugs
//		boolean scavenge = this.world.getRandom().nextFloat() <= SCAVENGE_CHANCE;
//		if(scavenge) {
//			this.scavengeEcosystemType(targetEcosystemType);
//		}
	}

	public void huntEcosystemType(EcosystemData ecosystemData) {
		// Choose random predator in this EcosystemArea to accept the task
		Set<EntityType<?>> predators = ecosystemData.predators();
		if (predators.isEmpty()) return;

		// I don't know why I can't use List<EcosystemType<?>> and Optional#get() on the map function, but the types were messing up in a way I don't know how
		// I was able to do it individually for the first entry of the filtered List, so idk why it wouldn't work for the map function either ¯\(°_o)/¯
		List<Optional<EcosystemData>> predatorTypes = predators.stream()
			.filter(this::containsEntityType)
			.map(type -> type.getData(AbysmMetatags.ECOSYSTEM_DATA))
			.toList();
		if (predatorTypes.isEmpty()) return;

		int index = this.level.getRandom().nextInt(predatorTypes.size());
		predatorTypes.get(index).ifPresent(hunterType -> {
			Mob hunter = this.getRandomEntity(hunterType);
			if (!(hunter instanceof EcologicalEntity ecologicalHunter)) return;

			// Allow the hunter to begin searching for nearby prey
			// TODO - Specify huntablePrey EcosystemTypes for predators
			ecologicalHunter.setShouldHunt(true);
			// hunter.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
			this.resetForceTaskTicks();
		});
	}

	public void reproduceEcosystemType(EcosystemData ecosystemData) {
		// Choose random entity in this EcosystemArea to accept the task
		Mob entity = this.getRandomEntity(ecosystemData);
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		// Allow the entity to begin searching for a nearby mate
		ecologicalEntity.setShouldRepopulate(true);
//		entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
		this.resetForceTaskTicks();
	}

	public void scavengeEcosystemType(EcosystemData ecosystemData) {
		// Choose random entity in this EcosystemArea to accept the task
		Mob entity = this.getRandomEntity(ecosystemData);
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		// Allow the entity to begin searching for a nearby mate
		ecologicalEntity.setShouldScavenge(true);
//		entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
		this.resetForceTaskTicks();
	}

	private void resetForceTaskTicks() {
		this.ticksUntilForceTask = FORCE_TASK_TICKS;
	}

	/**
	 * @return Get the number of entities of this EcosystemType based on this AND adjacent(including corners) EcosystemAreas.
	 */
	public int getNearbyPopulationSize(EntityType<?> type) {
		return getNearbyPopulationIds(type).size();
	}

	public IntList getNearbyPopulationIds(EntityType<?> type) {
		int searchRadius = 1;
		AtomicReference<IntList> atomicEntityIds = new AtomicReference<>(new IntArrayList());

		EcosystemAreaManager ecosystemAreaManager = EcosystemAreaManager.getEcosystemAreaManagerForWorld(this.level);
		EcosystemAreaPos.stream(this.pos, searchRadius).forEach(areaPos -> {
			if (!ecosystemAreaManager.containsArea(areaPos)) return;

			EcosystemArea area = ecosystemAreaManager.getEcosystemArea(areaPos);
			PopInfo popInfo = area.getPopInfo(type, false);
			if (popInfo == null) return;

			atomicEntityIds.get().addAll(popInfo.getEntityIds());
		});

		return atomicEntityIds.get();
	}

	/**
	 * @return A random EcosystemType that resides in this EcosystemArea with at least one entity, or null if none are residing here.
	 */
	public @Nullable PopInfo randomPop() {
		var residingTypes = getResidingEcosystemTypes().toList();

		if (residingTypes.isEmpty()) return null;

		var entry = Util.getRandom(residingTypes, level.random);
		return entry.getValue();
	}

	/**
	 * @return All the EcosystemTypes that have at least one entity within this EcosystemArea.
	 */
	public Stream<Map.Entry<EntityType<?>, PopInfo>> getResidingEcosystemTypes() {
		return populations.entrySet().stream()
			.filter(entry -> !entry.getValue().isEmpty());
	}

	/**
	 * @return A random MobEntity of the given EcosystemType that is within this EcosystemArea.
	 */
	@Nullable
	public Mob getRandomEntity(EcosystemData ecosystemData) {
		PopInfo popInfo = this.populations.get(ecosystemData);
		if (popInfo == null || popInfo.isEmpty()) return null;

		int[] entityIds = popInfo.getEntityIds().intStream().toArray();
		int index = this.level.getRandom().nextInt(entityIds.length);
		int entityId = entityIds[index];
		return (Mob) this.level.getEntity(entityId);
	}

	/**
	 * @return If this EcosystemArea contains the given EntityType.
	 */
	public boolean containsEntityType(EntityType<?> entityType) {
		return this.populations.containsKey(entityType);
	}

	public PopInfo getOrCreatePopInfo(EntityType<?> type) {
		return this.getPopInfo(type, true);
	}

	@Nullable
	public PopInfo getPopInfo(EntityType<?> type, boolean createIfEmpty) {
		if (createIfEmpty) {
			return this.populations.computeIfAbsent(
				type,
				PopInfo::new
			);
		}

		return this.populations.getOrDefault(type, null);
	}

	/**
	 * The EcosystemType Population information of a single EcosystemType in
	 * <b>a specific {@link EcosystemArea}</b>. Does <b>NOT</b> account for
	 * nearby ChunkAreas.
	 *
	 * <br><br>
	 * <p>
	 * Holds the Entity int ids for all of this EcosystemType within this ChunkArea,
	 * methods for adding/removing entities, and extra getter methods.
	 */
	public static class PopInfo {
		public final EcosystemData data;
		public final EntityType<?> type;
		public final IntOpenHashSet entityIds = new IntOpenHashSet();

		public PopInfo(EntityType<?> type) {
			this.data = type.getData(AbysmMetatags.ECOSYSTEM_DATA)
				.orElseThrow(() -> new RuntimeException("No ecodata found for entity '" + type.getDescriptionId() + "'"));
			this.type = type;
		}

		public void addEntity(Mob entity) {
			this.entityIds.add(entity.getId());
		}

		public void removeEntity(Mob entity) {
			this.entityIds.remove(entity.getId());
		}

		public int getEntityCount() {
			return this.entityIds.size();
		}

		public boolean isEmpty() {
			return this.entityIds.isEmpty();
		}

		// I wonder if it'd be worth changing this to an IntArrayList
		// and manually checking for duplicates in the add method?
		public IntSet getEntityIds() {
			return entityIds;
		}
	}
}
