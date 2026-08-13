package io.github.jvuong4.bloomfestal.item;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.EclipseOrb;
import io.github.jvuong4.bloomfestal.registry.BFDataComponents;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ProjectileMagicItem extends Item {
	//time needed to charge for a strong attack
	protected int charge1time = 10;
	//time needed to charge for full power
	protected int charge2time = 30;

	public int getCharge1time() {
		return charge1time;
	}
	public int getCharge2time() {
		return charge2time;
	}

	protected SimpleParticleType getChargedParticle() {return ParticleTypes.ENCHANTED_HIT;}

	public ProjectileMagicItem(final net.minecraft.world.item.Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if(player.hasEffect(BFEffects.SILENCE))
		{
			level.playLocalSound(player,SoundEvents.SHIELD_BLOCK.value(),SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			return InteractionResult.FAIL;
		}
		else {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}
	}

	public void spawnChargedParticles(int power, ServerLevel serverLevel, Entity entity)
	{
		if(power > 1)
		{
			for(int count = 0; count < 10; count++) {
				serverLevel.sendParticles(getChargedParticle(),
					entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.1, 0.1, 0.1, 0.0);
			}
		}
		else if(power == 1)
		{
			for(int count = 0; count < 3; count++) {
				serverLevel.sendParticles(getChargedParticle(),
					entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.05, 0.1, 0.05, 0.0);
			}
		}
		else
		{
			serverLevel.sendParticles(getChargedParticle(),
				entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.05, 0.1, 0.05, 0.0);
		}
	}

	@Override
	public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack itemStack, final int ticksRemaining) {
		super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
		if (this.getUseDuration(itemStack, livingEntity) - ticksRemaining == getCharge1time()) {
			level.playSound(null, livingEntity.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 2f, 1f);
			if(level instanceof ServerLevel serverLevel)
				spawnChargedParticles(1, serverLevel, livingEntity);
		}
		else if(this.getUseDuration(itemStack, livingEntity) - ticksRemaining == getCharge2time()) {
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
				spawnChargedParticles(2, serverLevel, livingEntity);
		}
		else if (this.getUseDuration(itemStack, livingEntity) - ticksRemaining > getCharge2time() && (this.getUseDuration(itemStack, livingEntity) - ticksRemaining) % 7 == 0 && level instanceof ServerLevel serverLevel)
		{
			spawnChargedParticles(0, serverLevel, livingEntity);
		}
	}

	protected MagicalOrb getOrb(ServerLevel level, LivingEntity player, Vec3 direction, double d) {
		return new EclipseOrb(level, player, (direction.normalize()).scale(d));
	}

	public boolean releaseUsing(final ItemStack itemStack, final Level level, final LivingEntity player, final int remainingTime) {
		//ItemStack itemStack = player.getItemInHand(hand);
		level.playSound(
			null,
			player.getX(),
			player.getY(),
			player.getZ(),
			SoundEvents.EVOKER_CAST_SPELL,
			SoundSource.NEUTRAL,
			0.5F,
			0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
		);
		if (level instanceof ServerLevel serverLevel) {
			double d = 20.0;
			Vec3 viewVector = player.getViewVector(1.0F);
			Vec3 direction = new Vec3(viewVector.x, viewVector.y, viewVector.z);
			MagicalOrb entity = getOrb(serverLevel, player, direction, d);
			int timeHeld = this.getUseDuration(itemStack, player) - remainingTime;
			entity.setCharge(getPowerForTime(timeHeld));
			entity.setPos(player.getX() + viewVector.x, player.getEyeY(), entity.getZ() + viewVector.z);
			Projectile.spawnProjectile(entity, serverLevel, itemStack);

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
			}
		}
		return power;
	}


	public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
		return 72000;
	}

	public ItemUseAnimation getUseAnimation(final ItemStack itemStack) {
		return ItemUseAnimation.BOW;
	}


}


