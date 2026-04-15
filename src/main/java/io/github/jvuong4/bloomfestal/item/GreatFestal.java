package io.github.jvuong4.bloomfestal.item;

import io.github.jvuong4.bloomfestal.entity.HealOrb;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.swing.*;
import java.util.List;

import static net.minecraft.world.level.block.entity.BeaconBlockEntity.playSound;

public class GreatFestal extends Item {
	protected static double range = 16;

	private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat()
		.ignoreInvisibilityTesting()
		.ignoreLineOfSight()
		.range(range);

	protected float healingPotency = 4.0f;


	public GreatFestal(final net.minecraft.world.item.Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		level.playSound(
			null,
			player.getX(),
			player.getY(),
			player.getZ(),
			SoundEvents.ALLAY_ITEM_GIVEN,
			SoundSource.NEUTRAL,
			0.5F,
			0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
		);
		if (level instanceof ServerLevel serverLevel) {
			Vec3 min = player.position().add(range*-1F,range*-0.3F,range*-1F);
			Vec3 max = player.position().add(range,range*0.3F,range);
			AABB box = new AABB(min,max);

			List<LivingEntity> entityList = ((ServerLevel) level).getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, player, box);
			for(LivingEntity entity: entityList)
			{
				if(entity.isInvertedHealAndHarm())
				{
					DamageSource damageSource = player.damageSources().indirectMagic(entity, player);
					entity.playSound(SoundEvents.BAMBOO_PLACE,0.5f,0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
					if (!entity.hurtServer(serverLevel, damageSource, healingPotency)) {
						//var7.setRemainingFireTicks(remainingFireTicks);
					} else {
						EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
					}
				}
				else
				{
					double xa = level.getRandom().nextGaussian() * 0.02;
					double ya = level.getRandom().nextGaussian() * 0.02;
					double za = level.getRandom().nextGaussian() * 0.02;
					entity.level().addParticle(ParticleTypes.HEART, entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0), xa, ya, za);
					//this.level().addParticle(ParticleTypes.HEART, var7.getX(), var7.getY() + 0.5, var7.getZ(), 0.0, 1.0, 0.0);
					//this.level().addParticle(ParticleTypes.HEART, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 1.0, 0.0);
					//entity.playSound(SoundEvents.ALLAY_ITEM_GIVEN,2f,0.4F / (level.getRandom().nextFloat() + 0.8F));

					entity.heal(healingPotency);
				}
			}

			double end = 16.0;
			for(double i=0; i<end; i++)
			{
				serverLevel.addParticle(ParticleTypes.CHERRY_LEAVES,
					true,
					true,
					player.getX() + Math.cos(i/end * 2.0 * Math.PI) * 16.0,
					player.getY() + 0.5,
					player.getZ() + Math.cos(i/end * 2.0 * Math.PI) * 16.0,
					0.0, 0.0, 0.0);
			}
			end = 8.0;
			for(double i=0; i<end; i++)
			{
				serverLevel.addParticle(ParticleTypes.CHERRY_LEAVES,
					false,
					false,
					player.getX() + Math.cos(i/end * 2.0 * Math.PI) * 8.0,
					player.getY() + 0.5,
					player.getZ() + Math.cos(i/end * 2.0 * Math.PI) * 8.0,
					0.0, 0.0, 0.0);
			}

		}
		player.awardStat(Stats.ITEM_USED.get(this));
		itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
		itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
		return InteractionResult.SUCCESS;
	}
}
