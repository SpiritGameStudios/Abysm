package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.component.BlessedComponent;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AbysmDataComponentTypes {

	public static final ComponentType<BlessedComponent> BLESSED = register("blessed", ComponentType.<BlessedComponent>builder().codec(BlessedComponent.CODEC).packetCodec(BlessedComponent.PACKET_CODEC));

	private static <T> ComponentType<T> register(String path, ComponentType.Builder<T> builder) {
		return register(Abysm.id(path), builder);
	}

	private static <T> ComponentType<T> register(Identifier id, ComponentType.Builder<T> builder) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, id, builder.build());
	}

	public static void init() {
		ComponentTooltipAppenderRegistry.addFirst(BLESSED);
	}
}
