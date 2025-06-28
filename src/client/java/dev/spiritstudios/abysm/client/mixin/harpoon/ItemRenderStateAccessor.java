package dev.spiritstudios.abysm.client.mixin.harpoon;

import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderState.class)
public interface ItemRenderStateAccessor {

	@Invoker("getFirstLayer")
	ItemRenderState.LayerRenderState abysm$invokeGetFirstLayer();

	@Accessor("displayContext")
	ItemDisplayContext abysm$getDisplayContext();

	@Mixin(ItemRenderState.LayerRenderState.class)
	interface LayerRenderStateAccessor {

		@Accessor("transform")
		Transformation abysm$getTransform();
	}
}
