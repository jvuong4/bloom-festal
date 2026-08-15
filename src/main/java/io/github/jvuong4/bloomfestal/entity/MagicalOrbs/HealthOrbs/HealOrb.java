package io.github.jvuong4.bloomfestal.entity.MagicalOrbs.HealthOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.HealthOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HealOrb extends HealthOrb {
	public HealOrb(final EntityType<? extends HealOrb> type, final Level level) {
		super(type, level);
		isHealing = true;
		initVals();
	}

	public HealOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, level, mob, direction);
		isHealing = true;
		initVals();
	}

	public HealOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, level, x, y, z, direction);
		isHealing = true;
		initVals();
	}

	@Override
	protected void initVals()
	{
		switch(charge)
		{
			//bloom
			case 0:
				accelerationPower = 0.8;
				range = 6;
				potency = 1F;
				explosionRadius = 0.0F;
				break;
			case 1:
				accelerationPower = 0.9;
				range = 6;
				potency = 3F;
				explosionRadius = 4.0F;
				break;
			case 2:
				accelerationPower = 1;
				range = 6;
				potency = 5F;
				explosionRadius = 6.0F;
				break;
			//sun/dawn
			case 3:
				accelerationPower = 0.8;
				range = 6;
				potency = 4F;
				explosionRadius = 0.0F;
				break;
			case 4:
				accelerationPower = 0.9;
				range = 6;
				potency = 7F;
				explosionRadius = 4.0F;
				break;
			case 5:
				accelerationPower = 1;
				range = 15;
				potency = 10F;
				explosionRadius = 6.0F;
				break;
			//wane/horizon
			case 6:
				accelerationPower = 0.8;
				range = 15;
				potency = 1F;
				explosionRadius = 6.0F;
				break;
			case 7:
				accelerationPower = 0.9;
				range = 18;
				potency = 3F;
				explosionRadius = 8.0F;
				break;
			case 8:
				accelerationPower = 1;
				range = 20;
				potency = 5F;
				explosionRadius = 10.0F;
				break;
			//moon/twilight
			case 9:
				accelerationPower = 0.8;
				range = 6;
				potency = 10F;
				explosionRadius = 0.0F;
				break;
			case 10:
				accelerationPower = 0.9;
				range = 6;
				potency = 14F;
				explosionRadius = 4.0F;
				break;
			case 11:
				accelerationPower = 1;
				range = 6;
				potency = 18F;
				explosionRadius = 6.0F;
				break;
			default:
				accelerationPower = 0.8;
				range = 6;
				potency = 1F;
				explosionRadius = 0.0F;
				break;
		}
		particleSpawnChance = 2.0F;
		explosionSound = BFSounds.HEAL;
	}

}
