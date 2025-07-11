package dev.spiritstudios.abysm.client.sound;

import static org.lwjgl.openal.EXTEfx.*;

public class LowPassFilter extends Filter {
	public float gain = 1.0F;
	public float gainHF = 1.0F;


	public LowPassFilter() {
		super();

		alFilteri(this.id, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
		ALException.assertOk();
	}

	public LowPassFilter(float gain, float gainHF) {
		this();

		this.gain = gain;
		this.gainHF = gainHF;
	}

	@Override
	public void apply() {
		alFilterf(id, AL_LOWPASS_GAIN, gain);
		alFilterf(id, AL_LOWPASS_GAINHF, gainHF);
		ALException.assertOk();
	}
}
