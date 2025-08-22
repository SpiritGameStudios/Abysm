package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class AbysmSoundEvents {
	// BIOME-SPECIFIC UNDERWATER MUSIC
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_FLORAL_REEF = registerReference("music.overworld.floral_reef");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_DEEP_SEA_RUINS = registerReference("music.overworld.deep_sea_ruins");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_PEARLESCENT_SEA = registerReference("music.overworld.pearlescent_sea");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_THE_ENTWINED = registerReference("music.overworld.the_entwined");
	// VANILLA BIOMES
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_SEASIDE = registerReference("music.overworld.seaside");
	// MUSIC DISCS
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_RENAISSANCE = registerReference("records.renaissance");
	// SOUNDS
	public static final SoundEvent ENTITY_BLOOMRAY_AMBIENT = register("entity.bloomray.ambient");
	public static final SoundEvent ENTITY_BLOOMRAY_HURT = register("entity.bloomray.hurt");
	public static final SoundEvent ENTITY_BLOOMRAY_DEATH = register("entity.bloomray.death");

	public static final SoundEvent ENTITY_BIG_FLORAL_FISH_HURT = register("entity.big_floral_fish.hurt");
	public static final SoundEvent ENTITY_BIG_FLORAL_FISH_DEATH = register("entity.big_floral_fish.death");
	public static final SoundEvent ENTITY_BIG_FLORAL_FISH_FLOP = register("entity.big_floral_fish.flop");

	public static final SoundEvent ENTITY_SMALL_FLORAL_FISH_HURT = register("entity.small_floral_fish.hurt");
	public static final SoundEvent ENTITY_SMALL_FLORAL_FISH_DEATH = register("entity.small_floral_fish.death");
	public static final SoundEvent ENTITY_SMALL_FLORAL_FISH_FLOP = register("entity.small_floral_fish.flop");

	public static final SoundEvent ENTITY_PADDLEFISH_HURT = register("entity.paddlefish.hurt");
	public static final SoundEvent ENTITY_PADDLEFISH_DEATH = register("entity.paddlefish.death");
	public static final SoundEvent ENTITY_PADDLEFISH_FLOP = register("entity.paddlefish.flop");

	public static final SoundEvent ITEM_OOZEBALL_APPLY = register("item.oozeball.apply");

	public static final SoundEvent ITEM_HARPOON_LAUNCH = register("item.harpoon.launch");
	public static final SoundEvent ITEM_HARPOON_IN_AIR = register("item.harpoon.in_air");
	public static final SoundEvent ITEM_HARPOON_HIT_GROUND = register("item.harpoon.hit_ground");
	public static final SoundEvent ITEM_HARPOON_HIT = register("item.harpoon.hit");
	public static final SoundEvent ITEM_HARPOON_RETURN = register("item.harpoon.return");

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
