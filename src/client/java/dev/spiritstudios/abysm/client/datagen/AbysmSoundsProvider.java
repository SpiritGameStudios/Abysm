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
				.sound(ofFile("music/game/cichlid").stream(true))
				.category(SoundCategory.MUSIC)
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
