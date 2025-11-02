package dev.spiritstudios.abysm.client.render.entity.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class FlippersModel extends HumanoidModel<HumanoidRenderState> {
	public final ModelPart rightFlipper;
	public final ModelPart leftFlipper;
	public final ModelPart rightFlipperSwimming;
	public final ModelPart leftFlipperSwimming;

	public FlippersModel(ModelPart root) {
		super(root);

		this.rightFlipper = this.rightLeg.getChild("right_flipper");
		this.leftFlipper = this.leftLeg.getChild("left_flipper");

		this.rightFlipperSwimming = this.rightLeg.getChild("right_flipper_swimming");
		this.leftFlipperSwimming = this.leftLeg.getChild("left_flipper_swimming");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = modelData.getRoot();

		PartDefinition rightLeg = root.addOrReplaceChild(
			"right_leg",
			CubeListBuilder.create()
				.texOffs(0, 12)
				.addBox(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)),
			PartPose.offset(-1.9F, 12.0F, 0.0F)
		);

		rightLeg.addOrReplaceChild(
			"right_flipper",
			CubeListBuilder.create()
				.texOffs(-12, 0)
				.addBox(-7.0F, 12.0F, -14.0F, 9.0F, 0.0F, 12.0F, CubeDeformation.NONE),
			PartPose.ZERO
		);

		rightLeg.addOrReplaceChild(
			"right_flipper_swimming",
			CubeListBuilder.create()
				.texOffs(-12, 0)
				.addBox(-6.0F, 0.0F, -11.0F, 9.0F, 0.0F, 12.0F, CubeDeformation.NONE),
			PartPose.offsetAndRotation(-1.0F, 13.0F, 0.0F, 1.5708F, 0.0F, 0.0F)
		);

		PartDefinition leftLeg = root.addOrReplaceChild(
			"left_leg",
			CubeListBuilder.create()
				.texOffs(0, 12)
				.addBox(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)),
			PartPose.offset(1.9F, 12.0F, 0.0F)
		);

		leftLeg.addOrReplaceChild(
			"left_flipper",
			CubeListBuilder.create()
				.texOffs(-12, 0)
				.mirror()
				.addBox(-1.8F, 12.0F, -14.0F, 9.0F, 0.0F, 12.0F, CubeDeformation.NONE),
			PartPose.ZERO
		);

		leftLeg.addOrReplaceChild(
			"left_flipper_swimming",
			CubeListBuilder.create()
				.mirror()
				.texOffs(-12, 0)
				.addBox(-6.0F, 0.0F, -11.0F, 9.0F, 0.0F, 12.0F, CubeDeformation.NONE),
			PartPose.offsetAndRotation(4.2F, 13.0F, 0.0F, 1.5708F, 0.0F, 0.0F)
		);

		return LayerDefinition.create(modelData, 32, 32);
	}
}
