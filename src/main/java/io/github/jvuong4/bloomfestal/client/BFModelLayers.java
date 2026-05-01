package io.github.jvuong4.bloomfestal.client;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.client.model.HealOrbModel;
import io.github.jvuong4.bloomfestal.client.renderer.*;
import io.github.jvuong4.bloomfestal.effects.StillnessEffect;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.resources.Identifier;

public class BFModelLayers {

	public static final ModelLayerLocation HEAL_ORB = createMain("heal_orb");
	public static final ModelLayerLocation ECLIPSE_ORB = createMain("eclipse_orb");
	public static final ModelLayerLocation THORON_ORB = createMain("thoron_orb");

	private static ModelLayerLocation createMain(String name) {
		return new ModelLayerLocation(Identifier.fromNamespaceAndPath(BloomFestal.ID, name), "main");
	}

	public static void init() {
		EntityRenderers.register(BFEntities.HEAL_ORB, HealOrbRenderer::new);
		EntityRenderers.register(BFEntities.HARM_ORB, HarmOrbRenderer::new);
		EntityRenderers.register(BFEntities.REWARP_ORB, RewarpOrbRenderer::new);
		EntityRenderers.register(BFEntities.REVELATION_ORB, RevelationOrbRenderer::new);
		EntityRenderers.register(BFEntities.SILENCE_ORB, SilenceOrbRenderer::new);
		//EntityRenderers.register(BFEntities.STILLNESS_ORB, StillnessOrbRenderer::new);
		EntityRenderers.register(BFEntities.HEXING_ORB, HexingOrbRenderer::new);
		EntityRenderers.register(BFEntities.ECLIPSE_ORB, EclipseOrbRenderer::new);
		EntityRenderers.register(BFEntities.NOSFERATU_ORB, NosferatuOrbRenderer::new);
		EntityRenderers.register(BFEntities.THORON_ORB, ThoronOrbRenderer::new);
		EntityRenderers.register(BFEntities.REXCALIBUR_ORB, RexcaliburOrbRenderer::new);
		EntityRenderers.register(BFEntities.BOLGANONE_ORB, BolganoneOrbRenderer::new);

		//EntityRenderers.register(BFEntities.THROWN_KODACHI, ThrownItemRenderer::new);

		EntityRenderers.register(BFEntities.VISUAL_LIGHTNING, LightningBoltRenderer::new);
	}
}
