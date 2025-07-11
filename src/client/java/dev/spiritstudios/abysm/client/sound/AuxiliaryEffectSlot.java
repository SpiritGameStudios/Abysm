package dev.spiritstudios.abysm.client.sound;

import dev.spiritstudios.abysm.client.mixin.SourceAccessor;
import net.minecraft.client.sound.Source;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.EXTEfx.*;

public class AuxiliaryEffectSlot implements NativeResource {
	private final @Nullable Filter filter;
	private final int id;

	public AuxiliaryEffectSlot(@Nullable Filter filter) {
		this.filter = filter;
		id = alGenAuxiliaryEffectSlots();
		alAuxiliaryEffectSloti(id, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
	}

	public void apply(Source soundSource) {
		if (filter != null) filter.apply();

		alSource3i(
			((SourceAccessor) soundSource).getPointer(),
			AL_AUXILIARY_SEND_FILTER,
			id, 1, filter == null ? AL_FILTER_NULL : filter.id
		);
	}

	@Override
	public void free() {
		alDeleteAuxiliaryEffectSlots(id);
		if (filter != null) filter.free();
	}
}
