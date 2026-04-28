package io.github.jvuong4.bloomfestal.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BloomEffect extends MobEffect {
	protected SimpleParticleType BloomParticle = ParticleTypes.HEART;
	protected double yElevation = 0.0;

	public BloomEffect(SimpleParticleType particle, double y) {
		// category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
		// color: int - Color is the color assigned to the effect (in RGB)
		super(MobEffectCategory.NEUTRAL, 0xe9b8b3);
		BloomParticle = particle;
		yElevation = y;
	}

	// Called every tick to check if the effect can be applied or not
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		// In our case, we just make it return true so that it applies the effect every tick
		return true;
	}

	// Called when the effect is applied.
	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		if(!(entity.level() instanceof ServerLevel))
			for(int i = 0; i < 2; i++)
			{
				double xa = level.getRandom().nextGaussian() * 0.02;
				double ya = level.getRandom().nextGaussian() * 0.02;
				double za = level.getRandom().nextGaussian() * 0.02;
				entity.level().addParticle(BloomParticle, entity.getRandomX(1.0), entity.getRandomY() + 0.5 + yElevation, entity.getRandomZ(1.0), xa, ya, za);
			}
		return super.applyEffectTick(level, entity, amplifier);
	}
}
