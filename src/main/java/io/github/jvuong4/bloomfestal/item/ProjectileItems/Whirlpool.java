package io.github.jvuong4.bloomfestal.item.ProjectileItems;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrbs.WhirlpoolOrb;
import io.github.jvuong4.bloomfestal.item.ProjectileMagicItem;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class Whirlpool extends ProjectileMagicItem {
	public Whirlpool(final Properties properties) {
		super(properties);
	}

	@Override
	protected SimpleParticleType getChargedParticle() {
		return BFParticles.BUBBLE_PARTICLE;
	}

	@Override
	protected MagicalOrb getOrb(ServerLevel level, LivingEntity player, Vec3 direction, double d) {
		return new WhirlpoolOrb(level, player, (direction.normalize()).scale(d));
	}
}
