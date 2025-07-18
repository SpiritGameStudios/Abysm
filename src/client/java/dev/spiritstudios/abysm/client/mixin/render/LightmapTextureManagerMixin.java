package dev.spiritstudios.abysm.client.mixin.render;

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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

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
		gpuDevice.createCommandEncoder().clearColorTexture(this.secondaryGlTexture, 0xFFFFFFFF);
	}

	@Inject(method = "close", at = @At("RETURN"))
	private void closeDummy(CallbackInfo ci) {
		this.secondaryGlTexture.close();
	}

	@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
	private void tweakLightmap(float tickProgress, CallbackInfo ci) {
		ClientPlayerEntity player = this.client.player;

		// player should not be null
		Objects.requireNonNull(player);

		LightmapAdjustment.adjustLightmap(player, this.glTexture, this.secondaryGlTexture, tickProgress);
	}
}
