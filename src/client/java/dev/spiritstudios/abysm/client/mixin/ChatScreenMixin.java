package dev.spiritstudios.abysm.client.mixin;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.networking.UserTypedForbiddenWordC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

	protected ChatScreenMixin(Component title) {
		super(title);
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Inject(method = "onEdited", at = @At("RETURN"))
	private void magicWord(String chatText, CallbackInfo ci) {
		if (chatText.startsWith("/")) {
			return;
		}
		if (chatText.toLowerCase(Locale.ROOT).replaceAll("\\s", "").contains("\u006F\u0062\u0061\u0062\u006F")) {
			ClientPlayNetworking.send(UserTypedForbiddenWordC2SPayload.INSTANCE);
		}
		if (chatText.toLowerCase(Locale.ROOT).hashCode() == -181037584) {
			Abysm.LOGGER.info(":3");
			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.CAT_PURREOW, 1F));
		}
	}
}
