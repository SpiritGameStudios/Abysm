package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class SimpleFishRenderer<R extends LivingEntityRenderState & GeoRenderState, T extends LivingEntity & GeoAnimatable> extends GeoEntityRenderer<T, R> {
	public static <T extends LivingEntity & GeoAnimatable> EntityRendererProvider<T> factory(String id, boolean genericAnim) {
		return factory(Abysm.id(id), genericAnim);
	}

	public static <T extends LivingEntity & GeoAnimatable> EntityRendererProvider<T> factory(ResourceLocation id, boolean genericAnim) {
		return ctx -> new SimpleFishRenderer<>(ctx, id, genericAnim);
	}

	public SimpleFishRenderer(EntityRendererProvider.Context context, ResourceLocation modelId, boolean genericAnim) {
		super(context, new SimpleFishEntityModel<>(modelId, genericAnim));
	}

	public static class SimpleFishEntityModel<T extends LivingEntity & GeoAnimatable> extends DefaultedEntityGeoModel<T> {
		public SimpleFishEntityModel(ResourceLocation id, boolean genericAnim) {
			super(id, false);

			if (genericAnim) this.withAltAnimations(Abysm.id("generic_fish"));
		}
	}
}
