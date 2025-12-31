package dev.spiritstudios.abysm.world.ecosystem.chunk;

import dev.spiritstudios.abysm.duck.EcosystemManagedLevel;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;

public class EcosystemAreaManager {
	public final ServerLevel level;
	public final Map<EcosystemAreaPos, EcosystemArea> areas = new Object2ObjectOpenHashMap<>();

	public EcosystemAreaManager(ServerLevel level) {
		this.level = level;
	}

	public void tick() {
		// TODO - EcosystemArea unloading either when empty or when all their chunks are unloaded
		this.areas.forEach((pos, area) -> area.tick(this.level, pos));
	}

	public boolean containsArea(EcosystemAreaPos pos) {
		return this.areas.containsKey(pos);
	}

	public EcosystemArea getEcosystemArea(EcosystemAreaPos pos) {
		return this.getEcosystemArea(pos, false);
	}

	public EcosystemArea getEcosystemArea(EcosystemAreaPos pos, boolean createIfEmpty) {
		if (createIfEmpty) return this.areas.computeIfAbsent(pos, pos1 -> new EcosystemArea(this.level, pos));
		return this.areas.getOrDefault(pos, null);
	}

	public static EcosystemAreaManager getEcosystemAreaManagerForWorld(ServerLevel world) {
		EcosystemManagedLevel ecosystemManagedLevel = (EcosystemManagedLevel) world;
		return ecosystemManagedLevel.abysm$getEcosystemAreaManager();
	}
}
