package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.registry.BFEffects;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class RevelationOrb extends Fireball {
	private int range = 6;
	private float potency = 6.0F;
	private int age = 0;

	public RevelationOrb(final EntityType<? extends RevelationOrb> type, final Level level) {
		super(type, level);
		age = 0;
		accelerationPower = 1;
	}


	public RevelationOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.REVELATION_ORB, mob, direction, level);
		age = 0;
		accelerationPower = 1;
	}

	public RevelationOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.REVELATION_ORB, x, y, z, direction, level);
		age = 0;
		accelerationPower = 1;
	}

	public void setStats(int r, float p)
	{
		range = r;
		potency = p;
		age = 0;
		accelerationPower = 1;
	}
	@Override
	protected void createParticleTrail() {
		ParticleOptions trailParticle = this.getTrailParticle();
		Vec3 position = this.position();
		if (trailParticle != null) {
			this.level().addParticle(trailParticle, position.x, position.y, position.z, 0.0, 0.0, 0.0);
			Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
			this.level().addParticle(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 0.0, 0.0, 0.0);
		}
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.SONIC_BOOM;
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
			if(var7 instanceof LivingEntity mob)
			{
				if(mob.isInvertedHealAndHarm())
				{
					DamageSource damageSource = this.damageSources().indirectMagic(this, owner);
					playSound(SoundEvents.TRIDENT_THUNDER.value(),0.3f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
					if (!var7.hurtServer(serverLevel, damageSource, potency)) {
					} else {
						EnchantmentHelper.doPostAttackEffects(serverLevel, var7, damageSource);
					}
				}
				else
				{
					playSound(SoundEvents.TRIDENT_THUNDER.value(),0.3f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
					mob.heal(potency);
					MobEffectInstance instance = new MobEffectInstance(MobEffects.GLOWING,  10, 0, false, false, false);
					mob.addEffect(instance,owner);
				}
				if(mob.hasEffect(BFEffects.REVELATION))
				{
					mob.removeEffect(BFEffects.REVELATION);
				}
				else
				{
					MobEffectInstance instance = new MobEffectInstance(BFEffects.REVELATION,  6000, 0, false, true, true);
					mob.addEffect(instance, owner);
				}
			}
		}
		else
		{
			Entity mob = hitResult.getEntity();
			if(mob instanceof LivingEntity livingEntity) {
				for(int i = 0; i < 3; i++) {
					double xa = this.random.nextGaussian() * 0.02;
					double ya = this.random.nextGaussian() * 0.02;
					double za = this.random.nextGaussian() * 0.02;
					this.level().addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, mob.getRandomX(1.0), mob.getRandomY() + 0.5, mob.getRandomZ(1.0), xa, ya, za);
				}
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
