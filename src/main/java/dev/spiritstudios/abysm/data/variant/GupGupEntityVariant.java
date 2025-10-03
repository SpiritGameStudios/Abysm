package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GupGupEntityVariant extends AbstractEntityVariant {
	public static final Codec<GupGupEntityVariant> CODEC = RecordCodecBuilder.create(
		instance -> fillFields(instance).apply(instance, GupGupEntityVariant::new)
	);

	public static final Codec<RegistryEntry<GupGupEntityVariant>> ENTRY_CODEC = RegistryFixedCodec.of(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT);

	public static final PacketCodec<RegistryByteBuf, RegistryEntry<GupGupEntityVariant>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT);

	public static RegistryEntry<GupGupEntityVariant> getDefaultEntry(DynamicRegistryManager registryManager) {
		return getDefaultEntry(registryManager.getOrThrow(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT));
	}

	public static RegistryEntry<GupGupEntityVariant> getDefaultEntry(RegistryEntryLookup<GupGupEntityVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.GUP_GUP);
	}

	public GupGupEntityVariant(Text name, Identifier texture, SpawnConditionSelectors spawnConditions) {
		super(name, texture, spawnConditions);
	}
}
