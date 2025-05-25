package dev.spiritstudios.abysm;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Abysm implements ModInitializer {
    public static final String MODID = "abysm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
		AbysmBlocks.init();
    }

	public static Identifier id(String path) {
		return Identifier.of(MODID, path);
	}
}
