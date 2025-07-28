package dev.spiritstudios.abysm.command;

import dev.spiritstudios.abysm.command.debug.EcosystemGlowCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.SharedConstants;

public class AbysmCommands {

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
			if(SharedConstants.isDevelopment) {
				EcosystemGlowCommand.register(dispatcher, registryAccess);
			}
		});
	}

}
