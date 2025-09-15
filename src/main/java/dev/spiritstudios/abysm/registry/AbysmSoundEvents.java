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
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_PADDLEBOARD = registerReference("records.paddleboard");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_KORALLENFLIEDER = registerReference("records.korallenflieder");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_WAKE = registerReference("records.wake");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_CICHLID = registerReference("records.cichlid");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_COLOSSAL_CODECS = registerReference("records.colossal_codecs");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_ABYSM = registerReference("records.abysm");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_TILAPIA = registerReference("records.tilapia");
	public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_NO = registerReference("records.no");
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

	public static final SoundEvent ENTITY_GUP_GUP_HURT = register("entity.gup_gup.hurt");
	public static final SoundEvent ENTITY_GUP_GUP_DEATH = register("entity.gup_gup.death");
	public static final SoundEvent ENTITY_GUP_GUP_FLOP = register("entity.gup_gup.flop");

	public static final SoundEvent ENTITY_AROWANA_MAGICII_HURT = register("entity.arowana_magicii.hurt");
	public static final SoundEvent ENTITY_AROWANA_MAGICII_DEATH = register("entity.arowana_magicii.death");
	public static final SoundEvent ENTITY_AROWANA_MAGICII_FLOP = register("entity.arowana_magicii.flop");

	public static final SoundEvent ENTITY_SYNTHETHIC_ORNIOTHOPE_HURT = register("entity.synthethic_orniothope.hurt");
	public static final SoundEvent ENTITY_SYNTHETHIC_ORNIOTHOPE_DEATH = register("entity.synthethic_orniothope.death");
	public static final SoundEvent ENTITY_SYNTHETHIC_ORNIOTHOPE_FLOP = register("entity.synthethic_orniothope.flop");

	public static final SoundEvent ENTITY_RETICULATED_FLIPRAY_HURT = register("entity.reticulated_flipray.hurt");
	public static final SoundEvent ENTITY_RETICULATED_FLIPRAY_DEATH = register("entity.reticulated_flipray.death");
	public static final SoundEvent ENTITY_RETICULATED_FLIPRAY_FLOP = register("entity.reticulated_flipray.flop");

	public static final SoundEvent ENTITY_LECTORFIN_HURT = register("entity.lectorfin.hurt");
	public static final SoundEvent ENTITY_LECTORFIN_DEATH = register("entity.lectorfin.death");
	public static final SoundEvent ENTITY_LECTORFIN_FLOP = register("entity.lectorfin.flop");

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
