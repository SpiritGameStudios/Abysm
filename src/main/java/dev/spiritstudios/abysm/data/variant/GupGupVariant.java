package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.world.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public class GupGupVariant extends AbstractEntityVariant {
	public static final Codec<GupGupVariant> CODEC = RecordCodecBuilder.create(
		instance -> fillFields(instance).apply(instance, GupGupVariant::new)
	);

	public static final Codec<Holder<GupGupVariant>> ENTRY_CODEC = RegistryFixedCodec.create(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT);

	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<GupGupVariant>> ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT);

	public static Holder<GupGupVariant> getDefaultEntry(RegistryAccess registryManager) {
		return getDefaultEntry(registryManager.lookupOrThrow(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT));
	}

	public static Holder<GupGupVariant> getDefaultEntry(HolderGetter<GupGupVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.GUP_GUP);
	}

	public GupGupVariant(Component name, Identifier texture, SpawnPrioritySelectors spawnConditions) {
		super(name, texture, spawnConditions);
	}
}
