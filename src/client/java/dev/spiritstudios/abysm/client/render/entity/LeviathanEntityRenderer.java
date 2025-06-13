package dev.spiritstudios.abysm.client.render.entity;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

// feel free to change this to LivingEntityRenderer if necessary
@SuppressWarnings("unused")
public abstract class LeviathanEntityRenderer<T extends Leviathan, S extends EntityRenderState> extends EntityRenderer<T, S> {

	protected LeviathanEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	protected void appendHitboxes(T leviathan, ImmutableList.Builder<EntityHitbox> builder, float f) {
		super.appendHitboxes(leviathan, builder, f);
		double d = -MathHelper.lerp(f, leviathan.lastRenderX, leviathan.getX());
		double e = -MathHelper.lerp(f, leviathan.lastRenderY, leviathan.getY());
		double g = -MathHelper.lerp(f, leviathan.lastRenderZ, leviathan.getZ());

		for (LeviathanPart part : leviathan.getParts()) {
			Box box = part.getBoundingBox();
			EntityHitbox entityHitbox = new EntityHitbox(
				box.minX - part.getX(),
				box.minY - part.getY(),
				box.minZ - part.getZ(),
				box.maxX - part.getX(),
				box.maxY - part.getY(),
				box.maxZ - part.getZ(),
				(float)(d + MathHelper.lerp(f, part.lastRenderX, part.getX())),
				(float)(e + MathHelper.lerp(f, part.lastRenderY, part.getY())),
				(float)(g + MathHelper.lerp(f, part.lastRenderZ, part.getZ())),
				0.25F,
				1.0F,
				0.0F
			);
			builder.add(entityHitbox);
		}
	}
}
