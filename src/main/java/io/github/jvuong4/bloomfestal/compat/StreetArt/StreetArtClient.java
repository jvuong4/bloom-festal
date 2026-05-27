package io.github.jvuong4.bloomfestal.compat.StreetArt;

import io.github.jvuong4.bloomfestal.client.renderer.HealOrbRenderer;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class StreetArtClient {
	public static void init() {
		EntityRenderers.register(StreetArtEntities.RED_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.YELLOW_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.ORANGE_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.LIME_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.GREEN_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.BLUE_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.LIGHT_BLUE_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.CYAN_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.PINK_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.MAGENTA_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.PURPLE_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.LIGHT_GRAY_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.GRAY_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.WHITE_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.BLACK_DYEBLAST_ORB, DyeblastOrbRenderer::new);
		EntityRenderers.register(StreetArtEntities.BROWN_DYEBLAST_ORB, DyeblastOrbRenderer::new);
	}
}
