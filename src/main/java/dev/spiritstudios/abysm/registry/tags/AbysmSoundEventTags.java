package dev.spiritstudios.abysm.registry.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;

public final class AbysmSoundEventTags {
	public static final TagKey<SoundEvent> UNEFFECTED_BY_WATER = of("uneffected_by_water");

	private static TagKey<SoundEvent> of(String id) {
		return TagKey.of(RegistryKeys.SOUND_EVENT, Abysm.id(id));
	}
}
