package dev.spiritstudios.abysm.client.sound;

import static org.lwjgl.openal.EXTEfx.*;

public class EchoEffect extends Effect {
	public float delay = AL_ECHO_DEFAULT_DELAY;
	public float lrDelay = AL_ECHO_DEFAULT_LRDELAY;
	public float damping = AL_ECHO_DEFAULT_DAMPING;
	public float spread = AL_ECHO_DEFAULT_SPREAD;

	public EchoEffect() {
		super();

		alEffecti(id, AL_EFFECT_TYPE, AL_EFFECT_ECHO);
	}

	@Override
	public void apply() {
		alEffectf(id, AL_ECHO_DELAY, delay);
		alEffectf(id, AL_ECHO_LRDELAY, lrDelay);
		alEffectf(id, AL_ECHO_DAMPING, damping);
		alEffectf(id, AL_ECHO_SPREAD, spread);
		ALException.assertOk();
	}
}
