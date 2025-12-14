package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

public class FishEntityRenderer<T extends Mob> extends MobRenderer<T, LivingEntityRenderState, FishEntityRenderer.Model> {
	public static <T extends Mob> EntityRendererProvider<T> factory(String name) {
		return context -> new FishEntityRenderer<>(context, name);
	}

	private final Identifier textureLocation;

	public FishEntityRenderer(EntityRendererProvider.Context context, String name) {
		super(context, new Model(context, name), 1F);
		this.textureLocation = Abysm.id("textures/entity/" + name + ".png");
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState livingEntityRenderState) {
		return textureLocation;
	}

	public static class Model extends EntityModel<LivingEntityRenderState> {
		protected Model(EntityRendererProvider.Context context, String name) {
			super(
				context.bakeLayer(new ModelLayerLocation(Abysm.id(name), "main"))
			);
		}
	}
}
