package io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BolganoneOrb extends ExplodingOrb {
	private float burnDuration = 5f;

	public BolganoneOrb(final EntityType<? extends BolganoneOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public BolganoneOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.BOLGANONE_ORB, level, mob, direction);
		initVals();
	}

	public BolganoneOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.BOLGANONE_ORB, level, x, y, z, direction);
		initVals();
	}

	@Override
	protected void initVals()
	{
		switch(charge)
		{
			case 1:
				accelerationPower = 0.8;
				range = 10;
				potency = 8F;
				explosionRadius = 3.5F;
				burnDuration = 8F;
				break;
			case 2:
				accelerationPower = 0.8;
				range = 15;
				potency = 10F;
				explosionRadius = 4.5F;
				burnDuration = 20F;
				break;
			case 0:
			default:
				accelerationPower = 0.8;
				range = 6;
				potency = 4F;
				explosionRadius = 3.0F;
				burnDuration = 5F;
				break;
		}
		particleSpawnChance = 2.0F;
		explosionSound = SoundEvents.FIRECHARGE_USE;
		damageParticle = ParticleTypes.FLAME;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.LAVA;
	}
	@Override
	protected void createParticleTrail() {
		//i just LOVE the smell of particles in the morning!
		ParticleOptions trailParticle = this.getTrailParticle();
		Vec3 position = this.position();
		if (trailParticle != null) {
			if(this.level() instanceof ServerLevel serverLevel)
			{
				serverLevel.sendParticles(trailParticle, position.x, position.y, position.z, 1, 0.0, 0.0, 0.00, 0.0);
				Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
				serverLevel.sendParticles(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 1, 0.0, 0.0, 0.0, 0.0);
			}
		}
	}
	@Override
	public SimpleParticleType smallExplosionParticle()
	{
		return ParticleTypes.GUST_EMITTER_SMALL;
	}
	@Override
	public SimpleParticleType largeExplosionParticle()
	{
		return ParticleTypes.LAVA;
	}

	@Override
	protected boolean shouldBurn() {
		return true;
	}

	@Override
	protected void onHitEntity(final EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			Entity var7 = hitResult.getEntity();
			Entity owner = this.getOwner();
			if(var7 instanceof LivingEntity mob)
			{
				if(explosionSound != null)
					playSound(explosionSound,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
				explode(serverLevel);
			}

		}
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
					float damage = potency * (float)Math.sqrt((explosionRadius - this.distanceTo(target)) / explosionRadius);
					float burnTicks = burnDuration * (float)Math.sqrt((explosionRadius - this.distanceTo(target)) / explosionRadius);

					if(!target.fireImmune()) {
						if (this.getOwner() instanceof Player playerOwner && target instanceof Player playerTarget) {
							if (playerOwner.canHarmPlayer(playerTarget)) {
								target.igniteForSeconds(burnTicks);
							}
						}
						else {
							target.igniteForSeconds(burnTicks);
						}
					}
					if(target.hurtServer(level, this.damageSources().fireball(this, this.getOwner()), damage))
					{
						spawnDamageParticles(target, damageParticle, level);
						spawnDamageParticles(target, ParticleTypes.LAVA, level);
					}

				}
			}
		}
	}

}
