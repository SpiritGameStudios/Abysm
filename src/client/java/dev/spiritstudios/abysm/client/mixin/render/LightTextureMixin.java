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
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MappableRingBuffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
	@Shadow @Final private Minecraft minecraft;
	@Shadow @Final private GpuTexture texture;

	@Shadow
	@Final private GpuTextureView textureView;
	@Unique private GpuTexture abysm$secondaryTexture;
	@Unique private GpuTextureView abysm$secondaryView;
	@Unique private MappableRingBuffer abysm$uniformBuffer;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initDummy(GameRenderer renderer, Minecraft client, CallbackInfo ci) {
		GpuDevice gpuDevice = RenderSystem.getDevice();
		this.abysm$secondaryTexture = gpuDevice.createTexture("Abysm Light Texture", GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING, TextureFormat.RGBA8, 16, 16, 1, 1);
		this.abysm$secondaryTexture.setTextureFilter(FilterMode.LINEAR, false);
		this.abysm$secondaryView = gpuDevice.createTextureView(abysm$secondaryTexture);

		this.abysm$uniformBuffer = new MappableRingBuffer(() -> "Abysm Lightmap Adjustment UBO", 130, LightmapAdjustment.UBO_SIZE);

	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/lang/String;ILcom/mojang/blaze3d/textures/TextureFormat;IIII)Lcom/mojang/blaze3d/textures/GpuTexture;"))
	private GpuTexture addCopySrc(GpuDevice instance, String label, int usage, TextureFormat textureFormat, int width, int height, int depthOrLayers, int mipLevels, Operation<GpuTexture> original) {
		return original.call(instance, label, usage | GpuTexture.USAGE_COPY_SRC, textureFormat, width, height, depthOrLayers, mipLevels);
	}

	@Inject(method = "close", at = @At("RETURN"))
	private void closeDummy(CallbackInfo ci) {
		this.abysm$secondaryTexture.close();
		this.abysm$secondaryView.close();
		this.abysm$uniformBuffer.close();
	}

	@Inject(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
	private void tweakLightmap(float tickProgress, CallbackInfo ci) {
		LocalPlayer player = this.minecraft.player;

		// player should not be null
		Objects.requireNonNull(player);

		LightmapAdjustment.adjustLightmap(player, this.texture, this.textureView, this.abysm$secondaryTexture, this.abysm$secondaryView, abysm$uniformBuffer, tickProgress);
	}
}
