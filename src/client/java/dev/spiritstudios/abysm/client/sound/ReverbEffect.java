package dev.spiritstudios.abysm.client.sound;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.EXTEfx.*;

public class ReverbEffect extends Effect {
	public float density = AL_EAXREVERB_DEFAULT_DENSITY;
	public float diffusion = AL_EAXREVERB_DEFAULT_DIFFUSION;
	public float gain = AL_EAXREVERB_DEFAULT_GAIN;
	public float gainHF = AL_EAXREVERB_DEFAULT_GAINHF;
	public float gainLF = AL_EAXREVERB_DEFAULT_GAINLF;
	public float decayTime = AL_EAXREVERB_DEFAULT_DECAY_TIME;
	public float decayHFRatio = AL_EAXREVERB_DEFAULT_DECAY_HFRATIO;
	public float decayLFRatio = AL_EAXREVERB_DEFAULT_DECAY_LFRATIO;
	public float reflectionsGain = AL_EAXREVERB_DEFAULT_REFLECTIONS_GAIN;
	public float lateReverbGain = AL_EAXREVERB_DEFAULT_LATE_REVERB_GAIN;
	public float lateReverbDelay = AL_EAXREVERB_DEFAULT_LATE_REVERB_DELAY;
	public float echoTime = AL_EAXREVERB_DEFAULT_ECHO_TIME;
	public float echoDepth = AL_EAXREVERB_DEFAULT_ECHO_DEPTH;
	public float modulationTime = AL_EAXREVERB_DEFAULT_MODULATION_TIME;
	public float modulationDepth = AL_EAXREVERB_DEFAULT_MODULATION_DEPTH;
	public float airAbsorptionGainHF = AL_EAXREVERB_DEFAULT_AIR_ABSORPTION_GAINHF;
	public float hfReference = AL_EAXREVERB_DEFAULT_HFREFERENCE;
	public float lfReference = AL_EAXREVERB_DEFAULT_LFREFERENCE;
	public float roomRolloffFactor = AL_EAXREVERB_DEFAULT_ROOM_ROLLOFF_FACTOR;
	public boolean decayHFLimit = true;

	public ReverbEffect() {
		super();

		alEffecti(id, AL_EFFECT_TYPE, AL_EFFECT_EAXREVERB);
	}

	public ReverbEffect(float density, float diffusion, float gain, float gainHF, float gainLF, float decayTime, float decayHFRatio, float decayLFRatio, float reflectionsGain, float lateReverbGain, float lateReverbDelay, float echoTime, float echoDepth, float modulationTime, float modulationDepth, float airAbsorptionGainHF, float hfReference, float lfReference, float roomRolloffFactor, boolean decayHFLimit) {
		this();

		this.density = density;
		this.diffusion = diffusion;
		this.gain = gain;
		this.gainHF = gainHF;
		this.gainLF = gainLF;
		this.decayTime = decayTime;
		this.decayHFRatio = decayHFRatio;
		this.decayLFRatio = decayLFRatio;
		this.reflectionsGain = reflectionsGain;
		this.lateReverbGain = lateReverbGain;
		this.lateReverbDelay = lateReverbDelay;
		this.echoTime = echoTime;
		this.echoDepth = echoDepth;
		this.modulationTime = modulationTime;
		this.modulationDepth = modulationDepth;
		this.airAbsorptionGainHF = airAbsorptionGainHF;
		this.hfReference = hfReference;
		this.lfReference = lfReference;
		this.roomRolloffFactor = roomRolloffFactor;
		this.decayHFLimit = decayHFLimit;
	}

	@Override
	public void apply() {
		set(AL_EAXREVERB_DENSITY, density);
		set(AL_EAXREVERB_DIFFUSION, diffusion);
		set(AL_EAXREVERB_GAIN, gain);
		set(AL_EAXREVERB_GAINHF, gainHF);
		set(AL_EAXREVERB_GAINLF, gainLF);
		set(AL_EAXREVERB_DECAY_TIME, decayTime);
		set(AL_EAXREVERB_DECAY_HFRATIO, decayHFRatio);
		set(AL_EAXREVERB_DECAY_LFRATIO, decayLFRatio);
		set(AL_EAXREVERB_REFLECTIONS_GAIN, reflectionsGain);
		set(AL_EAXREVERB_LATE_REVERB_GAIN, lateReverbGain);
		set(AL_EAXREVERB_LATE_REVERB_DELAY, lateReverbDelay);
		set(AL_EAXREVERB_ECHO_TIME, echoTime);
		set(AL_EAXREVERB_ECHO_DEPTH, echoDepth);
		set(AL_EAXREVERB_MODULATION_TIME, modulationTime);
		set(AL_EAXREVERB_MODULATION_DEPTH, modulationDepth);
		set(AL_EAXREVERB_AIR_ABSORPTION_GAINHF, airAbsorptionGainHF);
		set(AL_EAXREVERB_HFREFERENCE, hfReference);
		set(AL_EAXREVERB_LFREFERENCE, lfReference);
		set(AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, roomRolloffFactor);

		alEffecti(id, AL_EAXREVERB_DECAY_HFLIMIT, decayHFLimit ? AL_TRUE : AL_FALSE);
		ALException.assertOk();
	}

