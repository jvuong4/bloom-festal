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

public class GreatHarmingFestal extends GreatStaff {
	//harming festal is weaker overall but deals more damage
	public GreatHarmingFestal(final Properties properties) {
		super(properties);
		isHealing = false;
		damageMultiplier = 1.0F;
	}

	protected SimpleParticleType getChargedParticle() {return BFParticles.HARM_PETALS_PARTICLE;}

	@Override
	public float getHealingPotency(int power) {
		switch (power) {
			case 0:
				return 1.5f;
			case 1:
				return 4.5F;
			case 2:
				return 6.75F;
			case 3:
				return 9F;
			case 4:
				return 11.25F;
			case 5:
				return 13.5F;
			default:
				return 1.5F;
		}
	}
}
