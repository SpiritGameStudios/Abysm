package dev.spiritstudios.abysm.core.component;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.entity.pattern.EntityPattern;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class AbysmDataComponents {

	public static final DataComponentType<HarpoonComponent> HARPOON = register(
		"harpoon",
		DataComponentType.<HarpoonComponent>builder()
			.persistent(HarpoonComponent.CODEC)
			.networkSynchronized(HarpoonComponent.PACKET_CODEC)
	);

	public static final DataComponentType<EntityPattern> ENTITY_PATTERN = register(
		"entity_pattern",
		DataComponentType.<EntityPattern>builder()
			.persistent(EntityPattern.CODEC)
			.networkSynchronized(EntityPattern.PACKET_CODEC)
	);

	private static <T> DataComponentType<T> register(String path, DataComponentType.Builder<T> builder) {
		return register(Abysm.id(path), builder);
	}

	private static <T> DataComponentType<T> register(Identifier id, DataComponentType.Builder<T> builder) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builder.build());
	}

	public static void init() {
		ComponentTooltipAppenderRegistry.addFirst(HARPOON);

		// Component used to be called blessed in-dev, this is just so we don't break all of our dev worlds.
		BuiltInRegistries.DATA_COMPONENT_TYPE.addAlias(Abysm.id("blessed"), Abysm.id("harpoon"));
	}
}
