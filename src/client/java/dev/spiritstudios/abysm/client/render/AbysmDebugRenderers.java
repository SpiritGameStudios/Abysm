package dev.spiritstudios.abysm.client.render;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.debug.ecosystem.EcosystemDebugRenderer;
import dev.spiritstudios.specter.api.core.client.debug.DebugRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class AbysmDebugRenderers {
	public static final Identifier ECOSYSTEM_DEBUG_RENDERER_ID = Abysm.id("ecosystem_debug_renderer");

	public static void init() {
		DebugRendererRegistry.register(ECOSYSTEM_DEBUG_RENDERER_ID, new EcosystemDebugRenderer(MinecraftClient.getInstance()));
	}

}
