package dev.spiritstudios.abysm.client.render.entity.model;

import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

// inside joke
public class GarbageBagModel extends EntityModel<ManOWarRenderState> {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private final ModelPart body;

	public GarbageBagModel(ModelPart root) {
		super(root, RenderType::entityTranslucent);
		this.body = root.getChild("body");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(0.0F, -10.0F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}


}
