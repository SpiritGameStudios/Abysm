package dev.spiritstudios.abysm.entity.attribute;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public final class AbysmEntityAttributes {
	public static final Holder<Attribute> SWIMMING_SPEED = register(
		"swimming_speed",
		new RangedAttribute(
			"attribute.name.abysm.swimming_speed",
			0.9, 0.0, 1.0
		).setSyncable(true)
	);

	private static Holder<Attribute> register(String id, Attribute attribute) {
		return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Abysm.id(id), attribute);
	}

	public static void init() {
		// NO-OP
	}
}
