package io.github.jvuong4.bloomfestal.entity.MagicalOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

abstract public class ExplodingOrb extends MagicalOrb {
	private static final ExplosionDamageCalculator DEFAULT_EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
		false, false, Optional.of(0F), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
	);
	protected float potency = 14.0F;
	protected float explosionRadius = 5.0F;

	protected SoundEvent explosionSound = null;
	protected SimpleParticleType damageParticle = ParticleTypes.ENCHANTED_HIT;

	public ExplodingOrb(final EntityType<? extends ExplodingOrb> type, final Level level) {
		super(type, level);
	}


	public ExplodingOrb(final EntityType<? extends ExplodingOrb> type, final Level level, final LivingEntity mob, final Vec3 direction) {
		super(type, level, mob, direction);
	}

	public ExplodingOrb(final EntityType<? extends ExplodingOrb> type, final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(type,level, x, y, z, direction);
	}

	@Override
	public void setCharge(int val)
	{
		super.setCharge(val);
		initVals();
	}

	abstract protected void initVals();

	@Override
	protected void onHitEntity(final EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			Entity var7 = hitResult.getEntity();
			Entity owner = this.getOwner();
			if(var7 instanceof LivingEntity target)
			{
				if(explosionSound != null)
					playSound(explosionSound,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
				if(explosionRadius > 0) {
					explode(serverLevel);
				}
				else {
					if(owner instanceof LivingEntity livingOwner)
						SingleTargetEffect(target,serverLevel);
				}

			}

		}
	}

	public void SingleTargetEffect(LivingEntity target, ServerLevel level) {
		float damage = potency;
		target.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), damage);
		spawnDamageParticles(target, damageParticle, level);
	}

	@Override
	public void onLifeOver() {
		if (this.level() instanceof ServerLevel serverLevel) {
			this.explode(serverLevel);
		}
	}

	@Override
	protected void onHitBlock(final BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			this.explode(serverLevel);
		}
	}

	@Override
	protected void onHit(final HitResult hitResult) {
		super.onHit(hitResult);
	}

	public SimpleParticleType smallExplosionParticle()
	{
		return ParticleTypes.GUST_EMITTER_SMALL;
	}
	public SimpleParticleType largeExplosionParticle()
	{
		return ParticleTypes.GUST_EMITTER_LARGE;
	}
	public ExplosionDamageCalculator getExplosionDamageCalculator() {return DEFAULT_EXPLOSION_DAMAGE_CALCULATOR;}

	protected void explode(final ServerLevel level) {
		level.broadcastEntityEvent(this, (byte)17);
		this.gameEvent(GameEvent.EXPLODE, this.getOwner());
		this.dealExplosionDamage(level);
		this.level().explode(this, null,
			getExplosionDamageCalculator(),
			this.getX(), this.getY(), this.getZ(), 1.2F, false,
			Level.ExplosionInteraction.NONE,
			smallExplosionParticle(),
			largeExplosionParticle(),
			WeightedList.of(),
			SoundEvents.WIND_CHARGE_BURST
			);
		this.discard();
	}
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
					target.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), damage);
					spawnDamageParticles(target, damageParticle, level);

				}
			}
		}
	}

	public <T extends ParticleOptions> void spawnDamageParticles(Entity entity, T particle, ServerLevel serverLevel)
	{
		for(int count = 0; count < 3; count++) {
			serverLevel.sendParticles(particle,
				entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.02, 0.02, 0.02, 0.0);
		}
	}

}
