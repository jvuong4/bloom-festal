package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.HealthOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class HealOrb extends HealthOrb {
	public HealOrb(final EntityType<? extends HealOrb> type, final Level level) {
		super(type, level);
		isHealing = true;
	}

	public HealOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, level, mob, direction);
		isHealing = true;
	}

	public HealOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, level, x, y, z, direction);
		isHealing = true;
	}

	@Override
	protected void initVals()
	{
		switch(charge)
		{
			//bloom
			case 0:
				accelerationPower = 0.8;
				range = 6;
				potency = 1F;
				explosionRadius = 0.0F;
				break;
			case 1:
				accelerationPower = 0.9;
				range = 6;
				potency = 3F;
				explosionRadius = 4.0F;
				break;
			case 2:
				accelerationPower = 1;
				range = 6;
				potency = 5F;
				explosionRadius = 6.0F;
				break;
			//sun/dawn
			case 3:
				accelerationPower = 0.8;
				range = 6;
				potency = 4F;
				explosionRadius = 0.0F;
				break;
			case 4:
				accelerationPower = 0.9;
				range = 6;
				potency = 7F;
				explosionRadius = 4.0F;
				break;
			case 5:
				accelerationPower = 1;
				range = 15;
				potency = 10F;
				explosionRadius = 6.0F;
				break;
			//wane/horizon
			case 6:
				accelerationPower = 0.8;
				range = 15;
				potency = 1F;
				explosionRadius = 6.0F;
				break;
			case 7:
				accelerationPower = 0.9;
				range = 18;
				potency = 3F;
				explosionRadius = 8.0F;
				break;
			case 8:
				accelerationPower = 1;
				range = 20;
				potency = 5F;
				explosionRadius = 10.0F;
				break;
			//moon/twilight
			case 9:
				accelerationPower = 0.8;
				range = 6;
				potency = 10F;
				explosionRadius = 0.0F;
				break;
			case 10:
				accelerationPower = 0.9;
				range = 6;
				potency = 14F;
				explosionRadius = 4.0F;
				break;
			case 11:
				accelerationPower = 1;
				range = 6;
				potency = 18F;
				explosionRadius = 6.0F;
				break;
			default:
				accelerationPower = 0.8;
				range = 6;
				potency = 1F;
				explosionRadius = 0.0F;
				break;
		}
		particleSpawnChance = 2.0F;
		explosionSound = BFSounds.HEAL;
	}

}
