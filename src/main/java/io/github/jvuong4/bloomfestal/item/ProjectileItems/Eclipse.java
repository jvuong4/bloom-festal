package io.github.jvuong4.bloomfestal.item.ProjectileItems;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.EclipseOrb;
import io.github.jvuong4.bloomfestal.item.ProjectileMagicItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class Eclipse extends ProjectileMagicItem {
	public Eclipse(final Properties properties) {
		super(properties);
	}

	@Override
	public int getCharge1time() {
		return 40;
	}
	@Override
	public int getCharge2time() {
		return 120;
	}

	@Override
	protected SimpleParticleType getChargedParticle() {
		return ParticleTypes.SOUL;
	}

	@Override
	protected MagicalOrb getOrb(ServerLevel level, LivingEntity player, Vec3 direction, double d) {
		return new EclipseOrb(level, player, (direction.normalize()).scale(d));
	}
}
