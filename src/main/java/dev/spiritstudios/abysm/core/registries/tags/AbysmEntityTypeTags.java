package dev.spiritstudios.abysm.core.registries.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class AbysmEntityTypeTags {

	public static final TagKey<EntityType<?>> MAN_O_WAR_FRIEND = of("man_o_war_friend");
	public static final TagKey<EntityType<?>> MAN_O_WAR_PREY = of("man_o_war_prey");
	public static final TagKey<EntityType<?>> HARPOON_UNHAULABLE = of("harpoon_unhaulable");
	public static final TagKey<EntityType<?>> LEHYDRATHAN_HUNT_TARGETS = of("lehydrathan_hunt_targets");

	private static TagKey<EntityType<?>> of(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, Abysm.id(id));
	}
}
