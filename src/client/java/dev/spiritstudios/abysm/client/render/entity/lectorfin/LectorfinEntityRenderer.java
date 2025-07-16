package dev.spiritstudios.abysm.client.render.entity.lectorfin;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.AbstractFishEntityRenderer;
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
import software.bernie.geckolib.constant.DataTickets;
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
		map -> map.put(AbysmFishEnchantments.SHELL_ID, new ShellRenderer())
	);

	public LectorfinEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new LectorfinEntityModel());
	}

	@Override
	public void defaultRender(R state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, @Nullable RenderLayer renderLayer, @Nullable VertexConsumer vertexConsumer) {
		super.defaultRender(state, matrices, vertexConsumers, renderLayer, vertexConsumer);

		FishEnchantment fishEnchantment = state.getGeckolibData(FISH_ENCHANTMENT);
		if (fishEnchantment == null) {
			return;
		}
		FishEnchantmentRenderer renderer = ENCHANTMENT_RENDERERS.get(fishEnchantment.rendererId());
		if (renderer != null) {
			//noinspection DataFlowIssue
			renderer.render(state, matrices, vertexConsumers,
				state.getGeckolibData(DataTickets.PACKED_OVERLAY),
				state.getGeckolibData(DataTickets.PACKED_LIGHT),
				state.getGeckolibData(DataTickets.RENDER_COLOR),
				this::reRender);
		}
	}

	public static class LectorfinEntityModel extends AbstractFishEntityModel<LectorfinEntity> {
		public LectorfinEntityModel() {
			this(Abysm.id("lectorfin"));
		}

		public LectorfinEntityModel(Identifier id) {
			super(id, false);
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
