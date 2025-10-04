package dev.spiritstudios.abysm.entity.pattern;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

/**
 * Persistent/synced object to contain entity pattern information, such as the pattern variant(textures) and colors. Used by renderers for rendering, and entities for nbt writing.
 *
 * @see EntityPatternVariant
 */
public record EntityPattern(RegistryEntry<EntityPatternVariant> variant, int baseColor, int patternColor) {
	public static final Codec<EntityPattern> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			RegistryFixedCodec.of(AbysmRegistryKeys.ENTITY_PATTERN).fieldOf("pattern_variant").forGetter(pattern -> pattern.variant),
			Codec.INT.fieldOf("base_color").forGetter(pattern -> pattern.baseColor),
			Codec.INT.fieldOf("pattern_color").forGetter(pattern -> pattern.patternColor)
		).apply(instance, EntityPattern::new)
	);

	public static final PacketCodec<RegistryByteBuf, EntityPattern> PACKET_CODEC = PacketCodec.tuple(
		EntityPatternVariant.ENTRY_PACKET_CODEC, EntityPattern::variant,
		PacketCodecs.INTEGER, EntityPattern::baseColor,
		PacketCodecs.INTEGER, EntityPattern::patternColor,
		EntityPattern::new
	);

	public static final EntityPattern EMPTY_PATTERN = new EntityPattern(null, 0xF9FFFE, 0xF9FFFE);
}
