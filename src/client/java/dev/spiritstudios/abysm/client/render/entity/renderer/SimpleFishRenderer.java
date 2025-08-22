package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class SimpleFishRenderer<R extends LivingEntityRenderState & GeoRenderState, T extends LivingEntity & GeoAnimatable> extends GeoEntityRenderer<T, R> {
	public static <T extends LivingEntity & GeoAnimatable> EntityRendererFactory<T> factory(String id) {
		return factory(Abysm.id(id));
	}

	public static <T extends LivingEntity & GeoAnimatable> EntityRendererFactory<T> factory(Identifier id) {
		return ctx -> new SimpleFishRenderer<>(ctx, id);
	}

	public SimpleFishRenderer(EntityRendererFactory.Context context, Identifier modelId) {
		super(context, new SimpleFishEntityModel<>(modelId));
	}

	public static class SimpleFishEntityModel<T extends LivingEntity & GeoAnimatable> extends DefaultedEntityGeoModel<T> {
		public SimpleFishEntityModel(Identifier id) {
			super(id, false);
		}
	}
}
