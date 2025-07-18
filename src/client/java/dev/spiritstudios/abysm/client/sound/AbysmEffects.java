package dev.spiritstudios.abysm.client.sound;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.AbysmConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.sound.Source;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class AbysmEffects {
	private static boolean enabled = false;

	private static AuxiliaryEffectSlot underwaterEffect;
	private static Filter underwaterLowPass;

	public static void applyUnderwater(Source source) {
		if (!enabled) return;

		underwaterEffect.apply(source);
		underwaterLowPass.applyDirect(source);
	}

	static {
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> cleanup());
	}

	public static void init() {
		cleanup();

		if (!AbysmConfig.INSTANCE.underwaterSoundFilters.get()) return;

		long contextHandle = alcGetCurrentContext();
		long deviceHandle = alcGetContextsDevice(contextHandle);

		boolean efxCompatible = alcIsExtensionPresent(deviceHandle, "ALC_EXT_EFX");

		if (!efxCompatible) {
			Abysm.LOGGER.warn("Underwater sound filters is enabled, but the current audio device is not compatible with EFX. Underwater sound filters will not work.");

			return;
		}

		// So basically, somewhere in the vanilla sound code, something is broken.
		// I have no clue how, or where, but it throws an invalid name error.
		// If I don't reset the error by calling alGetError, then the game will crash whenever I do any error handling.
		alGetError();

		// TODO: Actually tune these values a bit
		underwaterLowPass = new LowPassFilter(
			1F,
			0.05F
		);

		underwaterEffect = new AuxiliaryEffectSlot(null,
			new ReverbEffect.Builder()
				.density(0.5F)
				.diffusion(1.0F)
				.gain(0.6F)
				.gainHF(0.3F)
				.decayTime(20)
				.reflectionsGain(0.5F)
				.echoDepth(0.6F)
				.airAbsorptionGainHF(0.98F)
				.build()
		);

		enabled = true;
	}

	private static void cleanup() {
		if (underwaterEffect != null) underwaterEffect.free();
		if (underwaterLowPass != null) underwaterLowPass.free();

		enabled = false;
	}
}
