package dev.spiritstudios.abysm.client.mixin.render;

import dev.spiritstudios.abysm.client.duck.EntityRenderStateDuck;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState, M extends EntityModel<? super S>> {

	@Inject(method = "updateRenderState", at = @At("RETURN"))
	private void updateCustomStateValues(T entity, S entityRenderState, float f, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity && entityRenderState instanceof EntityRenderStateDuck duck) {
			boolean blue = BlueEffect.isBlue(livingEntity);
			duck.abysm$setBlue(blue);
		}
	}
}
