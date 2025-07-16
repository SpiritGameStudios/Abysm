package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.component.HarpoonComponent;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AbysmDataComponentTypes {

	public static final ComponentType<HarpoonComponent> HARPOON = register(
		"harpoon",
		ComponentType.<HarpoonComponent>builder()
			.codec(HarpoonComponent.CODEC)
			.packetCodec(HarpoonComponent.PACKET_CODEC)
	);

	private static <T> ComponentType<T> register(String path, ComponentType.Builder<T> builder) {
		return register(Abysm.id(path), builder);
	}

	private static <T> ComponentType<T> register(Identifier id, ComponentType.Builder<T> builder) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, id, builder.build());
	}

	public static void init() {
		ComponentTooltipAppenderRegistry.addFirst(HARPOON);

		// Component used to be called blessed in-dev, this is just so we don't break all of our dev worlds.
		Registries.DATA_COMPONENT_TYPE.addAlias(Abysm.id("blessed"), Abysm.id("harpoon"));
	}
}
