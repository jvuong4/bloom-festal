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

public class SilenceOrb extends StatusEffectOrb {
	public SilenceOrb(final EntityType<? extends SilenceOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public SilenceOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.SILENCE_ORB, level, mob, direction);
		initVals();
	}

	public SilenceOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.SILENCE_ORB, level, x, y, z, direction);
		initVals();
	}

	@Override
	protected void initVals()
	{
		switch(charge)
		{
			case 1:
				accelerationPower = 0.7;
				range = 9;
				potency = 15F;
				explosionRadius = 2.5F;
				break;
			case 2:
				accelerationPower = 0.7;
				range = 9;
				potency = 30F;
				explosionRadius = 3F;
				break;
			case 0:
			default:
				accelerationPower = 0.7;
				range = 9;
				potency = 10F;
				explosionRadius = 0.0f;
				break;
		}
		particleSpawnChance = 2F;
		explosionSound = SoundEvents.TRIDENT_THUNDER.value();
		damageParticle = ParticleTypes.ENCHANT;
	}

	@Override
	public Holder<MobEffect> getStatusEffect()
	{return BFEffects.SILENCE;}

	//by default, assumes status effect is negative and therefore only affects attackable players
	@Override
	public boolean canGiveStatusEffect(Player user, Player target)
	{
		return (user.getUUID() != target.getUUID()) && user.canHarmPlayer(target);
	}

	@Override
	public int getAmplifierGivenCharge()
	{
		return 0;
	}

	@Override
	public void giveStatusEffect(Entity user, LivingEntity target, int duration)
	{
		MobEffectInstance instance = new MobEffectInstance(getStatusEffect(), duration, getAmplifierGivenCharge(), false, true, true);
		target.addEffect(instance, user);
	}

}
