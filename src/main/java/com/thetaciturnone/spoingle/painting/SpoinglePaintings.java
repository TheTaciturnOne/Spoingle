package com.thetaciturnone.spoingle.painting;

import com.thetaciturnone.spoingle.Spoingle;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpoinglePaintings {

	public static final PaintingVariant SMALL_SPOINGLE = registerPainting("spoingle_painting_small", new PaintingVariant(16, 16));
	public static final PaintingVariant CHADOINGLE = registerPainting("chadoingle", new PaintingVariant(32, 48));
	public static final PaintingVariant WIDE_SPOINGLE = registerPainting("long_spoingle_painting", new PaintingVariant(32, 16));
	public static final PaintingVariant GIGASPOINGLE = registerPainting("gigaoingle", new PaintingVariant(114, 126));

	public static PaintingVariant registerPainting(String name, PaintingVariant paintingVariant){
		return Registry.register(Registry.PAINTING_VARIANT, new Identifier(Spoingle.MOD_ID , name), paintingVariant);
	}

	public static void registerSpoinglePaintings() {
		Spoingle.LOGGER.info("Registering vicotrian art of" + Spoingle.MOD_ID);
	}
}
