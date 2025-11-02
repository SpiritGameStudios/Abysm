package dev.spiritstudios.abysm.client.render.entity.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.TextureLayerGeoLayer;

public class EntityPatternFeatureRenderer<T extends GeoAnimatable, O, R extends GeoRenderState> extends TextureLayerGeoLayer<T, O, R> {
	private static final ResourceLocation MISSING_TEXTURE = MissingTextureAtlasSprite.getLocation();
	public static final DataTicket<EntityPattern> DATA_TICKET = DataTicket.create("entity_pattern_ticket", EntityPattern.class);

	public EntityPatternFeatureRenderer(GeoRenderer<T, O, R> renderer) {
		super(renderer, MISSING_TEXTURE);
	}

	@Override
	protected ResourceLocation getTextureResource(R state) {
		EntityPattern pattern = state.getGeckolibData(DATA_TICKET);
		if (pattern != null && pattern.variant() != null) return pattern.variant().value().patternPath();
		return MISSING_TEXTURE;
	}

	@Override
	public void render(R renderState, PoseStack poseStack, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
		EntityPattern pattern = renderState.getGeckolibData(DATA_TICKET);
		int patternColor = renderColor;
		if (pattern != null) patternColor = pattern.patternColor();
		super.render(renderState, poseStack, bakedModel, renderType, bufferSource, buffer, packedLight, packedOverlay, patternColor);
	}
}
