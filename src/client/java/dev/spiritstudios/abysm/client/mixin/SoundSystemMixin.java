package dev.spiritstudios.abysm.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.client.sound.AbysmEffects;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.Source;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
	@WrapOperation(method = {"method_19752", "method_19755"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/Source;play()V"))
	private static void play(Source source, Operation<Void> original) {
//		if (instance.getCategory() == SoundCategory.MUSIC) {
//			original.call(sourceManager, action);
//			return;
//		}


		AbysmEffects.UNDERWATER.applyDirect(source);
		original.call(source);
	}
}
