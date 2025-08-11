package dev.spiritstudios.abysm.ecosystem.chunk;

import com.google.common.collect.ImmutableSet;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class EcosystemArea {
	// TODO - Sometimes task entities with eating plants, if they can

	// TODO - Tweak these numbers
	public static final int MAX_TASK_WAIT_TICKS = 600; // 30 seconds
	public static final int MIN_TASK_WAIT_TICKS = 200; // 10 seconds
	public static final int FORCE_TASK_TICKS = 1200; // 60 seconds

	public final ServerWorld world;
	public final EcosystemAreaPos pos;
	public final Map<EcosystemType<?>, PopInfo> populations = new Object2ObjectOpenHashMap<>();

	public int taskTicks = 0; // ticks until task
	public int ticksUntilForceTask = 0; // ticks until a task is forced - reset every time a task is successfully created

	public EcosystemArea(ServerWorld world, EcosystemAreaPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public void addEntity(MobEntity entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getOrCreatePopInfo(type);
		popInfo.addEntity(entity);
	}

	public void removeEntity(MobEntity entity) {
		if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		EcosystemType<?> type = ecologicalEntity.getEcosystemType();
		PopInfo popInfo = getOrCreatePopInfo(type);
		popInfo.removeEntity(entity);
	}

	@SuppressWarnings("unused")
	public void tick(ServerWorld serverWorld, EcosystemAreaPos pos) {
		this.taskTicks--;
		this.ticksUntilForceTask--;
		if(taskTicks <= 0 || ticksUntilForceTask <= 0) {
			this.createTasks();
		}
	}

	/**
	 * Creates tasks to hand out to EcosystemType(s) within this EcosystemArea. The type of task is determined using nearby population data via {@link EcosystemArea#getNearbyPopulationSize(EcosystemType)}, or forced if none have been given out in a while.
	 */
	public void createTasks() {
		this.taskTicks = this.world.random.nextBetween(MIN_TASK_WAIT_TICKS, MAX_TASK_WAIT_TICKS);

		// Could be influenced by the amount of residing EcosystemTypes?
		int tasks = this.world.getRandom().nextBetween(1, 3);
		// Forcing the task forces a random EcosystemType to get hunted and reproduce despite population numbers
		// This was done to create action if nothing happens for too long
		boolean force = this.ticksUntilForceTask <= 0;
		if(force) this.ticksUntilForceTask = 100; // If no tasks were successfully forced, try again in 5 seconds

		for (int i = 0; i < tasks; i++) {
			this.createSingleTask(force);
		}
	}

	/**
	 * Creates a single task for a randomly chosen EcosystemType from this EcosystemArea
	 */
	public void createSingleTask(boolean force) {
		// Choose random EcosystemType for what the task will be about (hunt or reproduce said EcosystemType)
		EcosystemType<?> targetEcosystemType = this.getRandomEcosystemType();
		if(targetEcosystemType == null) return;

		int targetPopSize = getNearbyPopulationSize(targetEcosystemType);
		boolean maintained = targetPopSize >= targetEcosystemType.targetPopulation();

		if(force || maintained) {
			// Hunt this EcosystemType - choose random predator to accept the task
			this.huntEcosystemType(targetEcosystemType);
		}

		if(force || !maintained) {
			// Reproduce this EcosystemType - choose random entity of said EcosystemType to accept the task
			this.reproduceEcosystemType(targetEcosystemType);
		}
	}

	public void huntEcosystemType(EcosystemType<?> ecosystemType) {
		// Choose random predator in this EcosystemArea to accept the task
		ImmutableSet<EntityType<? extends MobEntity>> predators = ecosystemType.predators();
		if(predators.isEmpty()) return;

		// I don't know why I can't use List<EcosystemType<?>> and Optional#get() on the map function, but the types were messing up in a way I don't know how
		// I was able to do it individually for the first entry of the filtered List, so idk why it wouldn't work for the map function either ¯\(°_o)/¯
		List<Optional<EcosystemType<?>>> predatorTypes = predators.stream()
			.filter(this::containsEntityType).map(EcosystemType::get).toList();
		if(predatorTypes.isEmpty()) return;

		int index = this.world.getRandom().nextInt(predatorTypes.size());
		EcosystemType<?> hunterType = predatorTypes.get(index).get();
		MobEntity hunter = this.getRandomEntity(hunterType);
		if(!(hunter instanceof EcologicalEntity ecologicalHunter)) return;

		// Allow the hunter to begin searching for nearby prey
		// TODO - Specify huntablePrey EcosystemTypes for predators
//		ecologicalHunter.getEcosystemLogic().allowHunting();
		ecologicalHunter.setCanHunt(true);
		hunter.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
		this.resetForceTaskTicks();
	}

	public void reproduceEcosystemType(EcosystemType<?> ecosystemType) {
		// Choose random entity in this EcosystemArea to accept the task
		MobEntity entity = this.getRandomEntity(ecosystemType);
		if(!(entity instanceof EcologicalEntity ecologicalEntity)) return;

		// Allow the entity to begin searching for a nearby mate
		ecologicalEntity.setCanRepopulate(true);
		entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
		this.resetForceTaskTicks();
	}

	private void resetForceTaskTicks() {
		this.ticksUntilForceTask = FORCE_TASK_TICKS;
	}

	/**
	 * @return Get the number of entities of this EcosystemType based on this AND adjacent(including corners) EcosystemAreas.
	 */
	public int getNearbyPopulationSize(EcosystemType<?> ecosystemType) {
		return getNearbyPopulationIds(ecosystemType).size();
	}

	public IntList getNearbyPopulationIds(EcosystemType<?> ecosystemType) {
		int searchRadius = 1;
		AtomicReference<IntList> atomicEntityIds = new AtomicReference<>(new IntArrayList());

		EcosystemAreaManager ecosystemAreaManager = EcosystemAreaManager.getEcosystemAreaManagerForWorld(this.world);
		EcosystemAreaPos.stream(this.pos, searchRadius).forEach(areaPos ->  {
			if(!ecosystemAreaManager.containsArea(areaPos)) return;

			EcosystemArea area = ecosystemAreaManager.getEcosystemArea(areaPos);
			PopInfo popInfo = area.getPopInfo(ecosystemType, false);
			if(popInfo == null) return;

			atomicEntityIds.get().addAll(popInfo.getEntityIds());
		});

		return atomicEntityIds.get();
	}

	/**
	 * @return A random EcosystemType that resides in this EcosystemArea with at least one entity, or null if none are residing here.
	 */
	@Nullable
	public EcosystemType<?> getRandomEcosystemType() {
		List<EcosystemType<?>> residingTypes = getResidingEcosystemTypes();
		if(residingTypes.isEmpty()) return null;

		int index = this.world.getRandom().nextInt(residingTypes.size());
		return residingTypes.get(index);
	}

	/**
	 * @return All the EcosystemTypes that have at least one entity within this EcosystemArea.
	 */
	public List<EcosystemType<?>> getResidingEcosystemTypes() {
		List<EcosystemType<?>> residingTypes = new ArrayList<>();
		this.populations.forEach((type, popInfo) -> {
			if (popInfo.isEmpty()) return;
			residingTypes.add(type);
		});
		return residingTypes;
	}

	/**
	 * @return A random MobEntity of the given EcosystemType that is within this EcosystemArea.
	 */
	@Nullable
	public MobEntity getRandomEntity(EcosystemType<?> ecosystemType) {
		PopInfo popInfo = this.populations.get(ecosystemType);
		if (popInfo == null || popInfo.isEmpty()) return null;

		List<Integer> entityIds = popInfo.getEntityIds().stream().toList();
		int index = this.world.getRandom().nextInt(entityIds.size());
		int entityId = entityIds.get(index);
		return (MobEntity) this.world.getEntityById(entityId);
	}

	/**
	 * @return If this EcosystemArea contains the given EntityType.
	 */
	public boolean containsEntityType(EntityType<? extends MobEntity> entityType) {
		EcosystemType<?> ecosystemType = EcosystemType.get(entityType).orElse(null);
		if(ecosystemType == null) return false;

		return this.populations.containsKey(ecosystemType);
	}

	public PopInfo getOrCreatePopInfo(EcosystemType<?> type) {
		return this.getPopInfo(type, true);
	}

	@Nullable
	public PopInfo getPopInfo(EcosystemType<?> type, boolean createIfEmpty) {
		if (createIfEmpty) return this.populations.computeIfAbsent(type, PopInfo::new);
		return this.populations.getOrDefault(type, null);
	}

	/**
	 * The EcosystemType Population information of a single EcosystemType in
	 * <b>a specific {@link EcosystemArea}</b>. Does <b>NOT</b> account for
	 * nearby ChunkAreas.
	 *
	 * <br><br>
	 *
	 * Holds the Entity int ids for all of this EcosystemType within this ChunkArea,
	 * methods for adding/removing entities, and extra getter methods.
	 */
	public static class PopInfo {
		public final EcosystemType<?> type;
		public final IntOpenHashSet entityIds = new IntOpenHashSet();

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
