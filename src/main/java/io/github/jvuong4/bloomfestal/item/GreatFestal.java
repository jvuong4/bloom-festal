package io.github.jvuong4.bloomfestal.item;

import io.github.jvuong4.bloomfestal.entity.HealOrb;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.world.level.block.entity.BeaconBlockEntity.playSound;

public class GreatFestal extends Item {
	protected static double range = 16;
	protected float healingPotency = 8.0f;


	public GreatFestal(final net.minecraft.world.item.Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if(player.hasEffect(BFEffects.SILENCE))
		{
			level.playLocalSound(player,SoundEvents.SHIELD_BLOCK.value(),SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			return InteractionResult.FAIL;
		}
		ItemStack itemStack = player.getItemInHand(hand);
		level.playSound(
			null,
			player.getX(),
			player.getY(),
			player.getZ(),
			BFSounds.HEAL,
			SoundSource.NEUTRAL,
			0.5F,
			1F
		);
		if (level instanceof ServerLevel serverLevel) {
			for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(range)))
			{
				if(entity.distanceToSqr(player) < range * range) {
					if (entity.isInvertedHealAndHarm()) {
						DamageSource damageSource = player.damageSources().indirectMagic(entity, player);
						if (!entity.hurtServer(serverLevel, damageSource, healingPotency)) {
						} else {
							EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
							for(int count = 0; count < 3; count++) {
								serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
									entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.02, 0.02, 0.02, 0.0);
							}
						}
					}
					else {
						entity.heal(healingPotency);
						for(int count = 0; count < 3; count++) {
							serverLevel.sendParticles(ParticleTypes.HEART,
								entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.02, 0.02, 0.02, 0.0);
						}
					}
				}
			}
			double end = 64.0;
			double pivot = player.getRandom().nextDouble();
			for(double i=0; i<end; i++)
			{
				serverLevel.sendParticles(i%4>0 ? ParticleTypes.CHERRY_LEAVES : ParticleTypes.FIREWORK,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * range,
					player.getY() + 0.5,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * range,
					1, 0.0, 0.5, 0.0, 0.0);
			}
			end = 16.0;
			pivot = player.getRandom().nextDouble();
			for(double i=0; i<end; i++)
			{
				serverLevel.sendParticles(i%3>0 ? ParticleTypes.CHERRY_LEAVES : ParticleTypes.FIREWORK,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * range/2.0,
					player.getY() + 1,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * range/2.0,
					1, 0.0, 0.5, 0.0, 0.0);
			}
			end = 6.0;
			pivot = player.getRandom().nextDouble();
			for(double i=0; i<end; i++)
			{
				serverLevel.sendParticles(i%2>0 ? ParticleTypes.CHERRY_LEAVES : ParticleTypes.FIREWORK,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * range/4.0,
					player.getY() + 1,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * range/4.0,
					1, 0.0, 0.5, 0.0, 0.0);
			}
		}
		MobEffectInstance instance = new MobEffectInstance(BFEffects.SILENCE,  80, 0, false, true, true);
		player.addEffect(instance);
		player.awardStat(Stats.ITEM_USED.get(this));
		itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
		itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
		return InteractionResult.SUCCESS;
	}
}
