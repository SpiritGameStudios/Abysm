package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.MysteriousBlobRenderState;
import dev.spiritstudios.abysm.client.render.entity.state.SkeletonSharkRenderState;
import dev.spiritstudios.abysm.world.entity.depths.MysteriousBlobEntity;
import dev.spiritstudios.abysm.world.entity.leviathan.pseudo.SkeletonSharkEntity;
import dev.spiritstudios.abysm.world.entity.leviathan.pseudo.SkeletonSharkPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import static dev.spiritstudios.abysm.world.entity.leviathan.pseudo.SkeletonSharkEntity.HUNTING;

public class SkeletonSharkEntityRenderer extends MobRenderer<SkeletonSharkEntity, SkeletonSharkRenderState, SkeletonSharkEntityRenderer.Model> {
	public static final Identifier TEXTURE = Abysm.id("textures/entity/skeleton_shark.png");

	public SkeletonSharkEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Model(context), 1F);
	}

	@Override
	public SkeletonSharkRenderState createRenderState() {
		return new SkeletonSharkRenderState();
	}

	@Override
	public void extractRenderState(SkeletonSharkEntity skeleshark, SkeletonSharkRenderState state, float partialTick) {
		super.extractRenderState(skeleshark, state, partialTick);
		//noinspection UnstableApiUsage
		state.sans = skeleshark.getAttachedOrElse(HUNTING, false);
		Entity previous = skeleshark;
		for (SkeletonSharkPart part : skeleshark.getSubEntities()) {
			String name = part.name;
			if (!state.parts.containsKey(name)) {
				continue;
			}
			state.parts.get(name).extract(part, name.contains("fin") ? null : previous, partialTick);
			previous = part;
		}
	}

	@Override
	public Identifier getTextureLocation(SkeletonSharkRenderState state) {
		return TEXTURE;
	}

	public static class Model extends EntityModel<SkeletonSharkRenderState> {
		protected Model(EntityRendererProvider.Context context) {
			super(
				context.bakeLayer(new ModelLayerLocation(Abysm.id("skeleton_shark"), "main"))
			);
		}
	}
}
