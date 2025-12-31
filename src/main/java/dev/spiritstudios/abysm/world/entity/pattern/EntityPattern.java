package dev.spiritstudios.abysm.world.entity.pattern;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

/**
 * Persistent/synced object to contain entity pattern information, such as the pattern variant(textures) and colors. Used by renderers for rendering, and entities for nbt writing.
 *
 * @see EntityPatternVariant
 */
public record EntityPattern(Holder<EntityPatternVariant> variant, int baseColor, int patternColor) {
	public static final Codec<EntityPattern> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			RegistryFixedCodec.create(AbysmRegistryKeys.ENTITY_PATTERN).fieldOf("pattern_variant").forGetter(pattern -> pattern.variant),
			Codec.INT.fieldOf("base_color").forGetter(pattern -> pattern.baseColor),
			Codec.INT.fieldOf("pattern_color").forGetter(pattern -> pattern.patternColor)
		).apply(instance, EntityPattern::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, EntityPattern> STREAM_CODEC = StreamCodec.composite(
		EntityPatternVariant.ENTRY_STREAM_CODEC, EntityPattern::variant,
		ByteBufCodecs.INT, EntityPattern::baseColor,
		ByteBufCodecs.INT, EntityPattern::patternColor,
		EntityPattern::new
	);

	public static final EntityPattern EMPTY_PATTERN = new EntityPattern(null, 0xF9FFFE, 0xF9FFFE);
}
