package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.data.variant.AbstractEntityVariant;
import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class VariantState<V extends AbstractEntityVariant> extends LivingEntityRenderState {
	public final Query query = new Query();
	public V variant;
}
