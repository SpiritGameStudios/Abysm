package dev.spiritstudios.abysm.client.mixin.render;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Inject(method = "addSkyPass", at = @At("HEAD"), cancellable = true)
	private void doNotRenderSkyUnderwater(FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice shaderFog, CallbackInfo ci) {
		if (camera.getFluidInCamera() == FogType.WATER) {
			ci.cancel();
		}
	}
}
