package dev.spiritstudios.abysm.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class AbysmRenderStateKeys {
	public static final RenderStateDataKey<Boolean> IS_BLUE = RenderStateDataKey.create(() -> "abysm:is_blue");
}
