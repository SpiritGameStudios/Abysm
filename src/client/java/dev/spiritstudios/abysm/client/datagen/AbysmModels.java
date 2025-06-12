package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;

import java.util.Optional;

import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.*;
import static net.minecraft.client.data.TextureKey.*;

public class AbysmModels {
	public static final Model BLOOMING_CROWN = block("blooming_crown", TOP, SIDE, BOTTOM, CROWN_PETAL_KEY, CROWN_INNER_KEY);
	public static final Model BLOSSOM = block("blossom", BLOSSOM_FLOWER_KEY, BLOSSOM_BASE_KEY);

	private static Model block(String parent, TextureKey... requiredTextureKeys) {
		return new Model(
			Optional.of(Abysm.id("block/" + parent)),
			Optional.empty(),
			requiredTextureKeys
		);
	}
}
