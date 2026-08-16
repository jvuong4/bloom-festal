package io.github.jvuong4.bloomfestal.item.GreatStaffs;

import io.github.jvuong4.bloomfestal.item.GreatStaff;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import net.minecraft.core.particles.SimpleParticleType;

public class GreatHarmingFestal extends GreatStaff {
	//harming festal is weaker overall but deals more damage
	public GreatHarmingFestal(final Properties properties) {
		super(properties);
		isHealing = false;
		damageMultiplier = 1.0F;
	}

	protected SimpleParticleType getChargedParticle() {return BFParticles.HARM_PETALS_PARTICLE;}

	@Override
	public float getHealingPotency(int power) {
		switch (power) {
			case 0:
				return 1.5f;
			case 1:
				return 4.5F;
			case 2:
				return 6.75F;
			case 3:
				return 9F;
			case 4:
				return 11.25F;
			case 5:
				return 13.5F;
			default:
				return 1.5F;
		}
	}
}
