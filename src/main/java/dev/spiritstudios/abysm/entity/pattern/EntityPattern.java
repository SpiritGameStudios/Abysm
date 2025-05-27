package dev.spiritstudios.abysm.entity.pattern;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.Optional;

/**
 * Persistent/synced object to contain entity pattern information, such as the pattern variant(textures) and colors. Used by renderers for rendering, and entities for nbt writing.
 *
 * @see EntityPatternVariant
 */
public record EntityPattern(EntityPatternVariant variant, int baseColor, int patternColor) {
	public static final Codec<EntityPattern> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			EntityPatternVariant.CODEC.fieldOf("pattern_variant").forGetter(pattern -> pattern.variant),
			Codec.INT.fieldOf("base_color").forGetter(pattern -> pattern.baseColor),
			Codec.INT.fieldOf("pattern_color").forGetter(pattern -> pattern.patternColor)
		).apply(instance, EntityPattern::new)
	);

	public static final PacketCodec<RegistryByteBuf, EntityPattern> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

	public static final TrackedDataHandler<EntityPattern> ENTITY_PATTERN_DATA_HANDLER = TrackedDataHandler.create(PACKET_CODEC);

	public static final EntityPattern EMPTY_PATTERN = new EntityPattern(null, 16383998, 16383998);

	public static Optional<EntityPattern> fromNbt(NbtCompound nbt) {
		return nbt.get("entity_pattern", CODEC);
	}

	public void writeNbt(NbtCompound nbt) {
		nbt.put("entity_pattern", CODEC, this);
	}

	public static void init() {
		FabricTrackedDataRegistry.register(Abysm.id("entity_pattern_data_handler"), ENTITY_PATTERN_DATA_HANDLER);
	}

}
