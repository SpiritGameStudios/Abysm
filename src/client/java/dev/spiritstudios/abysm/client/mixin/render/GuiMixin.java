package dev.spiritstudios.abysm.client.mixin.render;

import dev.spiritstudios.abysm.entity.effect.AbysmStatusEffects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Gui.class)
public abstract class GuiMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	@Final
	public static ResourceLocation NAUSEA_LOCATION;

	@Inject(method = "renderCameraOverlays", at = @At("RETURN"))
	private void renderBlueOverlay(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
		LocalPlayer clientPlayerEntity = this.minecraft.player;
		Objects.requireNonNull(clientPlayerEntity);

		float tickProgress = tickCounter.getGameTimeDeltaPartialTick(false);
		float blueFadeFactor = clientPlayerEntity.getEffectBlendFactor(AbysmStatusEffects.BLUE, tickProgress);
		if (blueFadeFactor > 0.0F) {
			this.abysm$renderBlueOverlay(context, blueFadeFactor);
		}
	}

	@Unique
	private void abysm$renderBlueOverlay(GuiGraphics context, float blueStrength) {
		int scaledWidth = context.guiWidth();
		int scaledHeight = context.guiHeight();

		context.pose().pushMatrix();

		float scale = Mth.lerp(blueStrength, 2.0F, 1.5F);
		context.pose().translate(scaledWidth / 2.0F, scaledHeight / 2.0F);
		context.pose().scale(scale, scale);
		context.pose().translate(-scaledWidth / 2.0F, -scaledHeight / 2.0F);

		float red = 0.08F * blueStrength;
		float green = 0.15F * blueStrength;
		float blue = 0.3F * blueStrength;

		context.blit(
			RenderPipelines.GUI_NAUSEA_OVERLAY,
			NAUSEA_LOCATION,
			0, 0,
			0.0F, 0.0F,
			scaledWidth, scaledHeight,
			scaledWidth, scaledHeight,
			ARGB.colorFromFloat(1.0F, red, green, blue));

		context.pose().popMatrix();
	}
}
