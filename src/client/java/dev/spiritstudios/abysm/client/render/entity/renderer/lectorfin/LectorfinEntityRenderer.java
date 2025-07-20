package dev.spiritstudios.abysm.client.render.entity.renderer.lectorfin;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.renderer.AbstractFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.model.AbstractFishEntityModel;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.Map;

/*
 * Note to my fellow developers: if you would like to fix this abomination, go ahead
 */
public class LectorfinEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends AbstractFishEntityRenderer<LectorfinEntity, R> {

	public static final DataTicket<FishEnchantment> FISH_ENCHANTMENT = DataTicket.create("fish_enchantment", FishEnchantment.class);
	public static final Map<Identifier, FishEnchantmentRenderer> ENCHANTMENT_RENDERERS = Util.make(
		new Object2ObjectOpenHashMap<>(),
		map -> {
			//map.put(AbysmFishEnchantments.JAW.getValue(), JawRenderer.INSTANCE);
			map.put(AbysmFishEnchantments.SHELL.getValue(), ShellRenderer.INSTANCE);
			map.put(AbysmFishEnchantments.JET.getValue(), JetRenderer.INSTANCE);
		}
	);

	public LectorfinEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new LectorfinEntityModel());
	}

	@Override
	public void actuallyRender(R state, MatrixStack matrices, BakedGeoModel model, @Nullable RenderLayer renderLayer, VertexConsumerProvider vertexConsumers, @Nullable VertexConsumer vertexConsumer, boolean isReRender, int light, int overlay, int color) {
		super.actuallyRender(state, matrices, model, renderLayer, vertexConsumers, vertexConsumer, isReRender, light, overlay, color);

		FishEnchantment fishEnchantment = state.getGeckolibData(FISH_ENCHANTMENT);
		if (fishEnchantment == null) {
			return;
		}
		FishEnchantmentRenderer renderer = ENCHANTMENT_RENDERERS.get(fishEnchantment.rendererId());
		if (renderer != null) {
			renderer.render(state, matrices, vertexConsumers, light, overlay, color, this::renderRecursively);
		}
	}

	public static class LectorfinEntityModel extends AbstractFishEntityModel<LectorfinEntity> {
		public LectorfinEntityModel() {
			this(Abysm.id("lectorfin"));
		}

		protected LectorfinEntityModel(Identifier id) {
			this(id, false);
		}

		protected LectorfinEntityModel(Identifier id, boolean animateBodyAndTail) {
			super(id, animateBodyAndTail);
		}

		@Override
		public void addAdditionalStateData(LectorfinEntity lectorfin, GeoRenderState renderState) {
			super.addAdditionalStateData(lectorfin, renderState);
			RegistryEntry<FishEnchantment> fishEnchantment = lectorfin.getEnchantment();
			if (fishEnchantment != null) {
				renderState.addGeckolibData(FISH_ENCHANTMENT, fishEnchantment.value());
			}
		}
	}
}
