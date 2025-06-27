package dev.spiritstudios.abysm.client.render.entity.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

public class FlippersModel extends BipedEntityModel<BipedEntityRenderState> {
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

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
		ModelPartData root = modelData.getRoot();

		ModelPartData rightLeg = root.addChild(
			"right_leg",
			ModelPartBuilder.create()
				.uv(0, 12)
				.cuboid(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.25F)),
			ModelTransform.origin(-1.9F, 12.0F, 0.0F)
		);

		rightLeg.addChild(
			"right_flipper",
			ModelPartBuilder.create()
				.uv(-12, 0)
				.cuboid(-7.0F, 12.0F, -14.0F, 9.0F, 0.0F, 12.0F, Dilation.NONE),
			ModelTransform.NONE
		);

		rightLeg.addChild(
			"right_flipper_swimming",
			ModelPartBuilder.create()
				.uv(-12, 0)
				.cuboid(-6.0F, 0.0F, -11.0F, 9.0F, 0.0F, 12.0F, Dilation.NONE),
			ModelTransform.of(-1.0F, 13.0F, 0.0F, 1.5708F, 0.0F, 0.0F)
		);

		ModelPartData leftLeg = root.addChild(
			"left_leg",
			ModelPartBuilder.create()
				.uv(0, 12)
				.cuboid(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.25F)),
			ModelTransform.origin(1.9F, 12.0F, 0.0F)
		);

		leftLeg.addChild(
			"left_flipper",
			ModelPartBuilder.create()
				.uv(-12, 0)
				.mirrored()
				.cuboid(-1.8F, 12.0F, -14.0F, 9.0F, 0.0F, 12.0F, Dilation.NONE),
			ModelTransform.NONE
		);

		leftLeg.addChild(
			"left_flipper_swimming",
			ModelPartBuilder.create()
				.mirrored()
				.uv(-12, 0)
				.cuboid(-6.0F, 0.0F, -11.0F, 9.0F, 0.0F, 12.0F, Dilation.NONE),
			ModelTransform.of(4.2F, 13.0F, 0.0F, 1.5708F, 0.0F, 0.0F)
		);

		return TexturedModelData.of(modelData, 32, 32);
	}
}
