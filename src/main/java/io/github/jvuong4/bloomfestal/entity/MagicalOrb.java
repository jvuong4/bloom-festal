package io.github.jvuong4.bloomfestal.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

abstract public class MagicalOrb extends Fireball {
	protected int range = 32;
	private int age = 0;
	public int charge = 0;
	protected float particleSpawnChance = 0.5F;

	public MagicalOrb(final EntityType<? extends MagicalOrb> type, final Level level) {
		super(type, level);
		age = 0;
		accelerationPower = 0.1;
	}

	public MagicalOrb(final EntityType<? extends MagicalOrb> type, final Level level, final LivingEntity mob, final Vec3 direction) {
		super(type, mob, direction, level);
		age = 0;
		accelerationPower = 0.1;
	}

	public MagicalOrb(final EntityType<? extends MagicalOrb> type, final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(type, x, y, z, direction, level);
		age = 0;
		accelerationPower = 0.1;
	}

	public void setCharge(int val) {charge = val;}

	@Override
	protected void createParticleTrail() {
		if(particleSpawnChance <= 0.0F) {//do not attempt to make particles
		}
		else if(particleSpawnChance < 1.0F) {
			if(age % (int)(1.0F / particleSpawnChance) == 0) {
				ParticleOptions trailParticle = this.getTrailParticle();
				Vec3 position = this.position();
				if (trailParticle != null) {
					if (this.level() instanceof ServerLevel serverLevel)
						serverLevel.sendParticles(trailParticle, position.x, position.y, position.z, 1, 0.0, 0.0, 0.00, 0.0);
				}
			}
		}
		else
		{
			ParticleOptions trailParticle = this.getTrailParticle();
			Vec3 position = this.position();
			if (trailParticle != null) {
				if (this.level() instanceof ServerLevel serverLevel) {
					serverLevel.sendParticles(trailParticle, position.x, position.y, position.z, 1, 0.0, 0.0, 0.00, 0.0);
					if(particleSpawnChance > 1.0F) {
						Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
						serverLevel.sendParticles(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 1, 0.0, 0.0, 0.0, 0.0);
					}
				}
			}

		}
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		age++;
		if(age > range)
		{
			if (this.level() instanceof ServerLevel serverLevel) {
				this.onLifeOver();
			}
		}
	}

	public void onLifeOver() {
		this.discard();
	}

	@Override
	public @Nullable Entity getOwner() {
		if(super.getOwner() == null)//EntityReference.getEntity(this.owner, this.level());
		{
			return this;
		}
		return super.getOwner();
	}
}
