package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.layers.LivingEntityTintedLayer;
import dev.spiritstudios.abysm.client.render.entity.state.FloralFishRenderState;
import dev.spiritstudios.abysm.world.entity.floralreef.AbstractFloralFish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class FloralFishRenderer<T extends AbstractFloralFish> extends MobRenderer<T, FloralFishRenderState, FloralFishRenderer.Model> {
	private final Identifier defaultTexture;

	public static <T extends AbstractFloralFish> EntityRendererProvider<T> factory(String name) {
		return context -> new FloralFishRenderer<>(context, name);
	}

	public FloralFishRenderer(EntityRendererProvider.Context context, String name) {
		super(
			context,
			new Model(context.bakeLayer(
				new ModelLayerLocation(Abysm.id(name), "main")
			)),
			1
		);

		this.layers.add(new LivingEntityTintedLayer<>(
			this,
			state -> state.pattern.variant().value().patternPath(),
			(state, partialTick) -> state.pattern.patternColor(),
			new Model(context.bakeLayer(
				new ModelLayerLocation(Abysm.id(name), "main")
			)),
			RenderTypes::entityCutout,
			false
		));

		this.defaultTexture = Abysm.id("textures/entity/" + name + ".png");
	}

	@Override
	public Identifier getTextureLocation(FloralFishRenderState renderState) {
		return renderState.pattern.variant().value().baseTexture().orElse(defaultTexture);
	}

	@Override
	protected int getModelTint(FloralFishRenderState renderState) {
		return renderState.pattern.baseColor();
	}

	@Override
	public FloralFishRenderState createRenderState() {
		return new FloralFishRenderState();
	}

	@Override
	public void extractRenderState(T entity, FloralFishRenderState renderState, float partialTick) {
		super.extractRenderState(entity, renderState, partialTick);

		renderState.query.set(entity, partialTick);
		renderState.pattern = entity.getEntityPattern();
	}

	public static class Model extends EntityModel<FloralFishRenderState> {
		public Model(ModelPart root) {
			super(root);
		}
	}

}
