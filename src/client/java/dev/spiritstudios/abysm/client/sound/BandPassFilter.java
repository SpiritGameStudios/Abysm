package dev.spiritstudios.abysm.client.sound;

import static org.lwjgl.openal.EXTEfx.*;

public class BandPassFilter extends Filter {
	public float gain = 1.0F;
	public float gainLF = 1.0F;
	public float gainHF = 1.0F;


	public BandPassFilter() {
		super();

		alFilteri(this.id, AL_FILTER_TYPE, AL_FILTER_BANDPASS);
	}

	public BandPassFilter(float gain, float gainLF, float gainHF) {
		this();

		this.gain = gain;
		this.gainLF = gainLF;
		this.gainHF = gainHF;
	}

	@Override
	public void apply() {
		alFilterf(id, AL_BANDPASS_GAIN, gain);
		alFilterf(id, AL_BANDPASS_GAINLF, gainLF);
		alFilterf(id, AL_BANDPASS_GAINHF, gainHF);
	}
}
