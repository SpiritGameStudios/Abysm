package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;

public class ElectricOoglyBooglyVariant extends AbstractEntityVariant {
	public static final Codec<ElectricOoglyBooglyVariant> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			AbstractEntityVariant.getNameCodec(),
			AbstractEntityVariant.getTextureCodec(),
			Codecs.RGB.fieldOf("electricity_color").forGetter(variant -> variant.electricityColor),
			Codec.BOOL.fieldOf("deadly").forGetter(variant -> variant.deadly)
		).apply(instance, ElectricOoglyBooglyVariant::new)
	);

	public static final ElectricOoglyBooglyVariant DEFAULT = new ElectricOoglyBooglyVariant(
		Text.translatable("entity.abysm.electric_oogly_boogly.oogly"),
		AbstractEntityVariant.buildEntityTexturePath("electric_oogly_boogly"),
		ColorHelper.fromFloats(1f, 0.75f, 1f, 0.75f)
	);

	public final int electricityColor;
	public final boolean deadly;

	public ElectricOoglyBooglyVariant(Text name, Identifier texture, int electricityColor, boolean deadly) {
		super(name, texture);
		this.electricityColor = electricityColor;
		this.deadly = deadly;
	}

	public ElectricOoglyBooglyVariant(Text name, Identifier texture, int electricityColor) {
		this(name, texture, electricityColor, false);
	}

	public int getElectricityColor() {
		return electricityColor;
	}

	public boolean isDeadly() {
		return deadly;
	}

	// Helper methods
	public static ElectricOoglyBooglyVariant fromIntId(DynamicRegistryManager registryManager, int id) {
		return AbstractEntityVariant.fromIntId(AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT, DEFAULT, registryManager, id);
	}

	public static int toIntId(DynamicRegistryManager registryManager, ElectricOoglyBooglyVariant variant) {
		return AbstractEntityVariant.toIntId(AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT, registryManager, variant);
	}

	public static int getDefaultIntId(DynamicRegistryManager registryManager) {
		return toIntId(registryManager, DEFAULT);
	}

}
