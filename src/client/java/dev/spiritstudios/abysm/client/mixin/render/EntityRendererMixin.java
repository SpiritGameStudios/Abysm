package dev.spiritstudios.abysm.client.mixin.render;

import dev.spiritstudios.abysm.client.render.AbysmRenderStateKeys;
import dev.spiritstudios.abysm.world.entity.effect.BlueEffect;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState, M extends EntityModel<? super S>> {
	@Inject(method = "extractRenderState", at = @At("RETURN"))
	private void extractAbysmState(T entity, S state, float f, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity) {
			state.setData(AbysmRenderStateKeys.IS_BLUE, BlueEffect.isBlue(livingEntity));
		}
	}
}
