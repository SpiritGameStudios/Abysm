package dev.spiritstudios.abysm.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.spiritstudios.abysm.client.render.entity.lectorfin.FishEnchantmentRenderer;
import dev.spiritstudios.abysm.client.render.entity.lectorfin.LectorfinEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Mixin(value = GeoEntityRenderer.class, priority = 500)
public abstract class GeoEntityRendererMixin {

	@ModifyVariable(method = "actuallyRender(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/VertexConsumer;ZIII)V", at = @At("HEAD"), index = 7, argsOnly = true)
	private boolean applyTransformsOnEnchantment(boolean value, @Local(argsOnly = true) EntityRenderState state, @Share("whatWasReRendering")LocalBooleanRef localBooleanRef) {
		if (!((GeoEntityRenderer) (Object) this instanceof LectorfinEntityRenderer)) {
			return value;
		}
		localBooleanRef.set(value);
		GeoRenderState geoRenderState = (GeoRenderState) state;
		if (!geoRenderState.hasGeckolibData(FishEnchantmentRenderer.RENDERING_ENCHANTMENT)) {
			return value;
		}
		Boolean isRenderingEnchantment = geoRenderState.getGeckolibData(FishEnchantmentRenderer.RENDERING_ENCHANTMENT);
		if (isRenderingEnchantment == null) {
			return value;
		}
		return value && !isRenderingEnchantment;
	}

	@ModifyVariable(method = "actuallyRender(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/VertexConsumer;ZIII)V", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;<init>(Lorg/joml/Matrix4fc;)V"), index = 7, argsOnly = true, remap = false)
	private boolean reset(boolean value, @Share("whatWasReRendering")LocalBooleanRef localBooleanRef) {
		if (!((GeoEntityRenderer) (Object) this instanceof LectorfinEntityRenderer)) {
			return value;
		}
		return localBooleanRef.get();
	}
}
