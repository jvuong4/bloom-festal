package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class WhirlpoolOrb extends ExplodingOrb{
	private static final ExplosionDamageCalculator DEFAULT_EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
		false, false, Optional.of(1F), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
	);

	public WhirlpoolOrb(final EntityType<? extends WhirlpoolOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public WhirlpoolOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.WHIRLPOOL_ORB, level, mob, direction);
		initVals();
	}

	public WhirlpoolOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.WHIRLPOOL_ORB, level, x, y, z, direction);
		initVals();
	}

	protected void initVals()
	{
		accelerationPower = 0.4;
		range = 20;
		potency = 4.0F;
		explosionRadius = 5.0F;
		particleSpawnChance = 2.0F;
		explosionSound = SoundEvents.AMBIENT_UNDERWATER_ENTER;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return BFParticles.BUBBLE_PARTICLE;
	}
	@Override
	protected void createParticleTrail() {
		//i just LOVE the smell of particles in the morning!
		ParticleOptions trailParticle = this.getTrailParticle();
		Vec3 position = this.position();
		if (trailParticle != null) {
			double xa = this.level().getRandom().nextGaussian() * 0.02;
			double ya = this.level().getRandom().nextGaussian() * 0.02;
			double za = this.level().getRandom().nextGaussian() * 0.02;
			this.level().addParticle(trailParticle, position.x, position.y, position.z, xa, ya, za);
			this.level().addParticle(ParticleTypes.RAIN, position.x, position.y, position.z, xa, ya*5f, za);
			Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
			xa = this.level().getRandom().nextGaussian() * 0.02;
			ya = this.level().getRandom().nextGaussian() * 0.02;
			za = this.level().getRandom().nextGaussian() * 0.02;
			this.level().addParticle(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z,  xa, ya, za);
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
		return BFParticles.BUBBLE_PARTICLE;
	}
	@Override
	public ExplosionDamageCalculator getExplosionDamageCalculator() {return DEFAULT_EXPLOSION_DAMAGE_CALCULATOR;}

	private boolean isWaterResistant(LivingEntity target)
	{
		if(target.canBreatheUnderwater() || MobEffectUtil.hasWaterBreathing(target))
			return true;
		return false;
	}

	@Override
	protected void dealExplosionDamage(final ServerLevel level) {
		Vec3 rocketPos = this.position();

		for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(explosionRadius))) {
			if(!(target.getUUID() == owner.getUUID())) {
				if (!(this.distanceToSqr(target) > explosionRadius * explosionRadius)) {
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
						float damage = potency * (float) Math.sqrt((explosionRadius - this.distanceTo(target)) / explosionRadius);
						if (damage > 1 && isWaterResistant(target))
							damage *= 0.5f;
						if (target.isOnFire() || target.isSensitiveToWater()) {
							damage *= 4;
						}
						DamageSource source = this.damageSources().indirectMagic(this, this.getOwner());
						if (target.hurtServer(level, source, damage)) {
							EnchantmentHelper.doPostAttackEffects(level, target, source);
						}

						if (target.isOnFire()) {
							target.extinguishFire();
						}
						if (target instanceof Axolotl axolotl) {
							axolotl.rehydrate();
						}
					}
				}
			}
			else {
				//very useful if you're burning!
				if (target.isOnFire()) {
					target.extinguishFire();
				}
				//very useful if you're an axolotl! somehow?
				if (target instanceof Axolotl axolotl) {
					axolotl.rehydrate();
				}
			}
		}
	}
}
