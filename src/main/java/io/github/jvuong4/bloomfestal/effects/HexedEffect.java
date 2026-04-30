package io.github.jvuong4.bloomfestal.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class HexedEffect extends MobEffect {
	public HexedEffect() {
		// category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
		// color: int - Color is the color assigned to the effect (in RGB)
		super(MobEffectCategory.HARMFUL, 0x736156);
	}

	// Called every tick to check if the effect can be applied or not
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return false;
	}

	// Called when the effect is applied.
	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		return super.applyEffectTick(level, entity, amplifier);
	}
}
