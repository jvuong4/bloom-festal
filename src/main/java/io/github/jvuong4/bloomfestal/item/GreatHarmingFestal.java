package io.github.jvuong4.bloomfestal.item;

import io.github.jvuong4.bloomfestal.registry.BFEffects;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class GreatHarmingFestal extends Item {
	protected static double range = 8;

	private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat()
		.ignoreInvisibilityTesting()
		.ignoreLineOfSight()
		.range(range);

	protected float healingPotency = 8.0f;


	public GreatHarmingFestal(final Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if(player.hasEffect(BFEffects.SILENCE))
		{
			level.playSound(
				null,
				player.getX(),
				player.getY(),
				player.getZ(),
				SoundEvents.SHIELD_BLOCK,
				SoundSource.NEUTRAL,
				0.5F,
				0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
			);
			return InteractionResult.FAIL;
		}
		ItemStack itemStack = player.getItemInHand(hand);
		level.playSound(
			null,
			player.getX(),
			player.getY(),
			player.getZ(),
			SoundEvents.TRIDENT_THUNDER.value(),
			SoundSource.NEUTRAL,
			0.5F,
			1F
		);
		if (level instanceof ServerLevel serverLevel) {
			for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(range)))
			{
				if(entity.distanceToSqr(player) < range * range) {
					if (!entity.isInvertedHealAndHarm()) {
						DamageSource damageSource = player.damageSources().indirectMagic(entity, player);
						if (!entity.hurtServer(serverLevel, damageSource, healingPotency)) {
						} else {
							EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
						}
					}
					else {
						entity.heal(healingPotency);
					}
				}
			}
		}
		else
		{
			for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(range)))
			{
				if(entity.distanceToSqr(player) < range * range) {
					if (entity.isInvertedHealAndHarm()) {
						for(int i = 0; i < 3; i++)
						{
							double xa = level.getRandom().nextGaussian() * 0.02;
							double ya = level.getRandom().nextGaussian() * 0.02;
							double za = level.getRandom().nextGaussian() * 0.02;
							entity.level().addParticle(ParticleTypes.SCULK_SOUL, entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0), xa, ya, za);
						}
					} else {
						for(int i = 0; i < 3; i++)
						{
							double xa = level.getRandom().nextGaussian() * 0.02;
							double ya = level.getRandom().nextGaussian() * 0.02;
							double za = level.getRandom().nextGaussian() * 0.02;
							entity.level().addParticle(ParticleTypes.DAMAGE_INDICATOR, entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0), xa, ya, za);
						}
					}
				}
			}



			double end = 32.0;
			double pivot = player.getRandom().nextDouble();
			for(double i=0; i<end; i++)
			{
				level.addParticle(BFParticles.HARM_PETALS_PARTICLE,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * 8.0,
					player.getY() + 0.5,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * 8.0,
					0.0, 0.5, 0.0);
			}
			end = 8.0 * 2;
			pivot = player.getRandom().nextDouble();
			for(double i=0; i<end; i++)
			{
				SimpleParticleType particle = i % 2 == 0 ? BFParticles.HARM_PETALS_PARTICLE : ParticleTypes.SOUL_FIRE_FLAME;

				level.addParticle(particle,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * 4.0,
					player.getY() + 1,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * 4.0,
					0.0, 0.5-(i%2 * 0.5), 0.0);
			}
		}
		MobEffectInstance instance = new MobEffectInstance(BFEffects.SILENCE,  20, 0, false, true, true);
		player.addEffect(instance);
		player.awardStat(Stats.ITEM_USED.get(this));
		itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
		itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
		return InteractionResult.SUCCESS;
	}
}
