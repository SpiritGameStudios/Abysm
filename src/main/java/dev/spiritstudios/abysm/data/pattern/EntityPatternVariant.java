package dev.spiritstudios.abysm.data.pattern;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * The data-drivable record which contains texture path information for Entity Patterns/entity renderers to use.
 *
 * @param patternPath The pattern(texture to overlay on the model) path
 * @param baseTexture The base texture for the model - if empty, the default entity texture is used instead
 * @param colorable   If the pattern is meant to be colored with code(default), or has pre-defined colors - if empty, defaults to true
 * @see dev.spiritstudios.abysm.entity.pattern.EntityPattern
 */
public record EntityPatternVariant(EntityType<?> entityType, Component name, ResourceLocation patternPath,
								   Optional<ResourceLocation> baseTexture, boolean colorable) {
	// TODO (unimportant) - Actually impl. base texture & colorable codec entries in required places
	public static final Codec<EntityPatternVariant> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			EntityType.CODEC.fieldOf("entity").forGetter(pattern -> pattern.entityType),
			ComponentSerialization.CODEC.fieldOf("name").forGetter(pattern -> pattern.name),
			ResourceLocation.CODEC.fieldOf("pattern").forGetter(pattern -> pattern.patternPath),
			ResourceLocation.CODEC.optionalFieldOf("base").forGetter(pattern -> pattern.baseTexture),
			Codec.BOOL.optionalFieldOf("colorable", true).forGetter(pattern -> pattern.colorable)
		).apply(instance, EntityPatternVariant::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<EntityPatternVariant>> ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(AbysmRegistryKeys.ENTITY_PATTERN);

	public EntityPatternVariant(EntityType<?> entityType, Component name, ResourceLocation patternPath) {
		this(entityType, name, patternPath, Optional.empty(), true);
	}

	public EntityPatternVariant(EntityType<?> entityType, Component name, ResourceLocation patternPath, ResourceLocation baseTexture) {
		this(entityType, name, patternPath, Optional.of(baseTexture), true);
	}

	public EntityPatternVariant(EntityType<?> entityType, Component name, ResourceLocation patternPath, boolean colorable) {
		this(entityType, name, patternPath, Optional.empty(), colorable);
	}

	public static Stream<? extends Holder<EntityPatternVariant>> getVariantsForEntityType(ServerLevelAccessor world, EntityType<?> entityType) {
		RegistryAccess registryManager = world.registryAccess();
		Registry<EntityPatternVariant> registry = registryManager.lookupOrThrow(AbysmRegistryKeys.ENTITY_PATTERN);
		return registry.listElements()
			.filter(patternVariant -> patternVariant.value().entityType.equals(entityType));
	}

}
