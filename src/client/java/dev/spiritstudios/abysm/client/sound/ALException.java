package dev.spiritstudios.abysm.client.sound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.openal.AL10.*;

public class ALException extends RuntimeException {
	public ALException(int code, String message) {
		super("OpenAL ERROR [%s]: %s".formatted(code, message));
	}

	public ALException(int code) {
		this(code, errorMessage(code));
	}

	private static final Logger LOGGER = LogManager.getLogger();

	public static String errorMessage(int code) {
		return switch (code) {
			case AL_NO_ERROR  -> "No Error";
			case AL_INVALID_NAME -> "Invalid Name";
			case AL_INVALID_ENUM -> "Invalid Enum";
			case AL_INVALID_VALUE -> "Invalid Value";
			case AL_OUT_OF_MEMORY -> "Out of Memory";
			default -> "Unknown Error";
		};
	}

	public static void assertOk() {
		int code = alGetError();

		if (code == AL_NO_ERROR) return;
		LOGGER.error("OpenAL ERROR [{}]: {}", code, errorMessage(code));
	}
}
