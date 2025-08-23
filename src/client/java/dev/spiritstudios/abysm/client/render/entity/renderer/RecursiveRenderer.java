package dev.spiritstudios.abysm.client.render.entity.renderer;


import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@FunctionalInterface
public interface RecursiveRenderer<R extends LivingEntityRenderState & GeoRenderState> {
	void render(R state, MatrixStack matrices, GeoBone bone, @Nullable RenderLayer renderLayer, VertexConsumerProvider vertexConsumers, @Nullable VertexConsumer vertexConsumer, boolean isReRender, int light, int overlay, int color);

	static <R extends LivingEntityRenderState & GeoRenderState> RecursiveRenderer<R> create(GeoEntityRenderer<?, R> renderer) {
		return renderer::renderRecursively;
	}
}
