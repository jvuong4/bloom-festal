package io.github.jvuong4.bloomfestal.item.GreatStaffs;

import io.github.jvuong4.bloomfestal.item.GreatStaff;
import io.github.jvuong4.bloomfestal.registry.BFDamageTypes;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class Blossom extends GreatStaff {
	//A berry-oriented staff...
	//A berry staff for the berry boy
	public Blossom(final net.minecraft.world.item.Item.Properties properties) {
		super(properties);
		isHealing = true;
		damageMultiplier = 0.35F;
	}

	protected SimpleParticleType getChargedParticle() {return BFParticles.BLOSSOM_PARTICLE;}

	@Override
	protected void spawnMagicCircle(int count, float range, Entity entity, ServerLevel level)
	{
		count *=4;
		double pivot = entity.getRandom().nextDouble() * Math.PI;
		SimpleParticleType simpleParticleType = getChargedParticle();
		for(double i=0; i<count; i++)
		{
			level.sendParticles(simpleParticleType,
				entity.getX() + Math.cos((i+pivot)/count * 2.0 * Math.PI) * range,
				entity.getY() + 0.5,
				entity.getZ() + Math.sin((i+pivot)/count * 2.0 * Math.PI) * range,
				1, 0.0, 0.5, 0.0, 0.0);
		}
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if(!player.getUUID().equals(UUID.fromString("3e640ed8-7e9f-469a-ab85-1cd6a91ca82a")))
		{
			MobEffectInstance instance = new MobEffectInstance(BFEffects.SILENCE,  99999, 0, true, true, true);
			player.addEffect(instance);
			level.playLocalSound(player, SoundEvents.FOX_BITE, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			if(level instanceof ServerLevel serverLevel) {
				DamageSource damageSource = player.damageSources().source(BFDamageTypes.BLOSSOM_REJECT_DAMAGE);
				float damage = player.getHealth() - 1;
				if(damage < 1)
				{
					damage = player.getMaxHealth();
				}
				player.hurtServer(serverLevel, damageSource, damage);
			}
			return InteractionResult.CONSUME;
		}
		else if(player.hasEffect(BFEffects.SILENCE))
		{
			level.playLocalSound(player, SoundEvents.SHIELD_BLOCK.value(), SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			return InteractionResult.FAIL;
		}
		else {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}
	}

	public boolean isFox(LivingEntity possibleFox)
	{
		//foxes must be unharmed
		if(possibleFox instanceof Fox)
		{
			return true;
		}
		if(possibleFox instanceof Player player)
		{
			if(player.getUUID().equals(UUID.fromString("3e640ed8-7e9f-469a-ab85-1cd6a91ca82a")))
			{
				return true;
			}
		}
		return false;
	}

	public void Affect(LivingEntity entity, LivingEntity owner, ServerLevel serverLevel, float health) {
		if(isFox(entity)) {
			entity.playSound(BFSounds.HEAL, 2f, 2F);
			MobEffectInstance instance = new MobEffectInstance(MobEffects.GLOWING,  10, 0, false, false, false);
			entity.addEffect(instance,owner);
			entity.heal(health);
			for(int count = 0; count < 3; count++) {
				serverLevel.sendParticles(getHealParticle(),
					entity.getRandomX(1.0), entity.getY(0.5), entity.getRandomZ(1.0), 1, 0.02, 0.02, 0.02, 0.0);
			}
		}
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

	@Override
	public float getHealingPotency(int power) {
		switch (power) {
			case 0:
				return 6f;
			case 1:
				return 18F;
			case 2:
				return 27F;
			case 3:
				return 36F;
			case 4:
				return 45F;
			case 5:
				return 54F;
			default:
				return 6F;
		}
	}
}
