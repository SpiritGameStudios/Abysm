package dev.spiritstudios.abysm.client.mixin.sound;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.audio.Channel;
import dev.spiritstudios.abysm.client.sound.AbysmAL;
import dev.spiritstudios.abysm.registry.tags.AbysmSoundEventTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
	@WrapOperation(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V"))
	private static void play(ChannelAccess.ChannelHandle sourceManager, Consumer<Channel> action, Operation<Void> original, @Local(argsOnly = true) SoundInstance instance) {
		Minecraft client = Minecraft.getInstance();
		LocalPlayer player = client.player;

		if (player == null ||
			!player.isUnderWater() ||
			instance.getSource() == SoundSource.MUSIC ||
			instance.getSource() == SoundSource.AMBIENT ||
			instance.getSource() == SoundSource.UI ||
			BuiltInRegistries.SOUND_EVENT.get(instance.getLocation())
				.map(entry -> entry.is(AbysmSoundEventTags.UNEFFECTED_BY_WATER))
				.orElse(false)) {
			original.call(sourceManager, action);
			return;
		}

		sourceManager.execute(AbysmAL::applyUnderwater);

		original.call(sourceManager, action);
	}

	@Inject(method = "loadLibrary", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/audio/Listener;reset()V"))
	private void start(CallbackInfo ci) {
		AbysmAL.init();
	}
}
