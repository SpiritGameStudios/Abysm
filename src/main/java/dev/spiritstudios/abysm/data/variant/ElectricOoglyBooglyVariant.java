package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.world.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public class ElectricOoglyBooglyVariant extends AbstractEntityVariant {
	public static final Codec<ElectricOoglyBooglyVariant> CODEC = RecordCodecBuilder.create(
		instance -> fillFields(instance).and(instance.group(
			ParticleTypes.CODEC.fieldOf("fumes").forGetter(variant -> variant.fumesParticle),
			Codec.FLOAT.fieldOf("explosion_power").forGetter(variant -> variant.explosionPower)
		)).apply(instance, ElectricOoglyBooglyVariant::new)
	);

	public static final Codec<Holder<ElectricOoglyBooglyVariant>> ENTRY_CODEC = RegistryFixedCodec.create(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT);

	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ElectricOoglyBooglyVariant>> ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT);

	public static Holder<ElectricOoglyBooglyVariant> getDefaultEntry(RegistryAccess registryManager) {
		return getDefaultEntry(registryManager.lookupOrThrow(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT));
	}

	public static Holder<ElectricOoglyBooglyVariant> getDefaultEntry(HolderGetter<ElectricOoglyBooglyVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.ELECTRIC_OOGLY_BOOGLY);
	}

	public final ParticleOptions fumesParticle;
	public final float explosionPower;

	public ElectricOoglyBooglyVariant(Component name, Identifier texture, SpawnPrioritySelectors spawnConditions, ParticleOptions fumesParticle, float explosionPower) {
		super(name, texture, spawnConditions);

		this.fumesParticle = fumesParticle;
		this.explosionPower = explosionPower;
	}
}
