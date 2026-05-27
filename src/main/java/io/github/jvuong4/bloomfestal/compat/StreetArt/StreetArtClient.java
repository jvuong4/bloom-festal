package io.github.jvuong4.bloomfestal.compat.StreetArt;

import io.github.jvuong4.bloomfestal.client.renderer.HealOrbRenderer;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class StreetArtClient {
	public static void init() {
		EntityRenderers.register(StreetArtEntities.DYEBLAST_ORB, DyeblastOrbRenderer::new);
	}
}
