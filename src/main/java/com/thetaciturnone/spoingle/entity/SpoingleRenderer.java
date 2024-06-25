package com.thetaciturnone.spoingle.entity;

import com.thetaciturnone.spoingle.Spoingle;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SpoingleRenderer extends MobEntityRenderer<SpoingleEntity, SpoingleModel<SpoingleEntity>> {

    protected final Identifier TEXTURE = new Identifier(Spoingle.MOD_ID, "textures/entity/spoingle.png");

    public SpoingleRenderer(EntityRendererFactory.Context context) {
        super(context, new SpoingleModel<>(context.getPart(SpoingleModelLayers.SPOINGLE)), 0.3f);
    }


    @Override
    public Identifier getTexture(SpoingleEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(SpoingleEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if(mobEntity.isBaby()){
			matrixStack.scale(0.8f, 0.8f, 0.8f);
		} else {
			matrixStack.scale(1.2f, 1.2f, 1.2f);
		}
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
