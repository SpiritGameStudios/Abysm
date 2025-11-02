package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.model.FlippersModel;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class AbysmEntityLayers {
	public static final ModelLayerLocation FLIPPERS = register("player", "flippers", FlippersModel::getTexturedModelData);

	public static final ModelLayerLocation MAN_O_WAR = register("man_o_war", "main", GarbageBagModel::getTexturedModelData);


	private static ModelLayerLocation register(String id, String layer, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
		ModelLayerLocation modelLayer = new ModelLayerLocation(Abysm.id(id), layer);
		EntityModelLayerRegistry.registerModelLayer(modelLayer, provider);
		return modelLayer;
	}

	public static void init() {
		// NO-OP
	}
}
