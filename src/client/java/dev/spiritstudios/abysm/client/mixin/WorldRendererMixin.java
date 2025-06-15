package dev.spiritstudios.abysm.client.mixin;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
	private void doNotRenderSkyUnderwater(FrameGraphBuilder frameGraphBuilder, Camera camera, float tickProgress, Fog fog, CallbackInfo ci) {
		if (camera.getSubmersionType() == CameraSubmersionType.WATER) {
			ci.cancel();
		}
	}
}
