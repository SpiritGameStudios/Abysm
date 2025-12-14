package dev.spiritstudios.abysm.client.debug;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.client.gui.components.debug.DebugEntryNoop;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;

public final class AbysmDebugScreenEntries {
	public static final Identifier ECOSYSTEM_POPULATION = register("ecosystem_population", new DebugEntryNoop());
	public static final Identifier ECOSYSTEM_ENTITY = register("ecosystem_entity", new DebugEntryNoop());

	private static Identifier register(String name, DebugScreenEntry entry) {
		return DebugScreenEntries.register(Abysm.id(name), entry);
	}
}
