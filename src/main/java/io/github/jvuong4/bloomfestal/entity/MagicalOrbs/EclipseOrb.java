package io.github.jvuong4.bloomfestal.entity.MagicalOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.registry.BFDamageTypes;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EclipseOrb extends MagicalOrb {
	private int age = 0;

	public EclipseOrb (final EntityType<? extends EclipseOrb> type, final Level level) {
		super(type, level);
		accelerationPower = 0.005;
	}


	public EclipseOrb (final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.ECLIPSE_ORB, level, mob, direction);
		accelerationPower = 0.005;
	}

	public EclipseOrb (final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.ECLIPSE_ORB, level, x, y, z, direction);
		accelerationPower = 0.005;
	}

	@Override
	public void setCharge(int val)
	{
		super.setCharge(val);
		setStats();
	}

	public void setStats()
	{
		age = 0;
		range = 400;
		switch(charge)
		{
			case 0:
				accelerationPower = 0.002;
				particleSpawnChance = 0.1F;
				range *= 0.75;
				break;
			case 1:
				accelerationPower = 0.005;
				particleSpawnChance = 0.2F;
				break;
			case 2:
				accelerationPower = 0.02;
				particleSpawnChance = 0.3F;
				range *= 1.25;
				break;
			default:
				accelerationPower = 0.002;
				particleSpawnChance = 0.1F;
				range *= 0.75;
				break;
		}


	}

	/*
	@Override
	protected void createParticleTrail() {
		//less particles!!
		if(age % 100 == 0)
		{
			ParticleOptions trailParticle;
			trailParticle = this.getTrailParticle();
			Vec3 position = this.position();
			if (trailParticle != null) {
				if(this.level() instanceof ServerLevel serverLevel)
					serverLevel.sendParticles(trailParticle, position.x, position.y, position.z, 1, 0.0, 0.0, 0.00, 0.0);
			}
		}
	}
	 */

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.SONIC_BOOM;
	}

	private boolean cannotHarmPlayer(LivingEntity target)
	{
		Entity owner = this.getOwner();
		if(owner instanceof Player playerAttacker)
			if(target instanceof Player playerTarget)
				return !playerAttacker.canHarmPlayer(playerTarget);
		return false;
	}

	@Override
	protected void onHitEntity(final EntityHitResult hitResult) {
		if (this.level() instanceof ServerLevel serverLevel) {
			Entity var7 = hitResult.getEntity();
			Entity owner = this.getOwner();

			if(var7 instanceof LivingEntity mob)
			{
				DamageSource damageSource = this.damageSources().source(BFDamageTypes.ECLIPSE_DAMAGE,this, owner);
				playSound(SoundEvents.TRIDENT_THUNDER.value(),0.3f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));

				if (!(
						mob.isInvulnerable() //does nothing against invulnerable mobs
					|| cannotHarmPlayer(mob)	//does nothing against players that this orb's owner cannot hurt
				))
				{
					boolean lethal = mob.getHealth() <= 1.0F;
					float minDamage = lethal ? 2048.0F : 1.0F;
					if (var7.hurtServer(serverLevel, damageSource, Math.max(mob.getHealth() - 1.0F, minDamage)))
					{
						EnchantmentHelper.doPostAttackEffects(serverLevel, var7, damageSource);
						for(int count = 0; count < 3; count++) {
							serverLevel.sendParticles(ParticleTypes.TRIAL_OMEN,
								mob.getRandomX(1.0), mob.getY(0.5), mob.getRandomZ(1.0), 1, 0.02, 0.02, 0.02, 0.0);
						}
					}
				}
			}
		}
		super.onHitEntity(hitResult);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void onHitBlock(final BlockHitResult hitResult) {

		playSound(SoundEvents.BAMBOO_PLACE,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
		super.onHitBlock(hitResult);
	}

	@Override
	protected void onHit(final HitResult hitResult) {
		super.onHit(hitResult);
		if (!this.level().isClientSide()) {
			this.discard();
		}
	}

}
