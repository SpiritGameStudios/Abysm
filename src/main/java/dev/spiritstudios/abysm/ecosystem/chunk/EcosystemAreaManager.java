package dev.spiritstudios.abysm.ecosystem.chunk;

import dev.spiritstudios.abysm.duck.EcosystemManagedLevel;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;

public class EcosystemAreaManager {
	public final ServerLevel world;
	public final Map<EcosystemAreaPos, EcosystemArea> areas = new Object2ObjectOpenHashMap<>();

	public EcosystemAreaManager(ServerLevel world) {
		this.world = world;
	}

	public void tick() {
		// TODO - EcosystemArea unloading either when empty or when all their chunks are unloaded
		this.areas.forEach((pos, area) -> area.tick(this.world, pos));
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

	public static EcosystemAreaManager getEcosystemAreaManagerForWorld(ServerLevel world) {
		EcosystemManagedLevel ecosystemManagedLevel = (EcosystemManagedLevel) world;
		return ecosystemManagedLevel.abysm$getEcosystemAreaManager();
	}
}
