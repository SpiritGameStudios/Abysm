package dev.spiritstudios.abysm.client.sound;

import static org.lwjgl.openal.EXTEfx.*;

public class BandPassFilter extends Filter {
	public float gain = AL_BANDPASS_DEFAULT_GAIN;
	public float gainLF = AL_BANDPASS_DEFAULT_GAINLF;
	public float gainHF = AL_BANDPASS_DEFAULT_GAINHF;


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
