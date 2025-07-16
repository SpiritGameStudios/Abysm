package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.entry.RegistryEntry;

public final class AbysmTrackedDataHandlers {
	public static final TrackedDataHandler<EntityPattern> ENTITY_PATTERN = register("entity_pattern", EntityPattern.PACKET_CODEC);

	public static final TrackedDataHandler<RegistryEntry<BloomrayEntityVariant>> BLOOMRAY_VARIANT = register("bloomray_variant", BloomrayEntityVariant.ENTRY_PACKET_CODEC);
	public static final TrackedDataHandler<RegistryEntry<ElectricOoglyBooglyVariant>> ELECTRIC_OOGLY_BOOGLY_VARIANT = register("electric_oogly_boogly_variant", ElectricOoglyBooglyVariant.ENTRY_PACKET_CODEC);

	public static final TrackedDataHandler<RegistryEntry<FishEnchantment>> FISH_ENCHANTMENT = register("fish_enchantment", FishEnchantment.ENTRY_PACKET_CODEC);

	private static <T> TrackedDataHandler<T> register(String path, PacketCodec<? super RegistryByteBuf, T> codec) {
		TrackedDataHandler<T> handler = TrackedDataHandler.create(codec);
		FabricTrackedDataRegistry.register(Abysm.id(path), handler);
		return handler;
	}

	public static void init() {
		// NO-OP
	}
}
