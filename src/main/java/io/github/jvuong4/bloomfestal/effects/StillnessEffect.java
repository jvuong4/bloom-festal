package io.github.jvuong4.bloomfestal.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class StillnessEffect extends MobEffect {
	private Vec3 pos;

	public StillnessEffect() {
		// category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
		// color: int - Color is the color assigned to the effect (in RGB)
		super(MobEffectCategory.HARMFUL, 0x8BAFE0);
	}

	@Override
	public void onEffectAdded(final LivingEntity mob, final int amplifier) {
		pos = mob.position();
		super.onEffectAdded(mob,amplifier);
	}

	// Called every tick to check if the effect can be applied or not
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	// Called when the effect is applied.
	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		if(pos == null)
			pos = entity.position();
		if(entity instanceof Player player)
		{
			entity.setPos(pos);
		}


		entity.setDeltaMovement(0, 0, 0);
		entity.setPosRaw(pos.x, pos.y, pos.z);

		return super.applyEffectTick(level, entity, amplifier);
	}
}

