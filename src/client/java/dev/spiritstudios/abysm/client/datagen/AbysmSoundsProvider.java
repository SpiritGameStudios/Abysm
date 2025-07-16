package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;

import java.util.concurrent.CompletableFuture;

public class AbysmSoundsProvider extends FabricSoundsProvider {
	public AbysmSoundsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, SoundExporter exporter) {
		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_FLORAL_REEF,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/cichlid").stream(true).volume(0.4F))
				.category(SoundCategory.MUSIC)
		);
		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_DEEP_SEA_RUINS,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/colossal_codecs").stream(true).volume(0.4F))
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_PEARLESCENT_SEA,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/wake").stream(true).volume(0.4F))
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_OVERWORLD_THE_ENTWINED,
			SoundTypeBuilder.of()
				.sound(ofFile("music/game/abysm").stream(true).volume(0.4F))
				.category(SoundCategory.MUSIC)
		);

		exporter.add(
			AbysmSoundEvents.SOUND_ENTITY_BLOOMRAY,
			SoundTypeBuilder.of()
				.sound(ofFile("entity/bloomray/bloomray_1").volume(0.4F))
				.sound(ofFile("entity/bloomray/bloomray_2").volume(0.4F))
				.sound(ofFile("entity/bloomray/bloomray_3").volume(0.4F))
				.category(SoundCategory.NEUTRAL)
		);

		exporter.add(
			AbysmSoundEvents.MUSIC_DISC_RENAISSANCE,
			SoundTypeBuilder.of()
				.sound(ofFile("records/renaissance").stream(true))
				.category(SoundCategory.RECORDS)
		);
	}

	private SoundTypeBuilder.EntryBuilder ofFile(String path) {
		return SoundTypeBuilder.EntryBuilder.ofFile(Abysm.id(path));
	}

	@Override
	public String getName() {
		return "Sound Events";
	}
}
