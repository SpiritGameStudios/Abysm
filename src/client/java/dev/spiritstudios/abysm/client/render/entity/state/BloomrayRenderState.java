package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.spectre.api.client.model.animation.AnimationControllerRenderState;
import dev.spiritstudios.spectre.api.core.math.Query;

public class BloomrayRenderState extends VariantState<BloomrayEntityVariant> {
	public final Query query = new Query();
	public final AnimationControllerRenderState antenna = new AnimationControllerRenderState();
}
