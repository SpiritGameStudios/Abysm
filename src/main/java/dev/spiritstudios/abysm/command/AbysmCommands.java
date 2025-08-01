package dev.spiritstudios.abysm.command;

import dev.spiritstudios.abysm.command.debug.EcosystemGlowCommand;
import dev.spiritstudios.specter.api.core.SpecterGlobals;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class AbysmCommands {

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
			if (SpecterGlobals.DEBUG) {
				EcosystemGlowCommand.register(dispatcher, registryAccess);
			}
		});
	}

}
