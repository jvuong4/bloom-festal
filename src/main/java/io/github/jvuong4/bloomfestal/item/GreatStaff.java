package io.github.jvuong4.bloomfestal.item;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.EclipseOrb;
import io.github.jvuong4.bloomfestal.registry.BFDataComponents;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class GreatStaff extends Item {
	//time needed to charge for a strong effect
	protected int charge1time = 40;
	//time needed to charge for the next step
	protected int charge2time = 70;
	//time needed to charge for the next step
	protected int charge3time = 90;
	//time needed to charge for the next step
	protected int charge4time = 100;
	//time needed to charge for the strongest effect
	protected int charge5time = 110;

	public int getCharge1time() {
		return charge1time;
	}
	public int getCharge2time() {
		return charge2time;
	}
	public int getCharge3time() {
		return charge3time;
	}
	public int getCharge4time() {
		return charge4time;
	}
	public int getCharge5time() {
		return charge5time;
	}

	protected SimpleParticleType getChargedParticle() {return ParticleTypes.CHERRY_LEAVES;}
	protected SimpleParticleType getHealParticle() {return ParticleTypes.HEART;}
	protected SimpleParticleType getDamageParticle() {return ParticleTypes.SOUL_FIRE_FLAME;}
	protected SoundEvent getCastSound() {return BFSounds.HEAL;}

	public boolean isHealing = true;
	public float damageMultiplier = 0.5f;

	public GreatStaff(final Item.Properties properties) {super(properties);}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if(player.hasEffect(BFEffects.SILENCE))
		{
			level.playLocalSound(player, SoundEvents.SHIELD_BLOCK.value(), SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			return InteractionResult.FAIL;
		}
		else {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}
	}

	protected void spawnMagicCircle(int count, float range, Entity entity, ServerLevel level)
	{
		double pivot = entity.getRandom().nextDouble() * Math.PI;
		SimpleParticleType simpleParticleType = getChargedParticle();
		for(double i=0; i<count; i++)
		{
			level.sendParticles(i%4>0 ? simpleParticleType : ParticleTypes.FIREWORK,
				entity.getX() + Math.cos((i+pivot)/count * 2.0 * Math.PI) * range,
				entity.getY() + 0.5,
				entity.getZ() + Math.sin((i+pivot)/count * 2.0 * Math.PI) * range,
				1, 0.0, 0.5, 0.0, 0.0);
		}
	}

	@Override
	public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack itemStack, final int ticksRemaining) {
		super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
		int chargeTime = this.getUseDuration(itemStack, livingEntity) - ticksRemaining;
		float range = getRange(getPowerForTime(chargeTime));

		if (chargeTime == getCharge1time()) {
			level.playSound(null, livingEntity.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 2f, 1f);
			if(level instanceof ServerLevel serverLevel)
				spawnMagicCircle(27, range, livingEntity, serverLevel);
		}
		else if(chargeTime == getCharge2time()) {
			level.playSound(
				null,
				livingEntity.getX(),
				livingEntity.getY(),
				livingEntity.getZ(),
				SoundEvents.BELL_BLOCK,
				SoundSource.NEUTRAL,
				0.5F,
				0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
			);
			if(level instanceof ServerLevel serverLevel)
				spawnMagicCircle(36, range, livingEntity, serverLevel);
		}
		else if(chargeTime == getCharge3time()) {
			level.playSound(
				null,
				livingEntity.getX(),
				livingEntity.getY(),
				livingEntity.getZ(),
				SoundEvents.BELL_BLOCK,
				SoundSource.NEUTRAL,
				0.5F,
				0.4F / (level.getRandom().nextFloat() * 0.4F + 0.6F)
			);
			if(level instanceof ServerLevel serverLevel)
				spawnMagicCircle(45, range, livingEntity, serverLevel);
		}
		else if(chargeTime == getCharge4time()) {
			level.playSound(
				null,
				livingEntity.getX(),
				livingEntity.getY(),
				livingEntity.getZ(),
				SoundEvents.BELL_BLOCK,
				SoundSource.NEUTRAL,
				0.5F,
				0.4F / (level.getRandom().nextFloat() * 0.4F + 0.4F)
			);
			if(level instanceof ServerLevel serverLevel)
				spawnMagicCircle(54, range, livingEntity, serverLevel);
		}
		else if(chargeTime == getCharge5time()) {
			level.playSound(
				null,
				livingEntity.getX(),
				livingEntity.getY(),
				livingEntity.getZ(),
				SoundEvents.BELL_BLOCK,
				SoundSource.NEUTRAL,
				1F,
				0.4F / (level.getRandom().nextFloat() * 0.4F + 0.2F)
			);
			if(level instanceof ServerLevel serverLevel)
				spawnMagicCircle(63, range, livingEntity, serverLevel);
		}
		else if ((chargeTime) % 2 == 0 && level instanceof ServerLevel serverLevel)
		{
			spawnMagicCircle(10, range, livingEntity, serverLevel);
		}
	}

	public boolean releaseUsing(final ItemStack itemStack, final Level level, final LivingEntity player, final int remainingTime) {
		level.playSound(
			null,
			player.getX(),
			player.getY(),
			player.getZ(),
			getCastSound(),
			SoundSource.NEUTRAL,
			0.5F,
			1F
		);
		if (level instanceof ServerLevel serverLevel) {
			int timeHeld = this.getUseDuration(itemStack, player) - remainingTime;
			int power = getPowerForTime(timeHeld);
			float range = getRange(power);
			float healingPotency = getHealingPotency(power);

			for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(range)))
			{
				if(entity.distanceToSqr(player) < range * range) {
					Affect(entity, player, serverLevel, healingPotency);
				}
			}
			spawnMagicCircle(64, range, player, serverLevel);
			spawnMagicCircle(16, range/2, player, serverLevel);
			spawnMagicCircle(8, range/4, player, serverLevel);
		}

		if(player instanceof Player person) {
			person.awardStat(Stats.ITEM_USED.get(this));
			itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
			InteractionHand hand = person.getUsedItemHand();
			itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
		}
		if(itemStack.getOrDefault(BFDataComponents.SILENCE_TICKS,0) > 0)
		{
			MobEffectInstance instance = new MobEffectInstance(BFEffects.SILENCE,  itemStack.getOrDefault(BFDataComponents.SILENCE_TICKS,0), 0, false, true, true);
			player.addEffect(instance);
		}
		return true;
	}

	//timeheld -> time spent charging, in ticks
	public int getPowerForTime(final int timeHeld) {
		int power = 0;
		if(timeHeld > getCharge1time()) {
			power++;
			if (timeHeld > getCharge2time()) {
				power++;
				if (timeHeld > getCharge3time()) {
					power++;
					if (timeHeld > getCharge4time()) {
						power++;
						if (timeHeld > getCharge5time()) {
							power++;
						}
					}
				}
			}
		}
		return power;
	}

	public float getRange(int power) {
		switch (power) {
			case 0:
				return 4f;
			case 1:
				return 8F;
			case 2:
				return 10F;
			case 3:
				return 12F;
			case 4:
				return 14F;
			case 5:
				return 16F;
			default:
				return 4F;
		}
	}

	public float getHealingPotency(int power) {
		switch (power) {
			case 0:
				return 2f;
			case 1:
				return 6F;
			case 2:
				return 9F;
			case 3:
				return 12F;
			case 4:
				return 15F;
			case 5:
				return 18F;
			default:
				return 1F;
		}
	}

	public void Affect(LivingEntity entity, LivingEntity owner, ServerLevel serverLevel, float health) {
		//deal damage if enemy is undead and you are using healing magic
		//deal damage if enemy is normal and you are using harming magic
		if(entity.isInvertedHealAndHarm() == isHealing)
		{
			DamageSource damageSource = owner.damageSources().indirectMagic(entity,owner);
			if(entity.hurtServer(serverLevel, damageSource, health * damageMultiplier)) {
				EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
				for (int count = 0; count < 3; count++) {
					serverLevel.sendParticles(getDamageParticle(),
						entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.02, 0.02, 0.02, 0.0);
				}
			}
		}
		else
		{
			entity.playSound(BFSounds.HEAL, 2f, 1F);
			MobEffectInstance instance = new MobEffectInstance(MobEffects.GLOWING,  10, 0, false, false, false);
			entity.addEffect(instance,owner);
			entity.heal(health);
			for(int count = 0; count < 3; count++) {
				serverLevel.sendParticles(getHealParticle(),
					entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.02, 0.02, 0.02, 0.0);
			}
		}
	}


	public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
		return 72000;
	}

	public ItemUseAnimation getUseAnimation(final ItemStack itemStack) {
		return ItemUseAnimation.BOW;
	}
}
