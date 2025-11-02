package dev.spiritstudios.abysm.client.mixin.render;

import dev.spiritstudios.abysm.client.duck.EntityRenderStateDuck;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin extends EntityRenderState implements EntityRenderStateDuck {

	@Unique
	private boolean abysm$isBlue = false;

	@Override
	public void abysm$setBlue(boolean isBlue) {
		this.abysm$isBlue = isBlue;
	}

	@Override
	public boolean abysm$isBlue() {
		return this.abysm$isBlue;
	}
}
