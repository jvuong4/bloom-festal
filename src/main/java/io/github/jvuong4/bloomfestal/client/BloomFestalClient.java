package io.github.jvuong4.bloomfestal.client;

import io.github.jvuong4.bloomfestal.registry.BFParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;

public class BloomFestalClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		//EntityRendererRegistryImpl
		BFModelLayers.init();
		BFParticlesClient.init();
	}
}
