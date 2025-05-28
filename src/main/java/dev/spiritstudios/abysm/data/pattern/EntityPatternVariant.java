package dev.spiritstudios.abysm.data.pattern;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.ServerWorldAccess;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The data-drivable record which contains texture path information for Entity Patterns/entity renderers to use.
 *
 * @see dev.spiritstudios.abysm.entity.pattern.EntityPattern
 *
 * @param patternPath The pattern(texture to overlay on the model) path
 * @param baseTexture The base texture for the model - if empty, the default entity texture is used instead
 * @param colorable If the pattern is meant to be colored with code(default), or has pre-defined colors - if empty, defaults to true
 */
public record EntityPatternVariant(EntityType<?> entityType, String name, Identifier patternPath, Optional<Identifier> baseTexture, Optional<Boolean> colorable) {
	public static final Codec<EntityPatternVariant> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			EntityType.CODEC.fieldOf("entity").forGetter(pattern -> pattern.entityType),
			Codec.STRING.fieldOf("name").forGetter(pattern -> pattern.name),
			Identifier.CODEC.fieldOf("pattern").forGetter(pattern -> pattern.patternPath),
			Identifier.CODEC.optionalFieldOf("base").forGetter(pattern -> pattern.baseTexture),
			Codec.BOOL.optionalFieldOf("colorable").forGetter(pattern -> pattern.colorable)
		).apply(instance, EntityPatternVariant::new)
	);

	public static final PacketCodec<RegistryByteBuf, EntityPatternVariant> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

	// Unused, but I believe it is still needed?
//	public static final Codec<RegistryEntry<EntityPatternVariant>> ENTRY_CODEC = RegistryElementCodec.of(EntityPatternVariantRegistry.ENTITY_PATTERN_VARIANT_KEY, CODEC);

	public EntityPatternVariant(EntityType<?> entityType, String name, Identifier patternPath) {
		this(entityType, name, patternPath, Optional.empty(), Optional.of(true));
	}

	public EntityPatternVariant(EntityType<?> entityType, String name, Identifier patternPath, Identifier baseTexture) {
		this(entityType, name, patternPath, Optional.of(baseTexture), Optional.of(true));
	}

	public EntityPatternVariant(EntityType<?> entityType, String name, Identifier patternPath, boolean colorable) {
		this(entityType, name, patternPath, Optional.empty(), Optional.of(colorable));
	}

	public static Set<EntityPatternVariant> getVariantsForEntityType(ServerWorldAccess world, EntityType<?> entityType) {
		DynamicRegistryManager registryManager = world.getRegistryManager();
		Registry<EntityPatternVariant> registry = registryManager.getOrThrow(AbysmRegistries.ENTITY_PATTERN);
		return registry.stream().filter(patternVariant -> patternVariant.entityType.equals(entityType)).collect(Collectors.toSet());
	}

}
