package dev.spiritstudios.abysm.client.sound;

import dev.spiritstudios.abysm.client.mixin.sound.ChannelAccessor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;

import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.EXTEfx.*;

import com.mojang.blaze3d.audio.Channel;

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

	public void disable() {
		alAuxiliaryEffectSloti(id, AL_EFFECTSLOT_EFFECT, AL_EFFECT_NULL);
	}

	public void enable() {
		alAuxiliaryEffectSloti(id, AL_EFFECTSLOT_EFFECT, effect.id);
	}

	public void apply(Channel soundSource) {
		effect.apply();


		if (filter != null) filter.apply();

		alSource3i(
			((ChannelAccessor) soundSource).getSource(),
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
