package dev.spiritstudios.abysm.core.component;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.entity.pattern.EntityPattern;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public final class AbysmDataComponents {
	public static final DataComponentType<HarpoonComponent> HARPOON = register(
		"harpoon",
		builder -> builder
			.persistent(HarpoonComponent.CODEC)
			.networkSynchronized(HarpoonComponent.STREAM_CODEC)
	);

	public static final DataComponentType<EntityPattern> ENTITY_PATTERN = register(
		"entity_pattern",
		builder -> builder
			.persistent(EntityPattern.CODEC)
			.networkSynchronized(EntityPattern.STREAM_CODEC)
	);

	private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Abysm.id(name), (builder.apply(DataComponentType.builder()).build()));
	}

	public static void init() {
		ComponentTooltipAppenderRegistry.addFirst(HARPOON);

		// Component used to be called blessed in-dev, this is just so we don't break all of our dev worlds.
		BuiltInRegistries.DATA_COMPONENT_TYPE.addAlias(Abysm.id("blessed"), Abysm.id("harpoon"));
	}
}
