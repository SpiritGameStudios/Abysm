package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import dev.spiritstudios.abysm.client.AbysmClient;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
	@Inject(method = "getFogColor", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", ordinal = 0))
	private static void reduceUnderwaterVisibility(
		Camera camera,
		float tickProgress,
		ClientWorld world,
		int viewDistance,
		float skyDarkness,
		boolean thick,
		CallbackInfoReturnable<Vector4f> cir,
		@Local(ordinal = 7) LocalFloatRef underwaterVisibilityRef
	) {
		if (camera.getSubmersionType() == CameraSubmersionType.WATER) {
			// adjust underwater visibility
			float visibilityMultiplier = MathHelper.lerp(tickProgress, AbysmClient.underwaterVisibilityMultiplier, AbysmClient.nextUnderwaterVisibilityMultiplier);

			if (visibilityMultiplier < (1.0F - MathHelper.EPSILON)) {
				underwaterVisibilityRef.set(underwaterVisibilityRef.get() * visibilityMultiplier);
			}
		}
	}
}
