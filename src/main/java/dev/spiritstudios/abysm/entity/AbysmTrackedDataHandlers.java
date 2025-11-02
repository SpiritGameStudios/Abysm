package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.data.variant.GupGupEntityVariant;
import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

public final class AbysmTrackedDataHandlers {
	public static final EntityDataSerializer<EntityPattern> ENTITY_PATTERN = register("entity_pattern", EntityPattern.PACKET_CODEC);

	public static final EntityDataSerializer<Holder<BloomrayEntityVariant>> BLOOMRAY_VARIANT = register("bloomray_variant", BloomrayEntityVariant.ENTRY_PACKET_CODEC);
	public static final EntityDataSerializer<Holder<ElectricOoglyBooglyVariant>> ELECTRIC_OOGLY_BOOGLY_VARIANT = register("electric_oogly_boogly_variant", ElectricOoglyBooglyVariant.ENTRY_PACKET_CODEC);
	public static final EntityDataSerializer<Holder<GupGupEntityVariant>> GUP_GUP_VARIANT = register("gup_gup_variant", GupGupEntityVariant.ENTRY_PACKET_CODEC);
	public static final EntityDataSerializer<Holder<SnapperEntityVariant>> SNAPPER_VARIANT = register("snapper_variant", SnapperEntityVariant.ENTRY_PACKET_CODEC);

	public static final EntityDataSerializer<Holder<FishEnchantment>> FISH_ENCHANTMENT = register("fish_enchantment", FishEnchantment.ENTRY_PACKET_CODEC);

	private static <T> EntityDataSerializer<T> register(String path, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		EntityDataSerializer<T> handler = EntityDataSerializer.forValueType(codec);
		FabricTrackedDataRegistry.register(Abysm.id(path), handler);
		return handler;
	}

	public static void init() {
		// NO-OP
	}
}
