package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;

@SuppressWarnings("UnstableApiUsage")
public class AbysmAttachments {
	public static final Identifier ECOSYSTEM_CHUNK_ID = Abysm.id("ecosystem_chunk");
	public static final AttachmentType<EcosystemChunk> ECOSYSTEM_CHUNK = AttachmentRegistry.create(ECOSYSTEM_CHUNK_ID);
}
