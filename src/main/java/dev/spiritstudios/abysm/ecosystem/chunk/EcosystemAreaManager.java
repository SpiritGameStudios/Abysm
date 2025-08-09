package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.duck.EcosystemManagedWorld;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;

public class EcosystemAreaManager {
	public final ServerWorld world;
	public final Map<EcosystemAreaPos, EcosystemArea> areas = new Object2ObjectOpenHashMap<>();

	public EcosystemAreaManager(ServerWorld world) {
		this.world = world;
	}

	public void tick() {
		this.areas.forEach((pos, area) -> area.tick(pos));
	}

	public boolean containsArea(EcosystemAreaPos pos) {
		return this.areas.containsKey(pos);
	}

	public EcosystemArea getEcosystemArea(EcosystemAreaPos pos) {
		return this.getEcosystemArea(pos, false);
	}

	public EcosystemArea getEcosystemArea(EcosystemAreaPos pos, boolean createIfEmpty) {
		if (createIfEmpty) return this.areas.computeIfAbsent(pos, pos1 -> new EcosystemArea(this.world, pos));
		return this.areas.getOrDefault(pos, null);
	}

	public static EcosystemAreaManager getEcosystemAreaManagerForWorld(ServerWorld world) {
		EcosystemManagedWorld ecosystemManagedWorld = (EcosystemManagedWorld) world;
		return ecosystemManagedWorld.abysm$getEcosystemAreaManager();
	}
}
