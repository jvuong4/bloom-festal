package io.github.jvuong4.bloomfestal.item.ProjectileItems;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.StatusEffectOrbs.RevelationOrb;
import io.github.jvuong4.bloomfestal.item.ProjectileMagicItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class RevelationFestal extends ProjectileMagicItem {

	public RevelationFestal(final net.minecraft.world.item.Item.Properties properties) {
		super(properties);
	}

	@Override
	protected SimpleParticleType getChargedParticle() {
		return ParticleTypes.SCULK_SOUL;
	}

	@Override
	protected MagicalOrb getOrb(ServerLevel level, LivingEntity player, Vec3 direction, double d) {
		return new RevelationOrb(level, player, (direction.normalize()).scale(d));
	}
}
