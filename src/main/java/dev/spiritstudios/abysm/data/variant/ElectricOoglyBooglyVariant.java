package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ElectricOoglyBooglyVariant extends AbstractEntityVariant {
	public static final Codec<ElectricOoglyBooglyVariant> CODEC = RecordCodecBuilder.create(
		instance -> fillFields(instance).and(instance.group(
			ParticleTypes.TYPE_CODEC.fieldOf("fumes").forGetter(variant -> variant.fumesParticle),
			Codec.FLOAT.fieldOf("explosion_power").forGetter(variant -> variant.explosionPower)
		)).apply(instance, ElectricOoglyBooglyVariant::new)
	);

	public static final Codec<RegistryEntry<ElectricOoglyBooglyVariant>> ENTRY_CODEC = RegistryFixedCodec.of(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT);

	public static final PacketCodec<RegistryByteBuf, RegistryEntry<ElectricOoglyBooglyVariant>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT);

	public static RegistryEntry<ElectricOoglyBooglyVariant> getDefaultEntry(DynamicRegistryManager registryManager) {
		return getDefaultEntry(registryManager.getOrThrow(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT));
	}

	public static RegistryEntry<ElectricOoglyBooglyVariant> getDefaultEntry(RegistryEntryLookup<ElectricOoglyBooglyVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.ELECTRIC_OOGLY_BOOGLY);
	}

	public final ParticleEffect fumesParticle;
	public final float explosionPower;

	public ElectricOoglyBooglyVariant(Text name, Identifier texture, SpawnConditionSelectors spawnConditions, ParticleEffect fumesParticle, float explosionPower) {
		super(name, texture, spawnConditions);

		this.fumesParticle = fumesParticle;
		this.explosionPower = explosionPower;
	}
}
