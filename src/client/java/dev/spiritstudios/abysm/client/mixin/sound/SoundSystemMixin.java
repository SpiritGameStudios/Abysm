package dev.spiritstudios.abysm.client.mixin.sound;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.client.sound.AbysmEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.Source;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
	@WrapOperation(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/Channel$SourceManager;run(Ljava/util/function/Consumer;)V"))
	private static void play(Channel.SourceManager sourceManager, Consumer<Source> action, Operation<Void> original, @Local(argsOnly = true) SoundInstance instance) {
		// TODO: Tag for excluding certain sounds (swimming effects, sounds for vanilla underwater mobs)

		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity player = client.player;

		if (player == null ||
			!player.isSubmergedInWater() ||
			instance.getCategory() == SoundCategory.MUSIC ||
			instance.getCategory() == SoundCategory.AMBIENT ||
			instance.getCategory() == SoundCategory.MASTER) {
			original.call(sourceManager, action);
			return;
		}

		sourceManager.run(AbysmEffects::applyUnderwater);

		original.call(sourceManager, action);
	}

	@Inject(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundListener;init()V"))
	private void start(CallbackInfo ci) {
		AbysmEffects.init();
	}
}
