package io.github.jvuong4.bloomfestal.entity.MagicalOrbs.StatusEffectOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.StatusEffectOrb;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RevelationOrb extends StatusEffectOrb {
	public RevelationOrb(final EntityType<? extends RevelationOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public RevelationOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.REVELATION_ORB, level, mob, direction);
		initVals();
	}

	public RevelationOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.REVELATION_ORB, level, x, y, z, direction);
		initVals();
	}
	@Override
	protected void initVals()
	{
		switch(charge)
		{
			case 1:
				accelerationPower = 1;
				range = 6;
				potency = 45F;
				explosionRadius = 3F;
				break;
			case 2:
				accelerationPower = 1;
				range = 6;
				potency = 60F;
				explosionRadius = 4F;
				break;
			case 0:
			default:
				accelerationPower = 1;
				range = 6;
				potency = 30F;
				explosionRadius = 0.0f;
				break;
		}
		particleSpawnChance = 2F;
		explosionSound = SoundEvents.TRIDENT_THUNDER.value();
		damageParticle = ParticleTypes.SCULK_SOUL;
	}

	@Override
	public Holder<MobEffect> getStatusEffect()
	{return BFEffects.REVELATION;}

	@Override
	public boolean canGiveStatusEffect(Player user, Player target)
	{
		return true;
	}

	@Override
	public int getAmplifierGivenCharge()
	{
		return 0;
	}

	@Override
	public void giveStatusEffect(Entity user, LivingEntity target, int duration)
	{
		if(target.hasEffect(BFEffects.REVELATION))
		{
			MobEffectInstance currentInstance = target.getEffect(BFEffects.REVELATION);
			int newDuration = currentInstance.getDuration() - duration;
			target.removeEffect(BFEffects.REVELATION);
			if(newDuration > 0)
			{
				MobEffectInstance instance = new MobEffectInstance(getStatusEffect(), duration, getAmplifierGivenCharge(), false, true, true);
				target.addEffect(instance, user);
			}
		}
		else {
			MobEffectInstance instance = new MobEffectInstance(getStatusEffect(), duration, getAmplifierGivenCharge(), false, true, true);
			target.addEffect(instance, user);
		}
	}

}
