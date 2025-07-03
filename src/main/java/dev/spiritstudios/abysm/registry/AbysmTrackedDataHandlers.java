package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public final class AbysmTrackedDataHandlers {
	public static final TrackedDataHandler<EntityPattern> ENTITY_PATTERN = register("entity_pattern", EntityPattern.PACKET_CODEC);

	private static <T> TrackedDataHandler<T> register(String path, PacketCodec<? super RegistryByteBuf, T> codec) {
		TrackedDataHandler<T> handler = TrackedDataHandler.create(codec);
		FabricTrackedDataRegistry.register(Abysm.id(path), handler);
		return handler;
	}

	public static void init() {
		// NO-OP
	}
}
