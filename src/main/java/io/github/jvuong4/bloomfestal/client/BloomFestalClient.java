package io.github.jvuong4.bloomfestal.client;

import io.github.jvuong4.bloomfestal.compat.StreetArt.DyeblastItem;
import io.github.jvuong4.bloomfestal.compat.StreetArt.StreetArtClient;
import io.github.jvuong4.bloomfestal.compat.StreetArt.StreetArtItems;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;

public class BloomFestalClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		//EntityRendererRegistryImpl
		BFModelLayers.init();
		BFParticlesClient.init();
		if(FabricLoader.getInstance().isModLoaded("street_art"))
		{
			StreetArtClient.init();
		}
	}
}
