package dev.spiritstudios.abysm.client.sound;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import static org.lwjgl.openal.AL10.*;

public class AbysmEffects {
	private static AuxiliaryEffectSlot underwaterEffect;
	private static Filter underwaterLowpass;

	public static AuxiliaryEffectSlot underwaterEffect() {
		return underwaterEffect;
	}

	public static Filter underwaterLowpass() {
		return underwaterLowpass;
	}

	static {
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			if (underwaterLowpass != null) underwaterLowpass.free();
		});
	}

	public static void init() {
		if (underwaterLowpass != null) underwaterLowpass.free();

		// So basically, somewhere in the vanilla sound code, something is broken.
		// I have no clue how, or where, but it throws an invalid name error.
		// If I don't reset the error by calling alGetError, then the game will crash whenever I do any error handling.
		alGetError();

		underwaterLowpass = new LowPassFilter(
			1F,
			0.05F
		);

		underwaterEffect = new AuxiliaryEffectSlot(null,
			new ReverbEffect.Builder()
				.density(0.5F)
				.diffusion(1.0F)
				.gain(0.8F)
				.decayTime(20)
				.reflectionsGain(1.0F)
				.echoDepth(0.6F)
				.airAbsorptionGainHF(0.9F)
				.build()
		);
	}
}
