package dev.spiritstudios.abysm.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class AbysmCommands {

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
//				EcosystemGlowCommand.register(dispatcher, registryAccess);
			}
		});
	}

}
