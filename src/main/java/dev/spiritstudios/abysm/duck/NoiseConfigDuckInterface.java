package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.worldgen.noise.NoiseConfigAttachment;
import net.minecraft.server.world.ServerWorld;

public interface NoiseConfigDuckInterface {
	void abysm$attachBonusFunctions(ServerWorld world);

	NoiseConfigAttachment abysm$getAttachment();
}
