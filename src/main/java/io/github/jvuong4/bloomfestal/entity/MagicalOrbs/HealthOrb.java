package io.github.jvuong4.bloomfestal.entity.MagicalOrbs;

import io.github.jvuong4.bloomfestal.registry.BFParticles;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

abstract public class HealthOrb extends ExplodingOrb {
	protected boolean isHealing = true; //if false, is harming

	public HealthOrb(final EntityType<? extends HealthOrb> type, final Level level) {
		super(type, level);
	}


	public HealthOrb(final EntityType<? extends HealthOrb> type, final Level level, final LivingEntity mob, final Vec3 direction) {
		super(type, level, mob, direction);
	}

	public HealthOrb(final EntityType<? extends HealthOrb> type, final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(type,level, x, y, z, direction);
	}

	@Override
	protected void explode(final ServerLevel level)
	{
		this.dealExplosionDamage(level);
		spawnMagicCircle((int)(explosionRadius * 2 + 4), explosionRadius>0 ? explosionRadius : 1.0, level);
		this.discard();
	}

	private void spawnMagicCircle(int count, double range, ServerLevel level)
	{
		double pivot = this.getRandom().nextDouble();
		for(double i=0; i<count; i++)
		{
			level.sendParticles(getTrailParticle(),
				this.getX() + Math.cos((i+pivot)/count * 2.0 * Math.PI) * range,
				this.getY() + 0.5,
				this.getZ() + Math.sin((i+pivot)/count * 2.0 * Math.PI) * range,
				1, 0.0, 0.5, 0.0, 0.0);
		}
	}

	public void SingleTargetEffect(LivingEntity target, ServerLevel level) {
		Affect(target, level, potency);
	}

	@Override
	protected void dealExplosionDamage(final ServerLevel level) {
		Vec3 rocketPos = this.position();

		for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(explosionRadius))) {
			if (!(this.distanceToSqr(target) > explosionRadius*explosionRadius)) {
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
					Affect(target,level,damage);
					spawnDamageParticles(target, getDamageParticle(), level);

				}
			}
		}
	}



	public void Affect(LivingEntity entity, ServerLevel level, float health) {
		//deal damage if enemy is undead and you are using healing magic
		//deal damage if enemy is normal and you are using harming magic
		if(entity.isInvertedHealAndHarm() == isHealing)
		{
			entity.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), health);
			spawnDamageParticles(entity, getDamageParticle(), level);
		}
		else
		{
			this.playSound(BFSounds.HEAL, 2f, 1F);
			MobEffectInstance instance = new MobEffectInstance(MobEffects.GLOWING,  10, 0, false, false, false);
			entity.addEffect(instance,this.getOwner());
			spawnDamageParticles(entity, getHealingParticle(), level);
			entity.heal(potency);
		}
	}

	protected ParticleOptions getHealingParticle() {
		return ParticleTypes.HEART;
	}
	protected ParticleOptions getDamageParticle() {
		return ParticleTypes.SOUL_FIRE_FLAME;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return isHealing ? ParticleTypes.CHERRY_LEAVES : BFParticles.HARM_PETALS_PARTICLE;
	}
}
