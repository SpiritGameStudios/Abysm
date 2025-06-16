package dev.spiritstudios.abysm.registry.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class AbysmEntityTypeTags {

	public static final TagKey<EntityType<?>> MAN_O_WAR_FRIEND = of("man_o_war_friend");

	private static TagKey<EntityType<?>> of(String id) {
		return TagKey.of(RegistryKeys.ENTITY_TYPE, Abysm.id(id));
	}
}
