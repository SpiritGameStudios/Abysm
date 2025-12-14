package dev.spiritstudios.abysm.client.render.entity.lectorfin;

import dev.spiritstudios.abysm.client.render.entity.state.LectorfinRenderState;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.spectre.api.client.model.animation.AnimationLocation;
import dev.spiritstudios.spectre.api.client.model.animation.SpectreKeyframeAnimation;
import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;

public class FishEnchantmentModel extends EntityModel<LectorfinRenderState> {
	private final SpectreKeyframeAnimation swim;

	public FishEnchantmentModel(EntityRendererProvider.Context context, Holder.Reference<FishEnchantment> holder) {
		super(context.bakeLayer(
			new ModelLayerLocation(holder.key().identifier(), "main")
		));

		this.swim = context.bakeAnimation(
			new AnimationLocation(holder.key().identifier(), "swim"),
			root()
		);
	}

	@Override
	public void setupAnim(LectorfinRenderState renderState) {
		super.setupAnim(renderState);

		Query query = renderState.query;

		swim.applyWalk(query, renderState.walkAnimationPos, renderState.walkAnimationSpeed, 1F, 1F);
	}
}
