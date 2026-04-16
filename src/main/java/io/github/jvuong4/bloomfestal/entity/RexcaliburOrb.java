package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.entity.LightningBolt.VisualLightning;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class RexcaliburOrb extends ExplodingOrb{
	private static final ExplosionDamageCalculator DEFAULT_EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
		false, false, Optional.of(3F), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
	);

	public RexcaliburOrb(final EntityType<? extends RexcaliburOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public RexcaliburOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.THORON_ORB, level, mob, direction);
		initVals();
	}

	public RexcaliburOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.THORON_ORB, level, x, y, z, direction);
		initVals();
	}

	protected void initVals()
	{
		accelerationPower = 1.0;
		range = 8;
		potency = 10.0F;
		explosionRadius = 7.0F;
		particleSpawnChance = 2.0F;
		explosionSound = SoundEvents.BREEZE_WIND_CHARGE_BURST.value();
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.GUST_EMITTER_SMALL;
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
		return ParticleTypes.GUST_EMITTER_LARGE;
	}
	@Override
	public ExplosionDamageCalculator getExplosionDamageCalculator() {return DEFAULT_EXPLOSION_DAMAGE_CALCULATOR;}

	@Override
	protected void explode(final ServerLevel level) {
		level.broadcastEntityEvent(this, (byte)17);
		this.gameEvent(GameEvent.EXPLODE, this.getOwner());
		this.dealExplosionDamage(level);
		this.level().explode(this, null,
			getExplosionDamageCalculator(),
			this.getX(), this.getY(), this.getZ(), 2.4F, false,
			Level.ExplosionInteraction.NONE,
			smallExplosionParticle(),
			largeExplosionParticle(),
			WeightedList.of(),
			SoundEvents.WIND_CHARGE_BURST
		);
		this.discard();
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
						if(!target.onGround() && !target.isInPowderSnow && !target.isInWater() && !target.isInLava())
							//double damage against airborne enemies!
							damage *= 2;
						target.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), damage);

					}
				}


			}
		}
	}

}
