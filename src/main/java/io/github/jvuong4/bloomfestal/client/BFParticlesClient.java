package io.github.jvuong4.bloomfestal.client;

import io.github.jvuong4.bloomfestal.registry.BFParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.FallingLeavesParticle;

public class BFParticlesClient {
	public static void init()
	{
		ParticleProviderRegistry.getInstance().register(BFParticles.HARM_PETALS_PARTICLE, FallingLeavesParticle.CherryProvider::new);
	}
}
