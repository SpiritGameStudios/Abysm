package dev.spiritstudios.abysm.client.mixin.render;

import dev.spiritstudios.abysm.entity.effect.AbysmStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	public static Identifier NAUSEA_TEXTURE;

	@Inject(method = "renderMiscOverlays", at = @At("RETURN"))
	private void renderBlueOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		ClientPlayerEntity clientPlayerEntity = this.client.player;
		Objects.requireNonNull(clientPlayerEntity);

		float tickProgress = tickCounter.getTickProgress(false);
		float blueFadeFactor = clientPlayerEntity.getEffectFadeFactor(AbysmStatusEffects.BLUE, tickProgress);
		if (blueFadeFactor > 0.0F) {
			this.abysm$renderBlueOverlay(context, blueFadeFactor);
		}
	}

	@Unique
	private void abysm$renderBlueOverlay(DrawContext context, float blueStrength) {
		int scaledWidth = context.getScaledWindowWidth();
		int scaledHeight = context.getScaledWindowHeight();

		context.getMatrices().pushMatrix();

		float scale = MathHelper.lerp(blueStrength, 2.0F, 1.5F);
		context.getMatrices().translate(scaledWidth / 2.0F, scaledHeight / 2.0F);
		context.getMatrices().scale(scale, scale);
		context.getMatrices().translate(-scaledWidth / 2.0F, -scaledHeight / 2.0F);

		float red = 0.08F * blueStrength;
		float green = 0.15F * blueStrength;
		float blue = 0.3F * blueStrength;

		context.drawTexture(
			RenderPipelines.GUI_NAUSEA_OVERLAY,
			NAUSEA_TEXTURE,
			0, 0,
			0.0F, 0.0F,
			scaledWidth, scaledHeight,
			scaledWidth, scaledHeight,
			ColorHelper.fromFloats(1.0F, red, green, blue));

		context.getMatrices().popMatrix();
	}
}
