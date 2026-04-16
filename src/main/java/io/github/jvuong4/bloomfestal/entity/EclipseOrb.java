package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EclipseOrb extends Fireball {
	private int range = 640;
	private int age = 0;

	public EclipseOrb (final EntityType<? extends EclipseOrb> type, final Level level) {
		super(type, level);
		accelerationPower = 0.03;
	}


	public EclipseOrb (final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.ECLIPSE_ORB, mob, direction, level);
		accelerationPower = 0.03;
	}

	public EclipseOrb (final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.ECLIPSE_ORB, x, y, z, direction, level);
		accelerationPower = 0.03;
	}

	public void setStats(int r)
	{
		range = r;
		age = 0;
		accelerationPower = 0.03;
	}

	@Override
	protected void createParticleTrail() {
		//less particles!!
		if(this.level().getRandom().nextFloat() > 0.5F)
		{
			return;
		}
		ParticleOptions trailParticle = this.getTrailParticle();
		Vec3 position = this.position();
		if (trailParticle != null) {
			this.level().addParticle(trailParticle, position.x, position.y, position.z, 0.0, 0.0, 0.0);
		}
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.SCULK_SOUL;
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	protected void onHitEntity(final EntityHitResult hitResult) {
		if (this.level() instanceof ServerLevel serverLevel) {
			Entity var7 = hitResult.getEntity();
			Entity owner = this.getOwner();
			//no burning!
			//int remainingFireTicks = var7.getRemainingFireTicks();
			//var7.igniteForSeconds(5.0F);

			if(var7 instanceof LivingEntity mob)
			{
				DamageSource damageSource = this.damageSources().indirectMagic(this, owner);
				playSound(SoundEvents.TRIDENT_THUNDER.value(),0.3f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));

				boolean lethal = mob.getHealth() <= 1.0F;
				float minDamage = lethal ? 2048.0F : 1.0F;
				if (!var7.hurtServer(serverLevel, damageSource, Math.max(mob.getHealth()-1.0F, minDamage))) {
					if(mob.getHealth() > 1F)
						mob.setHealth(1.0F);
					//var7.setRemainingFireTicks(remainingFireTicks);
				} else {
					if(mob.getHealth() > 1F)
						mob.setHealth(1.0F);
					EnchantmentHelper.doPostAttackEffects(serverLevel, var7, damageSource);
				}
				//if they're somehow still not dead yet LMFAOO
				if(lethal)
					mob.kill(serverLevel);
			}
		}
		super.onHitEntity(hitResult);
	}

	@Override
	public void tick() {
		super.tick();
		age++;
		if(age == range && range <= 10)
		{
			this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.2, 0.0);
			for(int i = 0; i < 4; i++)
			{
				this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY()+0.5, this.getZ(), this.level().getRandom().nextFloat() * 0.2f - 0.1f, this.level().getRandom().nextFloat() * 0.2F, this.level().getRandom().nextFloat() * 0.2f - 0.1f);
			}
		}
		if(age > range)
		{
			playSound(SoundEvents.BAMBOO_PLACE,5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
			this.discard();
		}
	}

	@Override
	protected void onHitBlock(final BlockHitResult hitResult) {

		playSound(SoundEvents.BAMBOO_PLACE,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
		super.onHitBlock(hitResult);
	}

	@Override
	protected void onHit(final HitResult hitResult) {
		super.onHit(hitResult);
		if (hitResult.getType() == HitResult.Type.BLOCK) {
			this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY()+0.5, this.getZ(), 0.0, 0.2, 0.0);
			for(int i = 0; i < 4; i++)
			{
				this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY()+0.5, this.getZ(), this.level().getRandom().nextFloat() * 0.2f - 0.1f, this.level().getRandom().nextFloat() * 0.2F, this.level().getRandom().nextFloat() * 0.2f - 0.1f);
			}
		}

		if (!this.level().isClientSide()) {
			this.discard();
		}
	}

}
