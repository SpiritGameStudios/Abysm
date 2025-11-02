package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class AbysmSoundEvents {
	// BIOME-SPECIFIC UNDERWATER MUSIC
	public static final Holder.Reference<SoundEvent> MUSIC_OVERWORLD_FLORAL_REEF = registerReference("music.overworld.floral_reef");
	public static final Holder.Reference<SoundEvent> MUSIC_OVERWORLD_DEEP_SEA_RUINS = registerReference("music.overworld.deep_sea_ruins");
	public static final Holder.Reference<SoundEvent> MUSIC_OVERWORLD_PEARLESCENT_SEA = registerReference("music.overworld.pearlescent_sea");
	public static final Holder.Reference<SoundEvent> MUSIC_OVERWORLD_THE_ENTWINED = registerReference("music.overworld.the_entwined");
	public static final Holder.Reference<SoundEvent> MUSIC_OVERWORLD_INKDEPTH_REALM = registerReference("music.overworld.inkdepth_realm");
	public static final Holder.Reference<SoundEvent> MUSIC_OVERWORLD_GLOWING_CAVES = registerReference("music.overworld.glowing_caves");
	// VANILLA BIOMES
	public static final Holder.Reference<SoundEvent> MUSIC_OVERWORLD_SEASIDE = registerReference("music.overworld.seaside");
	// MUSIC DISCS
	public static final Holder.Reference<SoundEvent> MUSIC_DISC_RENAISSANCE = registerReference("records.renaissance");
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
		ResourceLocation id = Abysm.id(path);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}

	private static Holder.Reference<SoundEvent> registerReference(String path) {
		ResourceLocation id = Abysm.id(path);
		return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}


	public static void init() {
		// NO-OP
	}
}
