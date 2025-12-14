package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.client.render.AbysmRenderStateKeys;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SubmitNodeStorage.ModelSubmit.class)
public abstract class SubmitNodeStorage$ModelSubmitMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 2, argsOnly = true)
	private static <S> int replaceTint(int original, @Local(argsOnly = true) S state) {
		return state instanceof FabricRenderState fabric && Boolean.TRUE.equals(fabric.getData(AbysmRenderStateKeys.IS_BLUE)) ?
			0xFF1F3FFF :
			original;
	}
}
