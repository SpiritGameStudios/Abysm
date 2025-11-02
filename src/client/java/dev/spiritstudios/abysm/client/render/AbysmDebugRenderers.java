package dev.spiritstudios.abysm.client.render;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.debug.ecosystem.EcosystemDebugRenderer;
import dev.spiritstudios.abysm.client.render.debug.ecosystem.EcosystemEntityDebugRenderer;
import dev.spiritstudios.specter.api.core.client.debug.DebugRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class AbysmDebugRenderers {
	public static final ResourceLocation ECOSYSTEM_ID = Abysm.id("ecosystem");
	public static final ResourceLocation ECOSYSTEM_ENTITY_ID = Abysm.id("ecosystem_entity");

	public static void init() {
		DebugRendererRegistry.register(ECOSYSTEM_ID, new EcosystemDebugRenderer(Minecraft.getInstance()));
		DebugRendererRegistry.register(ECOSYSTEM_ENTITY_ID, new EcosystemEntityDebugRenderer(Minecraft.getInstance()));
	}
}
