package com.thetaciturnone.spoingle.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class SpoingleModel<T extends SpoingleEntity> extends SinglePartEntityModel<T> {
	private final ModelPart spoingle;

	public SpoingleModel(ModelPart root) {
		this.spoingle = root.getChild("spoingle");
		ModelPart body = spoingle.getChild("body");
		ModelPart leg1 = spoingle.getChild("leg1");
		ModelPart leg2 = spoingle.getChild("leg2");
		ModelPart arm1 = spoingle.getChild("arm1");
		ModelPart arm2 = spoingle.getChild("arm2");
		ModelPart eye1 = spoingle.getChild("eye1");
		ModelPart eye2 = spoingle.getChild("eye2");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spoingle = modelPartData.addChild("spoingle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body = spoingle.addChild("body", ModelPartBuilder.create().uv(14, 6).cuboid(-2.0F, -5.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData leg1 = spoingle.addChild("leg1", ModelPartBuilder.create().uv(15, 15).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.499F, -1.0F, 0.0F));

		ModelPartData leg2 = spoingle.addChild("leg2", ModelPartBuilder.create().uv(12, 0).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.499F, -1.0F, 0.0F));

		ModelPartData arm1 = spoingle.addChild("arm1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = arm1.addChild("cube_r1", ModelPartBuilder.create().uv(0, 8).cuboid(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		ModelPartData arm2 = spoingle.addChild("arm2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r2 = arm2.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.48F));

		ModelPartData eye1 = spoingle.addChild("eye1", ModelPartBuilder.create().uv(0, 8).cuboid(-5.0F, -7.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData eye2 = spoingle.addChild("eye2", ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, -7.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	@Override
	public void setAngles(SpoingleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		spoingle.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return spoingle;
	}
}
