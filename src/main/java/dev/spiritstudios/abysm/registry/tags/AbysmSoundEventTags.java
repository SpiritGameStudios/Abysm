package dev.spiritstudios.abysm.registry.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;

public final class AbysmSoundEventTags {
	public static final TagKey<SoundEvent> UNEFFECTED_BY_WATER = of("uneffected_by_water");

	private static TagKey<SoundEvent> of(String id) {
		return TagKey.create(Registries.SOUND_EVENT, Abysm.id(id));
	}
}
