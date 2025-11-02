package dev.spiritstudios.abysm.client.render.entity.renderer.lectorfin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.renderer.RecursiveRenderer;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import java.util.Map;

public class LectorfinEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<LectorfinEntity, R> {

	public static final DataTicket<FishEnchantment> FISH_ENCHANTMENT = DataTicket.create("fish_enchantment", FishEnchantment.class);
	public static final Map<ResourceLocation, FishEnchantmentRenderer> ENCHANTMENT_RENDERERS = Util.make(
		new Object2ObjectOpenHashMap<>(),
		map -> {
			map.put(AbysmFishEnchantments.JAW.location(), JawRenderer.INSTANCE);
			map.put(AbysmFishEnchantments.SHELL.location(), ShellRenderer.INSTANCE);
			map.put(AbysmFishEnchantments.JET.location(), JetRenderer.INSTANCE);
		}
	);

	public LectorfinEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new LectorfinEntityModel());
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
			@Override
			protected boolean shouldRespectWorldLighting(R renderState) {
				return true;
			}
		});
	}

	@Override
	public void actuallyRender(R state, PoseStack matrices, BakedGeoModel model, @Nullable RenderType renderLayer, MultiBufferSource vertexConsumers, @Nullable VertexConsumer vertexConsumer, boolean isReRender, int light, int overlay, int color) {
		super.actuallyRender(state, matrices, model, renderLayer, vertexConsumers, vertexConsumer, isReRender, light, overlay, color);

		FishEnchantment fishEnchantment = state.getGeckolibData(FISH_ENCHANTMENT);
		if (fishEnchantment == null) {
			return;
		}
		FishEnchantmentRenderer renderer = ENCHANTMENT_RENDERERS.get(fishEnchantment.rendererId());
		if (renderer != null) {
			renderer.render(state, matrices, vertexConsumers, light, overlay, color, RecursiveRenderer.create(this));
		}
	}

	public static class LectorfinEntityModel extends DefaultedEntityGeoModel<LectorfinEntity> {
		public LectorfinEntityModel() {
			this(Abysm.id("lectorfin"));
		}

		protected LectorfinEntityModel(ResourceLocation id) {
			this(id, false);
		}

		protected LectorfinEntityModel(ResourceLocation id, boolean animateBodyAndTail) {
			super(id, animateBodyAndTail);
		}

		@Override
		public void addAdditionalStateData(LectorfinEntity lectorfin, GeoRenderState renderState) {
			super.addAdditionalStateData(lectorfin, renderState);
			Holder<FishEnchantment> fishEnchantment = lectorfin.getEnchantment();
			if (fishEnchantment != null) {
				renderState.addGeckolibData(FISH_ENCHANTMENT, fishEnchantment.value());
			}
		}
	}
}
