package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.effects.BloomEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class BFEffects {
	public static final Holder<MobEffect> CHERRY_BLOOM =
		Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
			Identifier.fromNamespaceAndPath(BloomFestal.ID, "cherry_bloom"),
			new BloomEffect(ParticleTypes.CHERRY_LEAVES, 0.5));

	public static final Holder<MobEffect> HEART_BLOOM =
		Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
			Identifier.fromNamespaceAndPath(BloomFestal.ID, "heart_bloom"),
			new BloomEffect(ParticleTypes.HEART, 0.0));

	public static final Holder<MobEffect> SOUL_BLOOM =
		Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
			Identifier.fromNamespaceAndPath(BloomFestal.ID, "soul_bloom"),
			new BloomEffect(ParticleTypes.SOUL_FIRE_FLAME, 0.0));

	public static void init() {}
}
