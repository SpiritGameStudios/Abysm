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

public class SnapperEntityVariant extends AbstractEntityVariant {

	public static final Codec<SnapperEntityVariant> CODEC = RecordCodecBuilder.create(
		instance -> fillFields(instance).apply(instance, SnapperEntityVariant::new)
	);

	public static final Codec<RegistryEntry<SnapperEntityVariant>> ENTRY_CODEC = RegistryFixedCodec.of(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT);

	public static final PacketCodec<RegistryByteBuf, RegistryEntry<SnapperEntityVariant>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT);

	public static RegistryEntry<SnapperEntityVariant> getDefaultEntry(DynamicRegistryManager registryManager) {
		return getDefaultEntry(registryManager.getOrThrow(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT));
	}

	public static RegistryEntry<SnapperEntityVariant> getDefaultEntry(RegistryEntryLookup<SnapperEntityVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.CHROMATIC);
	}

	public SnapperEntityVariant(Text name, Identifier texture, SpawnConditionSelectors spawnConditions) {
		super(name, texture, spawnConditions);
	}
}
