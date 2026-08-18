package io.github.jvuong4.bloomfestal.entity.MagicalOrbs;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class StatusEffectOrb extends MagicalOrb {
	//potency in this case refers to how long the effect should last for, in seconds
	protected float potency = 14.0F;
	protected float explosionRadius = 5.0F;

	protected SoundEvent explosionSound = null;
	protected SimpleParticleType damageParticle = ParticleTypes.ENCHANTED_HIT;

	public StatusEffectOrb(final EntityType<? extends StatusEffectOrb> type, final Level level) {
		super(type, level);
		accelerationPower = 0.1;
	}


	public StatusEffectOrb(final EntityType<? extends StatusEffectOrb> type, final Level level, final LivingEntity mob, final Vec3 direction) {
		super(type, level, mob, direction);
		accelerationPower = 0.1;
	}

	public StatusEffectOrb(final EntityType<? extends StatusEffectOrb> type, final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(type,level, x, y, z, direction);
		accelerationPower = 0.1;
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
			if(var7 instanceof LivingEntity mob)
			{
				if(explosionSound != null)
					playSound(explosionSound,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
				if(mob instanceof Player playerTarget && owner instanceof Player playerOwner)
				{
					if(canGiveStatusEffect(playerOwner, playerTarget))
					{
						giveStatusEffect(playerOwner,playerTarget,(int)(potency * 20));
					}
				}
				else
				{
					giveStatusEffect(owner,mob,(int)(potency * 20));
				}

				explode(serverLevel);
			}

		}
	}

	@Override
	public void onLifeOver() {
		if(explosionRadius <= 0)
			this.discard();
		else
		{
			if (this.level() instanceof ServerLevel serverLevel) {
				this.explode(serverLevel);
			}
		}
	}

	@Override
	protected void onHitBlock(final BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			onLifeOver();
		}
	}

	@Override
	protected void onHit(final HitResult hitResult) {
		super.onHit(hitResult);
	}

	protected void explode(final ServerLevel level) {
		level.broadcastEntityEvent(this, (byte)17);
		this.gameEvent(GameEvent.EXPLODE, this.getOwner());
		this.dealExplosionDamage(level);
		this.discard();
	}

	public Holder<MobEffect> getStatusEffect()
	{return BFEffects.SILENCE;}

	//by default, assumes status effect is negative and therefore only affects attackable players
	public boolean canGiveStatusEffect(Player user, Player target)
	{
		return (user.getUUID() != target.getUUID()) && user.canHarmPlayer(target);
	}

	public int getAmplifierGivenCharge()
	{
		return 0;
	}

	public void giveStatusEffect(Entity user, LivingEntity target, int duration)
	{
		MobEffectInstance instance = new MobEffectInstance(getStatusEffect(), duration, getAmplifierGivenCharge(), false, true, true);
		target.addEffect(instance, user);
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
					float durationRaw = potency * (float)Math.sqrt((explosionRadius - this.distanceTo(target)) / explosionRadius);
					int duration = (int)(durationRaw * 20);
					if(getOwner() instanceof Player playerOwner && target instanceof Player playerTarget) {
						if (canGiveStatusEffect(playerOwner, playerTarget)) {
							giveStatusEffect(playerOwner, playerTarget, duration);
						}
					}
						else
						{
							giveStatusEffect(getOwner(), target, duration);
						}
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
