package dev.spiritstudios.abysm.client.mixin;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import dev.spiritstudios.abysm.client.render.LightmapAdjustment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
	@Shadow @Final private MinecraftClient client;
	@Shadow @Final private GpuTexture glTexture;

	@Unique private GpuTexture secondaryGlTexture;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initDummy(GameRenderer renderer, MinecraftClient client, CallbackInfo ci) {
		GpuDevice gpuDevice = RenderSystem.getDevice();
		this.secondaryGlTexture = gpuDevice.createTexture("Light Texture", TextureFormat.RGBA8, 16, 16, 1);
		this.secondaryGlTexture.setTextureFilter(FilterMode.LINEAR, false);
		gpuDevice.createCommandEncoder().clearColorTexture(this.secondaryGlTexture, -1);
	}

	@Inject(method = "close", at = @At("RETURN"))
	private void closeDummy(CallbackInfo ci) {
		this.secondaryGlTexture.close();
	}

	@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
	private void tweakLightmap(float tickProgress, CallbackInfo ci) {
		ClientWorld clientWorld = this.client.world;
		ClientPlayerEntity player = this.client.player;

		// these should not be null at this point
		assert clientWorld != null;
		assert player != null;

		LightmapAdjustment.adjustLightmap(player, clientWorld, this.glTexture, this.secondaryGlTexture, tickProgress);
	}
}
