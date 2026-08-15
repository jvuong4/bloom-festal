package io.github.jvuong4.bloomfestal.entity.MagicalOrbs.HealthOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.HealthOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HarmOrb extends HealthOrb {
	public HarmOrb(final EntityType<? extends HarmOrb> type, final Level level) {
		super(type, level);
		isHealing = false;
		initVals();
	}

	public HarmOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.HARM_ORB, level, mob, direction);
		isHealing = false;
		initVals();
	}

	public HarmOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.HARM_ORB, level, x, y, z, direction);
		isHealing = false;
		initVals();
	}

	@Override
	protected void initVals() {
		switch (charge) {
			case 0:
				accelerationPower = 0.8;
				range = 6;
				potency = 3F;
				explosionRadius = 0.0F;
				break;
			case 1:
				accelerationPower = 0.9;
				range = 6;
				potency = 5F;
				explosionRadius = 4.0F;
				break;
			case 2:
				accelerationPower = 1;
				range = 6;
				potency = 7F;
				explosionRadius = 6.0F;
				break;
			default:
				accelerationPower = 0.8;
				range = 6;
				potency = 3F;
				explosionRadius = 0.0F;
				break;
		}
		particleSpawnChance = 2.0F;
		explosionSound = SoundEvents.AMBIENT_UNDERWATER_ENTER;
	}
}
