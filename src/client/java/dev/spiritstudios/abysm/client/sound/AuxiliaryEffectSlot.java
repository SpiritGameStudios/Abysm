package dev.spiritstudios.abysm.client.sound;

import dev.spiritstudios.abysm.client.mixin.sound.SourceAccessor;
import net.minecraft.client.sound.Source;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.EXTEfx.*;

public class AuxiliaryEffectSlot implements NativeResource {
	private final @Nullable Filter filter;
	private final Effect effect;
	private final int id;

	public AuxiliaryEffectSlot(@Nullable Filter filter, Effect effect) {
		this.filter = filter;
		this.effect = effect;

		id = alGenAuxiliaryEffectSlots();
		ALException.assertOk();

		alAuxiliaryEffectSloti(id, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
		ALException.assertOk();
	}

	public void apply(Source soundSource) {
		effect.apply();
		alAuxiliaryEffectSloti(id, AL_EFFECTSLOT_EFFECT, effect.id);

		if (filter != null) filter.apply();

		alSource3i(
			((SourceAccessor) soundSource).getPointer(),
			AL_AUXILIARY_SEND_FILTER,
			id, 0, filter == null ? AL_FILTER_NULL : filter.id
		);
	}

	@Override
	public void free() {
		alDeleteAuxiliaryEffectSlots(id);
		effect.free();
		if (filter != null) filter.free();
	}
}
