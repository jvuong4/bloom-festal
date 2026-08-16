package io.github.jvuong4.bloomfestal.item.Axes;

import io.github.jvuong4.bloomfestal.BloomFestal;
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

		float baseChance = 0.01f;

		Entity owner = damageSource.getEntity();
		if(owner != null && owner instanceof Player player && victim instanceof LivingEntity) {
			if(//player.getAttackStrengthScale(0.5F) > 0.9f &&
				CanCriticalAttack(player)) {
				baseChance = 0.40f + player.getLuck() * 0.05f;
			}
		}
		float chanceMultiplier = ((damage/5f)+0.5f)/1.5f;
		//penalty if you're not dealing a lot of damage
		if(chanceMultiplier < 1)
			chanceMultiplier *= chanceMultiplier;
		baseChance *= chanceMultiplier;




		if(victim instanceof Player player) {
			if(player.getRandom().nextFloat() < baseChance - player.getLuck() * (player.getLuck()+1) * 0.02f)
			{
				Level level = victim.level();
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

				return damage * 2;
			}
		}
		else{
			if(victim.getRandom().nextFloat() < baseChance)
			{
				Level level = victim.level();
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

				return damage * 2;
			}
		}
		return 0.0F;
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
