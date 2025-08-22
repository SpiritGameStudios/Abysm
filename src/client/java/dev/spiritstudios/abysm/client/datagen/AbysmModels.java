package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;

import java.util.Optional;

import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_BASE_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_FLOWER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_INNER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_PETAL_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.LANTERN_OVERLAY;
import static net.minecraft.client.data.TextureKey.BOTTOM;
import static net.minecraft.client.data.TextureKey.LANTERN;
import static net.minecraft.client.data.TextureKey.SIDE;
import static net.minecraft.client.data.TextureKey.TOP;

public class AbysmModels {
	public static final Model BLOOMING_CROWN = block("blooming_crown", TOP, SIDE, BOTTOM, CROWN_PETAL_KEY, CROWN_INNER_KEY);
	public static final Model BLOSSOM = block("blossom", BLOSSOM_FLOWER_KEY, BLOSSOM_BASE_KEY);

	public static final Model TEMPLATE_OOZETRICKLE_LANTERN = block("template_oozetrickle_lantern", LANTERN, LANTERN_OVERLAY);
	public static final Model TEMPLATE_HANGING_OOZETRICKLE_LANTERN = block("template_hanging_oozetrickle_lantern", "_hanging", LANTERN, LANTERN_OVERLAY);

	private static Model block(String parent, TextureKey... requiredTextureKeys) {
		return new Model(
			Optional.of(Abysm.id("block/" + parent)),
			Optional.empty(),
			requiredTextureKeys
		);
	}


	private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
		return new Model(
			Optional.of(Abysm.id("block/" + parent)),
			Optional.of(variant),
			requiredTextureKeys
		);
	}
}
