package dev.spiritstudios.abysm.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gl.GlResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* TODO: Merge into specter
Patch for a vanilla bug, validation being turned on causes breeze renderers to break.
 */
@Mixin(GlResourceManager.class)
public abstract class GlResourceManagerMixin {
	@ModifyExpressionValue(method = "setupRenderPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gl/RenderPassImpl;IS_DEVELOPMENT:Z"))
	private boolean noCrash(boolean original) {
		return false;
	}
}
