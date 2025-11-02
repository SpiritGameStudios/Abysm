package dev.spiritstudios.abysm.client.sound;

import dev.spiritstudios.abysm.client.mixin.sound.ChannelAccessor;
import org.lwjgl.system.NativeResource;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.EXTEfx.*;

import com.mojang.blaze3d.audio.Channel;

public abstract class Filter implements NativeResource {
	protected final int id;

	protected Filter() {
		this.id = alGenFilters();
		ALException.assertOk();
	}

	@Override
	public void free() {
		alDeleteFilters(id);
	}

	public abstract void apply();

	public void applyDirect(Channel source) {
		this.apply();

		alSourcei(((ChannelAccessor) source).getSource(), AL_DIRECT_FILTER, id);
		ALException.assertOk();
	}

}
