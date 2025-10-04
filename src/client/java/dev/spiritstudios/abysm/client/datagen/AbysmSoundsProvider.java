package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class AbysmSoundsProvider extends FabricSoundsProvider {
	public AbysmSoundsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	// Music & sounds created by WorldWidePixel (unless otherwise stated). Made with love and a bowl :3

	// EXTRA VANILLA SONGS (by C418)
	private final SoundTypeBuilder.EntryBuilder axolotl = ofVanillaFile("music/game/water/axolotl").stream(true).volume(0.4F).weight(1);
	private final SoundTypeBuilder.EntryBuilder dragonFish = ofVanillaFile("music/game/water/dragon_fish").stream(true).volume(0.4F).weight(1);
	private final SoundTypeBuilder.EntryBuilder shuniji = ofVanillaFile("music/game/water/shuniji").stream(true).volume(0.4F).weight(1);
	// GENERAL UNDERWATER
	private final SoundTypeBuilder.EntryBuilder tilapia = ofFile("music/game/tilapia").stream(true).volume(0.4F);

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, SoundExporter exporter) {
		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_FLORAL_REEF,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/cichlid").stream(true).volume(0.4F).weight(2)) // Original biome-exclusive song
				.sound(axolotl)
				.sound(dragonFish)
				.sound(tilapia)
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_DEEP_SEA_RUINS,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/colossal_codecs").stream(true).volume(0.4F).weight(2)) // Original biome-exclusive song
				.sound(dragonFish)
				.sound(shuniji)
				.sound(tilapia)
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_PEARLESCENT_SEA,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/wake").stream(true).volume(0.4F).weight(2)) // Original biome-exclusive song
				.sound(axolotl)
				.sound(shuniji)
				.sound(tilapia)
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_THE_ENTWINED,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/abysm").stream(true).volume(0.4F)) // The Entwined only plays this one song.
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_INKDEPTH_REALM,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/mare_ad_horizontum").stream(true).volume(0.4F).weight(2)) // Original biome-exclusive song, I will compose this at a later date
				.sound(axolotl)
				.sound(shuniji)
				.sound(tilapia)
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_GLOWING_CAVES,
			SoundTypeBuilder.of()
				.sound(axolotl)
				.sound(shuniji)
				.sound(tilapia)
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_SEASIDE,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/paddleboard").stream(true).volume(0.4F))
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			SoundEvents.MUSIC_UNDER_WATER,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/tilapia").stream(true).volume(0.4F)) // This augments the sound event instead of overriding.
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_DISC_RENAISSANCE,
			SoundTypeBuilder.of()
				.sound(ofFile("records/renaissance").stream(true))
				.category(SoundCategory.RECORDS)
		);

		exporter.add(
			AbysmSoundEvents.ENTITY_BLOOMRAY_AMBIENT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.bloomray.ambient")
				.sound(ofFile("entity/bloomray/ambient1").volume(0.4F))
				.sound(ofFile("entity/bloomray/ambient2").volume(0.4F))
				.sound(ofFile("entity/bloomray/ambient3").volume(0.4F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_BLOOMRAY_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.bloomray.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_BLOOMRAY_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.bloomray.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		exporter.add(
			AbysmSoundEvents.ITEM_OOZEBALL_APPLY,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.item.oozeball.apply")
				.sound(ofFile("item/oozeball/sticky1"))
				.sound(ofFile("item/oozeball/sticky2"))
				.sound(ofFile("item/oozeball/sticky3"))
				.category(SoundCategory.BLOCKS)
		);

		exporter.add(
			AbysmSoundEvents.ITEM_HARPOON_LAUNCH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.item.harpoon.launch")
//				.sound(ofFile("entity/harpoon/launch1"))
//				.sound(ofFile("entity/harpoon/launch2"))
				.sound(ofVanillaFile("item/trident/throw1"))
				.sound(ofVanillaFile("item/trident/throw2"))
		);

		exporter.add(
			AbysmSoundEvents.ITEM_HARPOON_IN_AIR,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.item.harpoon.in_air")
				.sound(ofVanillaFile("block/chain/break1").volume(0.5F))
				.sound(ofVanillaFile("block/chain/break2").volume(0.5F))
				.sound(ofVanillaFile("block/chain/break3").volume(0.5F))
				.sound(ofVanillaFile("block/chain/break4").volume(0.5F))
//				.sound(ofFile("entity/harpoon/launch1").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch2").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch2").volume(0.8F).pitch(0.8F))
//				.sound(ofFile("entity/harpoon/launch3").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch4").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch5").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch5").volume(0.8F).pitch(1.2F))
//				.sound(ofFile("entity/harpoon/launch6").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch7").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch8").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch8").volume(0.8F).pitch(0.8F))
//				.sound(ofFile("entity/harpoon/launch9").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch10").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch11").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch11").volume(0.8F).pitch(1.2F))
//				.sound(ofFile("entity/harpoon/launch12").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch13").volume(0.8F))
//				.sound(ofFile("entity/harpoon/launch13").volume(0.8F).pitch(0.8F))
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ITEM_HARPOON_HIT_GROUND,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.item.harpoon.hit_ground")
				.sound(ofVanillaFile("item/trident/ground_impact1").volume(0.9F))
				.sound(ofVanillaFile("item/trident/ground_impact2").volume(0.9F))
				.sound(ofVanillaFile("item/trident/ground_impact3").volume(0.9F))
				.sound(ofVanillaFile("item/trident/ground_impact4").volume(0.9F))
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ITEM_HARPOON_HIT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.item.harpoon.hit")
				.sound(ofVanillaFile("item/trident/pierce1"))
				.sound(ofVanillaFile("item/trident/pierce2"))
				.sound(ofVanillaFile("item/trident/pierce3"))
		);

		exporter.add(
			AbysmSoundEvents.ITEM_HARPOON_RETURN,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.item.harpoon.return")
				.sound(ofVanillaFile("item/trident/return1").volume(0.8F))
				.sound(ofVanillaFile("item/trident/return2").volume(0.8F))
				.sound(ofVanillaFile("item/trident/return2").volume(0.8F).pitch(0.8F))
				.sound(ofVanillaFile("item/trident/return2").volume(0.8F).pitch(1.2F))
				.sound(ofVanillaFile("item/trident/return3").volume(0.8F))
				.sound(ofVanillaFile("item/trident/return3").volume(0.8F).pitch(0.8F))
				.sound(ofVanillaFile("item/trident/return3").volume(0.8F).pitch(0.8F))
				.sound(ofVanillaFile("item/trident/return3").volume(0.8F).pitch(1.2F))
//				.sound(ofFile("entity/harpoon/retract1").volume(0.8F))
//				.sound(ofFile("entity/harpoon/retract2").volume(0.8F))
//				.sound(ofFile("entity/harpoon/retract2").volume(0.8F).pitch(1.2F))
//				.sound(ofFile("entity/harpoon/retract3").volume(0.8F))
//				.sound(ofFile("entity/harpoon/retract4").volume(0.8F))
//				.sound(ofFile("entity/harpoon/retract4").volume(0.8F).pitch(0.8F))
//				.sound(ofFile("entity/harpoon/retract5").volume(0.8F))
//				.sound(ofFile("entity/harpoon/retract6").volume(0.8F))
//				.sound(ofFile("entity/harpoon/retract6").volume(0.8F).pitch(0.8F))
//				.sound(ofFile("entity/harpoon/retract7").volume(0.8F))
//				.sound(ofFile("entity/harpoon/retract7").volume(0.8F).pitch(1.2F))
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_PADDLEFISH_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.paddlefish.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_PADDLEFISH_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.paddlefish.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_PADDLEFISH_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.paddlefish.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_BIG_FLORAL_FISH_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.big_floral_fish.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_BIG_FLORAL_FISH_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.big_floral_fish.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_BIG_FLORAL_FISH_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.big_floral_fish.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_SMALL_FLORAL_FISH_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.small_floral_fish.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_SMALL_FLORAL_FISH_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.small_floral_fish.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_SMALL_FLORAL_FISH_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.small_floral_fish.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_GUP_GUP_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.gup_gup.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_GUP_GUP_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.gup_gup.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_GUP_GUP_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.gup_gup.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_AROWANA_MAGICII_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.arowana_magicii.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_AROWANA_MAGICII_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.arowana_magicii.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_AROWANA_MAGICII_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.arowana_magicii.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_SYNTHETHIC_ORNIOTHOPE_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.synthethic_orniothope.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_SYNTHETHIC_ORNIOTHOPE_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.synthethic_orniothope.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_SYNTHETHIC_ORNIOTHOPE_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.synthethic_orniothope.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_RETICULATED_FLIPRAY_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.reticulated_flipray.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_RETICULATED_FLIPRAY_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.reticulated_flipray.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_RETICULATED_FLIPRAY_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.reticulated_flipray.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_LECTORFIN_HURT,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.lectorfin.hurt")
				.sound(ofVanillaFile("entity/fish/hurt1"))
				.sound(ofVanillaFile("entity/fish/hurt2"))
				.sound(ofVanillaFile("entity/fish/hurt3"))
				.sound(ofVanillaFile("entity/fish/hurt4"))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_LECTORFIN_DEATH,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.lectorfin.death")
				.sound(ofVanillaFile("entity/fish/hurt1").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt2").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt3").pitch(0.8F))
				.sound(ofVanillaFile("entity/fish/hurt4").pitch(0.8F))
				.category(SoundCategory.NEUTRAL)
		);

		// TODO: Custom sounds (optional)
		exporter.add(
			AbysmSoundEvents.ENTITY_LECTORFIN_FLOP,
			SoundTypeBuilder.of()
				.subtitle("subtitles.abysm.entity.lectorfin.flop")
				.sound(ofVanillaFile("entity/fish/flop1").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop2").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop3").volume(0.3F))
				.sound(ofVanillaFile("entity/fish/flop4").volume(0.3F))
				.category(SoundCategory.NEUTRAL)
		);
	}

	private SoundTypeBuilder.EntryBuilder ofFile(String path) {
		return SoundTypeBuilder.EntryBuilder.ofFile(Abysm.id(path));
	}

	private SoundTypeBuilder.EntryBuilder ofVanillaFile(String path) {
		return SoundTypeBuilder.EntryBuilder.ofFile(Identifier.ofVanilla(path));
	}

	@Override
	public String getName() {
		return "Sound Events";
	}
}
