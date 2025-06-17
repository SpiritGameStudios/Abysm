package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;

// inside joke
public class GarbageBagModel extends EntityModel<ManOWarRenderState> {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private final ModelPart body;

	public GarbageBagModel(ModelPart root) {
		super(root, RenderLayer::getEntityTranslucent);
		this.body = root.getChild("body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -4.0F, -6.0F, 6.0F, 4.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 16).cuboid(0.0F, -10.0F, -6.0F, 0.0F, 6.0F, 12.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}


}
