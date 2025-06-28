package dev.spiritstudios.abysm.client.mixin.harpoon;

import dev.spiritstudios.abysm.client.duck.HarpoonItemRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin implements HarpoonItemRenderState {

	@Unique
	private boolean abysm$renderHarpoon = false;

	@Override
	public void abysm$setShouldRenderHarpoon(boolean renderHarpoon) {
		this.abysm$renderHarpoon = renderHarpoon;
	}

	@Override
	public boolean abysm$shouldRenderHarpoon() {
		return this.abysm$renderHarpoon;
	}
}
