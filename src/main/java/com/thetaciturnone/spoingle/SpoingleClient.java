package com.thetaciturnone.spoingle;


import com.thetaciturnone.spoingle.entity.SpoingleEntities;
import com.thetaciturnone.spoingle.entity.SpoingleModel;
import com.thetaciturnone.spoingle.entity.SpoingleModelLayers;
import com.thetaciturnone.spoingle.entity.SpoingleRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;


public class SpoingleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(SpoingleEntities.SPOINGLE, SpoingleRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(SpoingleModelLayers.SPOINGLE, SpoingleModel::getTexturedModelData);
	}

}
