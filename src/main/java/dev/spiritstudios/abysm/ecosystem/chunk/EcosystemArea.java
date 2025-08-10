package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class EcosystemArea {
	public final ServerWorld world;
	public final EcosystemAreaPos pos;
	public final Map<EcosystemType<?>, PopInfo> populations = new Object2ObjectOpenHashMap<>();
	public final Map<EcosystemType<?>, Integer> hunters = new Object2ObjectOpenHashMap<>();

	public EcosystemArea(ServerWorld world, EcosystemAreaPos pos) {
		this.world = world;
		this.pos = pos;
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

	@SuppressWarnings("unused")
	public void tick(ServerWorld serverWorld, EcosystemAreaPos pos) {
		Random random = serverWorld.getRandom();
		this.populations.forEach((ecosystemType, popInfo) -> {
			// do we want to add some sort of delay?
			this.hunters.computeIfAbsent(ecosystemType, ecosystemType1 -> {
				List<Integer> youCannotIndexASet = popInfo.entityIds.stream().toList();
				return youCannotIndexASet.get(random.nextBetween(0, youCannotIndexASet.size() - 1));
			});
			if (!popInfo.entityIds.contains(this.hunters.get(ecosystemType))) {
				this.hunters.remove(ecosystemType);
				return;

			}
			Entity entity = serverWorld.getEntityById(this.hunters.get(ecosystemType));
			if (!(entity instanceof EcologicalEntity ecologicalEntity)) {
				this.hunters.remove(ecosystemType);
				return;
			}
			EcosystemLogic logic = ecologicalEntity.getEcosystemLogic();
			if (logic.canHunt() && logic.huntTicks == 0) {
				// hunt failed
				logic.stopHunt();
				if (logic.huntTargetEntity instanceof EcologicalEntity ecoTarget) {
					ecoTarget.getEcosystemLogic().stopHunt();
				}
			} else {
				// being hunt
				logic.allowHunting();
			}
		});
	}

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

	public PopInfo getPopInfo(EcosystemType<?> type) {
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

		public boolean popMaintained() {
			return this.getEntityCount() >= this.type.targetPopulation();
		}

		public IntSet getEntityIds() {
			return entityIds;
		}
	}
}
