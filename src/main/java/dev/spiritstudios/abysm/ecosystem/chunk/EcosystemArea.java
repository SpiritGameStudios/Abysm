package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class EcosystemArea {
	public final ServerWorld world;
	public final EcosystemAreaPos pos;
	public final Map<EcosystemType<?>, PopInfo> populations = new Object2ObjectOpenHashMap<>();

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

	public void tick() {

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
}
