package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;

public final class AbysmTrackedDataHandlers {
	public static final TrackedDataHandler<EntityPattern> ENTITY_PATTERN = register("entity_pattern", EntityPattern.PACKET_CODEC);
	@SuppressWarnings("unused")
	public static final TrackedDataHandler<Identifier> IDENTIFIER = register("identifier", Identifier.PACKET_CODEC);
	public static final TrackedDataHandler<FishEnchantment> FISH_ENCHANTMENT = register("fish_enchantment", FishEnchantment.PACKET_CODEC);

	private static <T> TrackedDataHandler<T> register(String path, PacketCodec<? super RegistryByteBuf, T> codec) {
		TrackedDataHandler<T> handler = TrackedDataHandler.create(codec);
		FabricTrackedDataRegistry.register(Abysm.id(path), handler);
		return handler;
	}

	public static void init() {
		// NO-OP
	}
}
