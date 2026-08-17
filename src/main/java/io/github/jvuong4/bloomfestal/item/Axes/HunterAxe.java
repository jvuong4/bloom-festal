package io.github.jvuong4.bloomfestal.item.Axes;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HunterAxe extends Item{
	public HunterAxe(final Item.Properties properties) {
		super(properties);
	}

	private boolean CanCriticalAttack(Player attacker)
	{
		return attacker.fallDistance > (double)0.0F && !attacker.onGround() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.isMobilityRestricted() && !attacker.isPassenger() && !attacker.isSprinting();
	}

	public float getAttackDamageBonus(final Entity victim, final float damage, final DamageSource damageSource) {
		//when attacking with a Hunter Axe, you have a 1% chance to deal triple damage.
		//this chance is increased to a 40% chance if it is a critical hit (minecraft mechanics)
		//however, this chance is reduced for every point of luck the target has

		float baseChance = 0.10f;


		Entity owner = damageSource.getEntity();
		if(owner != null && owner instanceof Player player && victim instanceof LivingEntity) {
			float luck = player.getLuck();
			if(luck < 0)
			{
				luck = (int)-Math.sqrt(Math.abs(luck));
			}
			baseChance += luck * 0.01f;
			if(//player.getAttackStrengthScale(0.5F) > 0.9f &&
				CanCriticalAttack(player)) {
				baseChance += 0.30f;
				if(luck > 0)
				{
					baseChance += luck * 0.04f;
				}
			}
		}
		float chanceMultiplier = ((damage/5f)+0.5f)/1.5f;
		//penalty if you're not dealing a lot of damage
		if(chanceMultiplier < 1)
			chanceMultiplier *= chanceMultiplier;
		baseChance *= chanceMultiplier;




		if(victim instanceof Player player) {
			float luckDefense = player.getLuck();
			if(luckDefense < 0)
			{
				//attacking an enemy with lower luck almost always crits.
				luckDefense *= -(luckDefense-1) * 0.02f;
			}
			else {
				//attacking an enemy with higher luck almost never crits.
				luckDefense *= (luckDefense+1) * 0.02f;
			}

			if(player.getRandom().nextFloat() < baseChance - luckDefense)
			{
				Level level = victim.level();
				critEffects(victim,level);
				return damage * 2;
			}
		}
		else{
			if(victim.getRandom().nextFloat() < baseChance)
			{
				Level level = victim.level();
				critEffects(victim,level);
				return damage * 2;
			}
		}
		return 0.0F;
	}

	private void critEffects(Entity victim, Level level)
	{
		level.playSound(
			null,
			victim.getX(),
			victim.getY(),
			victim.getZ(),
			BFSounds.CRIT,
			SoundSource.NEUTRAL,
			16F,
			1F / (level.getRandom().nextFloat() * 0.2F + 0.9F)
		);
		if(level instanceof ServerLevel serverLevel)
		{
			int count = (int)(victim.getBbWidth() * 5);
			spawnMagicCircle(count,serverLevel,victim);
		}
	}

	private void spawnMagicCircle(int count, ServerLevel level,  Entity victim)
	{
		double pivot = victim.getRandom().nextDouble();
		float range = victim.getBbWidth() / 2f;
		float maxHeight = victim.getBbHeight()/2f;
		for(double i=0; i<count; i++)
		{
			for(double j=0; j<maxHeight; j++) {
				float bonusRange = (float)(maxHeight*(1+maxHeight/10f)-j)/maxHeight;
				level.sendParticles(BFParticles.HARM_PETALS_PARTICLE,
					victim.getX() + Math.cos((i + pivot) / count * 2.0 * Math.PI) * range * bonusRange,
					victim.getY(0.5) + j,
					victim.getZ() + Math.sin((i + pivot) / count * 2.0 * Math.PI) * range * bonusRange,
					1, 0.2, 0.5, 0.2, 100.0);
			}
		}
		level.sendParticles(BFParticles.HARM_PETALS_PARTICLE,
			victim.getX(),
			victim.getY(0.5),
			victim.getZ(),
			5, range, 0.5, range, 100.0);
	}

	@Override
	public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
		super.hurtEnemy(itemStack, mob, attacker);
	}

	@Override
	public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
		super.postHurtEnemy(itemStack, mob, attacker);
	}
}
