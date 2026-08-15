package io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NosferatuOrb extends ExplodingOrb {
	public NosferatuOrb(final EntityType<? extends NosferatuOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public NosferatuOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.NOSFERATU_ORB, level, mob, direction);
		initVals();
	}

	public NosferatuOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.NOSFERATU_ORB, level, x, y, z, direction);
		initVals();
	}

	@Override
	protected void initVals()
	{
		switch(charge)
		{
			case 1:
				accelerationPower = 1;
				range = 20;
				potency = 8F;
				explosionRadius = 2.5F;
				break;
			case 2:
				accelerationPower = 1;
				range = 20;
				potency = 10F;
				explosionRadius = 3F;
				break;
			case 0:
			default:
				accelerationPower = 1;
				range = 20;
				potency = 6F;
				explosionRadius = 0.0f;
				break;
		}
		particleSpawnChance = 0.5F;
		explosionSound = SoundEvents.TRIDENT_THUNDER.value();
		damageParticle = ParticleTypes.TRIAL_OMEN;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.SOUL;
	}

	@Override
	public SimpleParticleType smallExplosionParticle()
	{
		return ParticleTypes.GUST_EMITTER_SMALL;
	}
	@Override
	public SimpleParticleType largeExplosionParticle()
	{
		return ParticleTypes.GUST_EMITTER_LARGE;
	}

	@Override
	public void onLifeOver() {
		if(explosionRadius <= 0)
			this.discard();
		else
			super.onLifeOver();
	}

	@Override
	public void SingleTargetEffect(LivingEntity target, ServerLevel level) {
		float damage = potency;
		Entity owner = this.getOwner();
		if(owner instanceof LivingEntity livingOwner)
			livingOwner.heal(potency/2f);
		target.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), damage);
		spawnDamageParticles(target, damageParticle, level);
	}

	@Override
	protected void dealExplosionDamage(final ServerLevel level) {
		Vec3 rocketPos = this.position();

		for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(explosionRadius))) {
			if (!(this.distanceToSqr(target) > explosionRadius*explosionRadius)) {
				if(!(target.getUUID() == owner.getUUID()))
				{
					boolean canSee = false;
					for (int testStep = 0; testStep < 2; testStep++) {
						Vec3 to = new Vec3(target.getX(), target.getY(0.5 * testStep), target.getZ());
						HitResult clip = this.level().clip(new ClipContext(rocketPos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
						if (clip.getType() == HitResult.Type.MISS) {
							canSee = true;
							break;
						}
					}
					if (canSee) {
						//remove rapid decay for consistent damage
						float damage = potency * (float)Math.sqrt((explosionRadius - this.distanceTo(target)) / explosionRadius);
						target.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), damage);
						Entity user = getOwner();
						if(user instanceof LivingEntity livingOwner)
							livingOwner.heal(potency/2f);
						spawnDamageParticles(target, damageParticle, level);
					}
				}


			}
		}
	}
}
