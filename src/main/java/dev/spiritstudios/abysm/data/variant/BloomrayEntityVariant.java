package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.block.Block;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public class BloomrayEntityVariant extends AbstractEntityVariant {

	public static final Codec<BloomrayEntityVariant> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			getNameCodec(),
			getTextureCodec(),
			HideableCrownType.CODEC.fieldOf("crown").forGetter(variant -> variant.hideableCrownType)
		).apply(instance, BloomrayEntityVariant::new)
	);

	public static final BloomrayEntityVariant DEFAULT = new BloomrayEntityVariant(
		Text.translatable("entity.abysm.bloomray.rosy"),
		buildEntityTexturePath("rosy_bloomray"),
		HideableCrownType.SODALITE_CROWN
	);

	public final HideableCrownType hideableCrownType;

	public BloomrayEntityVariant(Text name, Identifier texture, HideableCrownType hideableCrownType) {
		super(name, texture);
		this.hideableCrownType = hideableCrownType;
	}

	// Helper methods
	public static BloomrayEntityVariant fromIntId(DynamicRegistryManager registryManager, int id) {
		return AbstractEntityVariant.fromIntId(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT, DEFAULT, registryManager, id);
	}

	public static int toIntId(DynamicRegistryManager registryManager, BloomrayEntityVariant variant) {
		return AbstractEntityVariant.toIntId(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT, registryManager, variant);
	}

	public static int getDefaultIntId(DynamicRegistryManager registryManager) {
		return toIntId(registryManager, DEFAULT);
	}

	// Getter method(s)
	public HideableCrownType getHideableCrownType() {
		return hideableCrownType;
	}

	// Hiding crown type
	// Uses this hard-coded enum instead of a block codec to hopefully reduce lag with the block finding AI
	public static enum HideableCrownType implements StringIdentifiable{
		SODALITE_CROWN(AbysmBlocks.BLOOMING_SODALITE_CROWN, "sodalite"),
		ANYOLITE_CROWN(AbysmBlocks.BLOOMING_ANYOLITE_CROWN, "anyolite"),
		MELILITE_CROWN(AbysmBlocks.BLOOMING_MELILITE_CROWN, "melilite");

		public static final Codec<BloomrayEntityVariant.HideableCrownType> CODEC = StringIdentifiable.createCodec(BloomrayEntityVariant.HideableCrownType::values);
		private final Block crown;
		private final String id;

		private HideableCrownType(Block crown, String id) {
			this.crown = crown;
			this.id = id;
		}

		public Block getCrown() {
			return crown;
		}

		public String getId() {
			return id;
		}

		@Override
		public String asString() {
			return this.getId();
		}
	}
}
