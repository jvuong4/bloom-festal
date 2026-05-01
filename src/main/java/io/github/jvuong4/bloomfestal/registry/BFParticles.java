package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class BFParticles {
	public static final SimpleParticleType HARM_PETALS_PARTICLE = FabricParticleTypes.simple();

	public static void init() {

		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(BloomFestal.ID, "harm_petals"), HARM_PETALS_PARTICLE);
	}
}
