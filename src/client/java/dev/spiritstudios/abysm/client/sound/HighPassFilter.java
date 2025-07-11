package dev.spiritstudios.abysm.client.sound;

import static org.lwjgl.openal.EXTEfx.*;

public class HighPassFilter extends Filter {
	public float gain = 1.0F;
	public float gainLF = 1.0F;


	public HighPassFilter() {
		super();

		alFilteri(this.id, AL_FILTER_TYPE, AL_FILTER_HIGHPASS);
	}

	public HighPassFilter(float gain, float gainLF) {
		this();

		this.gain = gain;
		this.gainLF = gainLF;
	}

	@Override
	public void apply() {
		alFilterf(id, AL_HIGHPASS_GAIN, gain);
		alFilterf(id, AL_HIGHPASS_GAINLF, gainLF);
	}
}
