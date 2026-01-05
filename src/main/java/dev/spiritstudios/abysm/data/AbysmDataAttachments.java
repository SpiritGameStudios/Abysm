package dev.spiritstudios.abysm.data;

import com.mojang.serialization.Codec;
import dev.spiritstudios.abysm.Abysm;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;

@SuppressWarnings("UnstableApiUsage")
public final class AbysmDataAttachments {
	public static AttachmentType<Boolean> BLUE = AttachmentRegistry.create(
		Abysm.id("blue"),
		builder -> builder
			.persistent(Codec.BOOL)
			.syncWith(
				ByteBufCodecs.BOOL,
				AttachmentSyncPredicate.all()
			)
	);
}
