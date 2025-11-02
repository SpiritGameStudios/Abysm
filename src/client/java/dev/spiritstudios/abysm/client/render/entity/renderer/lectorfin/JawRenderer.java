package dev.spiritstudios.abysm.client.render.entity.renderer.lectorfin;

import dev.spiritstudios.abysm.client.render.GeoUtil;
import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animatable.processing.AnimationProcessor;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class JawRenderer extends FishEnchantmentRenderer {

	public static final JawRenderer INSTANCE = new JawRenderer();

	protected JawRenderer() {
		super(new JawModel(AbysmFishEnchantments.JAW.location()));
	}

	public static class JawModel extends ExtraModel {

		public JawModel(ResourceLocation id) {
			super(id);
		}

		@Override
		public void setCustomAnimations(AnimationState<LectorfinEntity> animationState) {
			super.setCustomAnimations(animationState);

			AnimationProcessor<LectorfinEntity> animationProcessor = this.getAnimationProcessor();
			GeoBone upperJaw = animationProcessor.getBone("upperJaw");
			GeoBone lowerJaw = animationProcessor.getBone("lowerJaw");
			if (upperJaw == null && lowerJaw == null) {
				return;
			}
			float age = 0.5F * (float) GeoUtil.getAge(animationState.renderState(), 1);
			float rot = (Mth.sin(age + animationState.partialTick()) + 1) * 0.5F;
			if (upperJaw != null) upperJaw.setRotX(rot);
			if (lowerJaw != null) lowerJaw.setRotX(-rot);
		}
	}
}
