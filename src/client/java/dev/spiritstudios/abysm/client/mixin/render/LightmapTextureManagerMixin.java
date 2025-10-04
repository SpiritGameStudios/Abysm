package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import dev.spiritstudios.abysm.client.render.LightmapAdjustment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.MappableRingBuffer;
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

	@Shadow
	@Final private GpuTextureView glTextureView;
	@Unique private GpuTexture abysm$secondaryGlTexture;
	@Unique private GpuTextureView abysm$secondaryView;
	@Unique private MappableRingBuffer abysm$uniformBuffer;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initDummy(GameRenderer renderer, MinecraftClient client, CallbackInfo ci) {
		GpuDevice gpuDevice = RenderSystem.getDevice();
		this.abysm$secondaryGlTexture = gpuDevice.createTexture("Abysm Light Texture", GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING, TextureFormat.RGBA8, 16, 16, 1, 1);
		this.abysm$secondaryGlTexture.setTextureFilter(FilterMode.LINEAR, false);
		this.abysm$secondaryView = gpuDevice.createTextureView(abysm$secondaryGlTexture);

		this.abysm$uniformBuffer = new MappableRingBuffer(() -> "Abysm Lightmap Adjustment UBO", 130, LightmapAdjustment.UBO_SIZE);

	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/lang/String;ILcom/mojang/blaze3d/textures/TextureFormat;IIII)Lcom/mojang/blaze3d/textures/GpuTexture;"))
	private GpuTexture addCopySrc(GpuDevice instance, String label, int usage, TextureFormat textureFormat, int width, int height, int depthOrLayers, int mipLevels, Operation<GpuTexture> original) {
		return original.call(instance, label, usage | GpuTexture.USAGE_COPY_SRC, textureFormat, width, height, depthOrLayers, mipLevels);
	}

	@Inject(method = "close", at = @At("RETURN"))
	private void closeDummy(CallbackInfo ci) {
		this.abysm$secondaryGlTexture.close();
	}

	@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
	private void tweakLightmap(float tickProgress, CallbackInfo ci) {
		ClientPlayerEntity player = this.client.player;

		// player should not be null
		Objects.requireNonNull(player);

		LightmapAdjustment.adjustLightmap(player, this.glTexture, this.glTextureView, this.abysm$secondaryGlTexture, this.abysm$secondaryView, abysm$uniformBuffer, tickProgress);
	}
}
