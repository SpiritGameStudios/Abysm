package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public class ElectricOoglyBooglyVariant extends AbstractEntityVariant {
	public static final Codec<ElectricOoglyBooglyVariant> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			getNameCodec(),
			getTextureCodec(),
			Codecs.RGB.fieldOf("electricity_color").forGetter(variant -> variant.electricityColor),
			Codec.BOOL.fieldOf("deadly").forGetter(variant -> variant.deadly)
		).apply(instance, ElectricOoglyBooglyVariant::new)
	);

	public static final Codec<RegistryEntry<ElectricOoglyBooglyVariant>> ENTRY_CODEC = RegistryFixedCodec.of(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT);

	public static final PacketCodec<RegistryByteBuf, RegistryEntry<ElectricOoglyBooglyVariant>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT);

	public static RegistryEntry<ElectricOoglyBooglyVariant> getDefaultEntry(DynamicRegistryManager registryManager) {
		return getDefaultEntry(registryManager.getOrThrow(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT));
	}

	public static RegistryEntry<ElectricOoglyBooglyVariant> getDefaultEntry(RegistryEntryLookup<ElectricOoglyBooglyVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.ELECTRIC_OOGLY_BOOGLY);
	}

	public final int electricityColor;
	public final boolean deadly;

	public ElectricOoglyBooglyVariant(Text name, Identifier texture, int electricityColor, boolean deadly) {
		super(name, texture);
		this.electricityColor = electricityColor;
		this.deadly = deadly;
	}

	public int getElectricityColor() {
		return electricityColor;
	}

	public boolean isDeadly() {
		return deadly;
	}

}
