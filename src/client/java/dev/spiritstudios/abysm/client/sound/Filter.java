package dev.spiritstudios.abysm.client.sound;

import dev.spiritstudios.abysm.client.mixin.sound.SourceAccessor;
import net.minecraft.client.sound.Source;
import org.lwjgl.system.NativeResource;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.EXTEfx.*;

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

	public void applyDirect(Source source) {
		this.apply();

		alSourcei(((SourceAccessor) source).getPointer(), AL_DIRECT_FILTER, id);
		ALException.assertOk();
	}

}
