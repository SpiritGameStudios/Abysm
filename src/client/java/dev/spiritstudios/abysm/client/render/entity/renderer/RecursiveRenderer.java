package dev.spiritstudios.abysm.client.render.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@FunctionalInterface
public interface RecursiveRenderer<R extends LivingEntityRenderState & GeoRenderState> {
	void render(R state, PoseStack matrices, GeoBone bone, @Nullable RenderType renderLayer, MultiBufferSource vertexConsumers, @Nullable VertexConsumer vertexConsumer, boolean isReRender, int light, int overlay, int color);

	static <R extends LivingEntityRenderState & GeoRenderState> RecursiveRenderer<R> create(GeoEntityRenderer<?, R> renderer) {
		return renderer::renderRecursively;
	}
}