	private void set(int key, float value) {
		alEffectf(id, key, value);
		ALException.assertOk();
	}

	public static class Builder {
		private float density = AL_EAXREVERB_DEFAULT_DENSITY;
		private float diffusion = AL_EAXREVERB_DEFAULT_DIFFUSION;
		private float gain = AL_EAXREVERB_DEFAULT_GAIN;
		private float gainHF = AL_EAXREVERB_DEFAULT_GAINHF;
		private float gainLF = AL_EAXREVERB_DEFAULT_GAINLF;
		private float decayTime = AL_EAXREVERB_DEFAULT_DECAY_TIME;
		private float decayHFRatio = AL_EAXREVERB_DEFAULT_DECAY_HFRATIO;
		private float decayLFRatio = AL_EAXREVERB_DEFAULT_DECAY_LFRATIO;
		private float reflectionsGain = AL_EAXREVERB_DEFAULT_REFLECTIONS_GAIN;
		private float lateReverbGain = AL_EAXREVERB_DEFAULT_LATE_REVERB_GAIN;
		private float lateReverbDelay = AL_EAXREVERB_DEFAULT_LATE_REVERB_DELAY;
		private float echoTime = AL_EAXREVERB_DEFAULT_ECHO_TIME;
		private float echoDepth = AL_EAXREVERB_DEFAULT_ECHO_DEPTH;
		private float modulationTime = AL_EAXREVERB_DEFAULT_MODULATION_TIME;
		private float modulationDepth = AL_EAXREVERB_DEFAULT_MODULATION_DEPTH;
		private float airAbsorptionGainHF = AL_EAXREVERB_DEFAULT_AIR_ABSORPTION_GAINHF;
		private float hfReference = AL_EAXREVERB_DEFAULT_HFREFERENCE;
		private float lfReference = AL_EAXREVERB_DEFAULT_LFREFERENCE;
		private float roomRolloffFactor = AL_EAXREVERB_DEFAULT_ROOM_ROLLOFF_FACTOR;
		private boolean decayHFLimit = true;

		public Builder density(float density) {
			this.density = density;
			return this;
		}

		public Builder diffusion(float diffusion) {
			this.diffusion = diffusion;
			return this;
		}

		public Builder gain(float gain) {
			this.gain = gain;
			return this;
		}

		public Builder gainHF(float gainHF) {
			this.gainHF = gainHF;
			return this;
		}

		public Builder gainLF(float gainLF) {
			this.gainLF = gainLF;
			return this;
		}

		public Builder decayTime(float decayTime) {
			this.decayTime = decayTime;
			return this;
		}

		public Builder decayHFRatio(float decayHFRatio) {
			this.decayHFRatio = decayHFRatio;
			return this;
		}

		public Builder decayLFRatio(float decayLFRatio) {
			this.decayLFRatio = decayLFRatio;
			return this;
		}

		public Builder reflectionsGain(float reflectionsGain) {
			this.reflectionsGain = reflectionsGain;
			return this;
		}

		public Builder lateReverbGain(float lateReverbGain) {
			this.lateReverbGain = lateReverbGain;
			return this;
		}

		public Builder lateReverbDelay(float lateReverbDelay) {
			this.lateReverbDelay = lateReverbDelay;
			return this;
		}

		public Builder echoTime(float echoTime) {
			this.echoTime = echoTime;
			return this;
		}

		public Builder echoDepth(float echoDepth) {
			this.echoDepth = echoDepth;
			return this;
		}

		public Builder modulationTime(float modulationTime) {
			this.modulationTime = modulationTime;
			return this;
		}

		public Builder modulationDepth(float modulationDepth) {
			this.modulationDepth = modulationDepth;
			return this;
		}

		public Builder airAbsorptionGainHF(float airAbsorptionGainHF) {
			this.airAbsorptionGainHF = airAbsorptionGainHF;
			return this;
		}

		public Builder hfReference(float hfReference) {
			this.hfReference = hfReference;
			return this;
		}

		public Builder lfReference(float lfReference) {
			this.lfReference = lfReference;
			return this;
		}

		public Builder roomRolloffFactor(float roomRolloffFactor) {
			this.roomRolloffFactor = roomRolloffFactor;
			return this;
		}

		public Builder decayHFLimit(boolean decayHFLimit) {
			this.decayHFLimit = decayHFLimit;
			return this;
		}

		public ReverbEffect build() {
			return new ReverbEffect(density, diffusion, gain, gainHF, gainLF, decayTime, decayHFRatio, decayLFRatio, reflectionsGain, lateReverbGain, lateReverbDelay, echoTime, echoDepth, modulationTime, modulationDepth, airAbsorptionGainHF, hfReference, lfReference, roomRolloffFactor, decayHFLimit);
		}
	}
}
