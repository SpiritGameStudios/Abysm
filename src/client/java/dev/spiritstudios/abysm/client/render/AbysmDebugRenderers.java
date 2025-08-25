package dev.spiritstudios.abysm.client.render;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.debug.ecosystem.EcosystemDebugRenderer;
import dev.spiritstudios.abysm.client.render.debug.ecosystem.EcosystemEntityDebugRenderer;
import dev.spiritstudios.specter.api.core.client.debug.DebugRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class AbysmDebugRenderers {
	public static final Identifier ECOSYSTEM_ID = Abysm.id("ecosystem");
	public static final Identifier ECOSYSTEM_ENTITY_ID = Abysm.id("ecosystem_entity");

	public static void init() {
		DebugRendererRegistry.register(ECOSYSTEM_ID, new EcosystemDebugRenderer(MinecraftClient.getInstance()));
		DebugRendererRegistry.register(ECOSYSTEM_ENTITY_ID, new EcosystemEntityDebugRenderer(MinecraftClient.getInstance()));
	}
}
