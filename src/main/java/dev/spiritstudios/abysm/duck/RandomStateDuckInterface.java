package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.world.level.levelgen.noise.NoiseConfigAttachment;
import net.minecraft.server.level.ServerLevel;

public interface RandomStateDuckInterface {
	void abysm$attachBonusFunctions(ServerLevel world);

	NoiseConfigAttachment abysm$getAttachment();
}
