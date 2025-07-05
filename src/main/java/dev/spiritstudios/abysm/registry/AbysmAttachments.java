package dev.spiritstudios.abysm.registry;

import com.mojang.serialization.Codec;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

@SuppressWarnings("UnstableApiUsage")
public class AbysmAttachments {
	public static final AttachmentType<EcosystemChunk> ECOSYSTEM_CHUNK = register("ecosystem_chunk");

	private static <T> AttachmentType<T> register(String path) {
		return AttachmentRegistry.create(Abysm.id(path));
	}

	private static <T> AttachmentType<T> registerPersistent(String path, Codec<T> codec) {
		return AttachmentRegistry.createPersistent(Abysm.id(path), codec);
	}

	private static <T> AttachmentType<T> register(String path, AttachmentRegistry.Builder<T> builder) {
		return builder.buildAndRegister(Abysm.id(path));
	}
}
