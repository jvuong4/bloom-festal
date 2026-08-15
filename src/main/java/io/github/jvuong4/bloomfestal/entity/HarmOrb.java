package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.HealthOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
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

public class HarmOrb extends HealthOrb {
	public HarmOrb(final EntityType<? extends HarmOrb> type, final Level level) {
		super(type, level);
		isHealing = false;
		initVals();
	}

	public HarmOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.HARM_ORB, level, mob, direction);
		isHealing = false;
		initVals();
	}

	public HarmOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.HARM_ORB, level, x, y, z, direction);
		isHealing = false;
		initVals();
	}

	@Override
	protected void initVals() {
		switch (charge) {
			case 0:
				accelerationPower = 0.8;
				range = 6;
				potency = 3F;
				explosionRadius = 0.0F;
				break;
			case 1:
				accelerationPower = 0.9;
				range = 6;
				potency = 5F;
				explosionRadius = 4.0F;
				break;
			case 2:
				accelerationPower = 1;
				range = 6;
				potency = 7F;
				explosionRadius = 6.0F;
				break;
			default:
				accelerationPower = 0.8;
				range = 6;
				potency = 3F;
				explosionRadius = 0.0F;
				break;
		}
		particleSpawnChance = 2.0F;
		explosionSound = SoundEvents.AMBIENT_UNDERWATER_ENTER;
	}
}
