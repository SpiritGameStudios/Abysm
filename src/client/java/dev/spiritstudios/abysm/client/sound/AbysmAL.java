package dev.spiritstudios.abysm.client.sound;

import com.mojang.blaze3d.audio.Channel;
import dev.spiritstudios.abysm.Abysm;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class AbysmAL {
	private static boolean enabled = false;

	private static AuxiliaryEffectSlot underwaterEffectSlot;
	private static LowPassFilter underwaterLowPass;

	public static void applyUnderwater(Channel source) {
		if (!enabled) return;

		underwaterEffectSlot.apply(source);
		underwaterLowPass.applyDirect(source);
	}

	static {
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> cleanup());
	}

	public static void init() {
		cleanup();

//		if (!AbysmConfig.INSTANCE.underwaterSoundFilters.get()) return;

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

		underwaterLowPass = new LowPassFilter();

		underwaterEffectSlot = new AuxiliaryEffectSlot(null, new ReverbEffect.Builder()
			.density(0.5F)
			.diffusion(1.0F)
			.gain(0.6F)
			.gainHF(0.3F)
			.decayTime(20)
			.reflectionsGain(0.5F)
			.echoDepth(0.6F)
			.airAbsorptionGainHF(0.98F)
			.build());
	}

	public static void disable() {
		if (!enabled) return;

		if (underwaterLowPass != null) {
			underwaterLowPass.gain = 0.0F;
			underwaterLowPass.gainHF = 0.0F;
			underwaterLowPass.apply();
		}

		if (underwaterEffectSlot != null) {
			underwaterEffectSlot.disable();
		}

		enabled = false;
	}

	public static void enable() {
		if (enabled) return;

		if (underwaterLowPass != null) {
			underwaterLowPass.gain = 1.0F;
			underwaterLowPass.gainHF = 0.05F;
			underwaterLowPass.apply();
		}

		if (underwaterEffectSlot != null) {
			underwaterEffectSlot.enable();
		}

		enabled = true;
	}


	private static void cleanup() {
		if (underwaterEffectSlot != null) underwaterEffectSlot.free();
		if (underwaterLowPass != null) underwaterLowPass.free();

		enabled = false;
	}
}
