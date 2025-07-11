package dev.spiritstudios.abysm.client.sound;

import org.lwjgl.system.NativeResource;

import static org.lwjgl.openal.EXTEfx.*;

public abstract class Effect implements NativeResource {
	protected final int id;

	protected Effect() {
		this.id = alGenEffects();
		ALException.assertOk();
	}

	@Override
	public void free() {
		alDeleteEffects(id);
	}

	public abstract void apply();

}
