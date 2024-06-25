package com.thetaciturnone.spoingle.entity;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 4.10.3
 * Exported for Minecraft version 1.19 or later with Yarn mappings
 * @author Author
 */
public class SpoingleAnimations {
	public static final Animation WALK = Animation.Builder.create(2.7917F).looping()
		.addBoneAnimation("leg1", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.method_41822(17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(0.25F, AnimationHelper.method_41822(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(0.5F, AnimationHelper.method_41822(17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(0.75F, AnimationHelper.method_41822(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(1.0F, AnimationHelper.method_41822(17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884)))
		.addBoneAnimation("leg2", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.method_41822(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(0.25F, AnimationHelper.method_41822(17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(0.5F, AnimationHelper.method_41822(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(0.75F, AnimationHelper.method_41822(17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884),
			new Keyframe(1.0F, AnimationHelper.method_41822(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.field_37884)
		))
		.build();
}
