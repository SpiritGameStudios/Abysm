package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public class GupGupEntityVariant extends AbstractEntityVariant {
	public static final Codec<GupGupEntityVariant> CODEC = RecordCodecBuilder.create(
		instance -> fillFields(instance).apply(instance, GupGupEntityVariant::new)
	);

	public static final Codec<Holder<GupGupEntityVariant>> ENTRY_CODEC = RegistryFixedCodec.create(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT);

	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<GupGupEntityVariant>> ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT);

	public static Holder<GupGupEntityVariant> getDefaultEntry(RegistryAccess registryManager) {
		return getDefaultEntry(registryManager.lookupOrThrow(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT));
	}

	public static Holder<GupGupEntityVariant> getDefaultEntry(HolderGetter<GupGupEntityVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.GUP_GUP);
	}

	public GupGupEntityVariant(Component name, ResourceLocation texture, SpawnPrioritySelectors spawnConditions) {
		super(name, texture, spawnConditions);
	}
}
