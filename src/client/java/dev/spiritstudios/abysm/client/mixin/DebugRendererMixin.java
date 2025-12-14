package dev.spiritstudios.abysm.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.client.debug.AbysmDebugScreenEntries;
import dev.spiritstudios.abysm.client.debug.ecosystem.EcosystemEntityDebugRenderer;
import dev.spiritstudios.abysm.client.debug.ecosystem.EcosystemPopulationDebugRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin {
	@Shadow
	@Final
	private List<DebugRenderer.SimpleDebugRenderer> renderers;

	@Inject(method = "refreshRendererList", at = @At("TAIL"))
	private void addAbysmDebugRenderers(CallbackInfo ci, @Local Minecraft minecraft) {
		if (minecraft.debugEntries.isCurrentlyEnabled(AbysmDebugScreenEntries.ECOSYSTEM_ENTITY)) {
			this.renderers.add(new EcosystemEntityDebugRenderer(minecraft));
		}

		if (minecraft.debugEntries.isCurrentlyEnabled(AbysmDebugScreenEntries.ECOSYSTEM_POPULATION)) {
			this.renderers.add(new EcosystemPopulationDebugRenderer(minecraft));
		}
	}
}
