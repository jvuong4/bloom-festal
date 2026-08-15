package io.github.jvuong4.bloomfestal.item.ProjectileItems;

import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.HealthOrbs.HealOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.item.ProjectileMagicItem;
import io.github.jvuong4.bloomfestal.registry.BFDataComponents;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class HealingStaff extends ProjectileMagicItem {
	protected int healingPotency = 0;

	public HealingStaff(final Properties properties) {
		super(properties);
	}

	@Override
	protected SimpleParticleType getChargedParticle() {
		return ParticleTypes.CHERRY_LEAVES;
	}

	@Override
	protected MagicalOrb getOrb(ServerLevel level, LivingEntity player, Vec3 direction, double d) {
		return new HealOrb(level, player, (direction.normalize()).scale(d));
	}

	@Override
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
			entity.setCharge(getPowerForTime(timeHeld) + healingPotency);
			entity.setPos(player.getX() + viewVector.x, player.getEyeY() + viewVector.y, entity.getZ() + viewVector.z);
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
}
