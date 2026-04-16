package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.registry.BFEntities;
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
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

abstract public class ExplodingOrb extends Fireball {
	private static final ExplosionDamageCalculator DEFAULT_EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
		false, false, Optional.of(0F), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
	);
	protected int range = 32;
	protected float potency = 14.0F;
	protected float explosionRadius = 5.0F;
	protected float particleSpawnChance = 0.5F;
	protected SoundEvent explosionSound = null;


	private int age = 0;

	public ExplodingOrb(final EntityType<? extends ExplodingOrb> type, final Level level) {
		super(type, level);
		age = 0;
		accelerationPower = 0.1;
	}

	public ExplodingOrb(final EntityType<? extends ExplodingOrb> type, final Level level, final LivingEntity mob, final Vec3 direction) {
		super(type, mob, direction, level);
		age = 0;
		accelerationPower = 0.1;
	}

	public ExplodingOrb(final EntityType<? extends ExplodingOrb> type, final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(type, x, y, z, direction, level);
		age = 0;
		accelerationPower = 0.1;
	}

	@Override
	protected void createParticleTrail() {
		//less particles!!
		if(this.level().getRandom().nextFloat() > particleSpawnChance)
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
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	protected void onHitEntity(final EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			Entity var7 = hitResult.getEntity();
			Entity owner = this.getOwner();
			//no burning!
			//int remainingFireTicks = var7.getRemainingFireTicks();
			//var7.igniteForSeconds(5.0F);

			if(var7 instanceof LivingEntity mob)
			{
				DamageSource damageSource = this.damageSources().indirectMagic(this, owner);
				if(explosionSound != null)
					playSound(explosionSound,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
				explode(serverLevel);
			}

		}
	}

	@Override
	public void tick() {
		super.tick();
		age++;
		if(age > range)
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

				}
			}
		}
	}

}
