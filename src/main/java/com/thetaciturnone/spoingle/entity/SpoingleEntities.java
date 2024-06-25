package com.thetaciturnone.spoingle.entity;

import com.thetaciturnone.spoingle.Spoingle;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpoingleEntities {
	public static final EntityType<SpoingleEntity> SPOINGLE = Registry.register(Registry.ENTITY_TYPE, new Identifier(Spoingle.MOD_ID, "spoingle"),
		FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SpoingleEntity::new).dimensions(EntityDimensions.fixed(0.6f, 0.6f)).build());
}
