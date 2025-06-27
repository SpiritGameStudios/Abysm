package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.model.FlippersModel;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public final class AbysmEntityLayers {
	public static final EntityModelLayer FLIPPERS = register("player", "flippers", FlippersModel::getTexturedModelData);

	public static final EntityModelLayer MAN_O_WAR = register("man_o_war", "main", GarbageBagModel::getTexturedModelData);


	private static EntityModelLayer register(String id, String layer, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
		EntityModelLayer modelLayer = new EntityModelLayer(Abysm.id(id), layer);
		EntityModelLayerRegistry.registerModelLayer(modelLayer, provider);
		return modelLayer;
	}

	public static void init() {
		// NO-OP
	}
}
