package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public final class AbysmEntityAttributes {
	public static final RegistryEntry<EntityAttribute> SWIMMING_SPEED = register(
		"swimming_speed",
		new ClampedEntityAttribute(
			"attribute.name.abysm.swimming_speed",
			0.9, 0.0, 1.0
		).setTracked(true)
	);

	private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
		return Registry.registerReference(Registries.ATTRIBUTE, Abysm.id(id), attribute);
	}

	public static void init() {
		// NO-OP
	}
}
