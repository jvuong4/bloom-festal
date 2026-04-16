package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class HealOrb extends Fireball {
	public static final ExplosionDamageCalculator DEFAULT_EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
		false, false, Optional.of(0F), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
	);

	private int range = 6;
	private float potency = 4.0F;
	private int age = 0;


	public HealOrb(final EntityType<? extends HealOrb> type, final Level level) {
		super(type, level);
		age = 0;
		accelerationPower = 1;
	}


	public HealOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, mob, direction, level);
		age = 0;
		accelerationPower = 1;
	}

	public HealOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, x, y, z, direction, level);
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
			if(this.level().getRandom().nextFloat() < 0.3F)
			{
				Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
				this.level().addParticle(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 0.0, 0.0, 0.0);
			}
		}
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.CHERRY_LEAVES;
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
				if(mob.isInvertedHealAndHarm())
				{
					DamageSource damageSource = this.damageSources().indirectMagic(this, owner);
					playSound(SoundEvents.BAMBOO_PLACE,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
					if (!var7.hurtServer(serverLevel, damageSource, potency)) {
						//var7.setRemainingFireTicks(remainingFireTicks);
					} else {
						EnchantmentHelper.doPostAttackEffects(serverLevel, var7, damageSource);
					}
					var7.level().explode(this, null,
						DEFAULT_EXPLOSION_DAMAGE_CALCULATOR,
						var7.getX(), var7.getY(0.5) + 0.5, var7.getZ(), 1.2F, false,
						Level.ExplosionInteraction.NONE,
						ParticleTypes.CHERRY_LEAVES,
						ParticleTypes.CHERRY_LEAVES,
						WeightedList.of(),
						SoundEvents.HONEY_DRINK
					);
				}
				else
				{
					double xa = this.random.nextGaussian() * 0.02;
					double ya = this.random.nextGaussian() * 0.02;
					double za = this.random.nextGaussian() * 0.02;
					mob.level().addParticle(ParticleTypes.HEART, mob.getRandomX(1.0), mob.getRandomY() + 0.5, mob.getRandomZ(1.0), xa, ya, za);
					//this.level().addParticle(ParticleTypes.HEART, var7.getX(), var7.getY() + 0.5, var7.getZ(), 0.0, 1.0, 0.0);
					//this.level().addParticle(ParticleTypes.HEART, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 1.0, 0.0);
					//mob.playSound(SoundEvents.ALLAY_ITEM_GIVEN,2f,0.4F / (level().getRandom().nextFloat() + 0.8F));
					var7.level().explode(this, null,
						DEFAULT_EXPLOSION_DAMAGE_CALCULATOR,
						var7.getX(), var7.getY(0.5) + 0.5, var7.getZ(), 1.2F, false,
						Level.ExplosionInteraction.NONE,
						ParticleTypes.CHERRY_LEAVES,
						ParticleTypes.HEART,
						WeightedList.of(),
						SoundEvents.HONEY_DRINK
					);
					mob.heal(potency);
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
