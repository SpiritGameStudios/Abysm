package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class AbysmSoundEvents {
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_FLORAL_REEF = registerReference("music.overworld.floral_reef");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_DEEP_SEA_RUINS = registerReference("music.overworld.deep_sea_ruins");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_PEARLESCENT_SEA = registerReference("music.overworld.pearlescent_sea");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_THE_ENTWINED = registerReference("music.overworld.the_entwined");

	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_RENAISSANCE = registerReference("records.renaissance");

	public static final RegistryEntry.Reference<SoundEvent> SOUND_ENTITY_BLOOMRAY = registerReference("entity.bloomray");

	private static SoundEvent register(String path) {
		Identifier id = Abysm.id(path);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

	private static RegistryEntry.Reference<SoundEvent> registerReference(String path) {
		Identifier id = Abysm.id(path);
		return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

	public static void init() {
		// NO-OP
	}
}
