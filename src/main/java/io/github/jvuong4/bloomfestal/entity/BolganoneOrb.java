package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.entity.LightningBolt.VisualLightning;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class BolganoneOrb extends ExplodingOrb{

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

	protected void initVals()
	{
		accelerationPower = 0.8;
		range = 10;
		potency = 8.0F;
		explosionRadius = 3.0F;
		particleSpawnChance = 2.0F;
		explosionSound = SoundEvents.FIRECHARGE_USE;
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
			this.level().addParticle(trailParticle, position.x, position.y, position.z, 0.0, 0.0, 0.0);
			Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
			this.level().addParticle(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 0.0, 0.0, 0.0);
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
			//YES burning!


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
					float burnTicks = 5.0F * (float)Math.sqrt((explosionRadius - this.distanceTo(target)) / explosionRadius);

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
					if(!target.hurtServer(level, this.damageSources().fireball(this, this.getOwner()), damage));
					{
						//BloomFestal.LOGGER.info("[Bloom Festal] undid burning on entity");
						//target.setRemainingFireTicks(remainingFireTicks);
					}

				}
			}
		}
	}

}
