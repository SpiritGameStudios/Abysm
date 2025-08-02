package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.CodecHolder;

public class AbysmDensityBlobTypes {

	public static void init() {
		register("void", DensityVoid.CODEC_HOLDER);
		register("sphere", DensitySphere.CODEC_HOLDER);
		register("layered_sphere", LayeredDensitySphere.CODEC_HOLDER);
		register("translated_blob", TranslatedDensityBlob.CODEC_HOLDER);
	}

	private static MapCodec<? extends DensityBlob> register(String id, CodecHolder<? extends DensityBlob> codecHolder) {
		return Registry.register(AbysmRegistries.DENSITY_BLOB_TYPE, Abysm.id(id), codecHolder.codec());
	}
}
