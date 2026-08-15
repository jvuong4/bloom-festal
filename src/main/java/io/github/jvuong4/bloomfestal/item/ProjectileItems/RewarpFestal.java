package io.github.jvuong4.bloomfestal.item.ProjectileItems;

import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.RewarpOrb;
import io.github.jvuong4.bloomfestal.item.ProjectileMagicItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RewarpFestal extends ProjectileMagicItem {
	public RewarpFestal(final Properties properties) {
		super(properties);
	}

	@Override
	public int getCharge1time() {
		return 40;
	}
	@Override
	public int getCharge2time() {
		return 80;
	}

	@Override
	protected SimpleParticleType getChargedParticle() {
		return ParticleTypes.WITCH;
	}

	@Override
	protected MagicalOrb getOrb(ServerLevel level, LivingEntity player, Vec3 direction, double d) {
		return new RewarpOrb(level, player, (direction.normalize()).scale(d));
	}

	private void spawnMagicCircle(int count, int range, Entity entity, ServerLevel level)
	{
		double pivot = entity.getRandom().nextDouble();
		for(double i=0; i<count; i++)
		{
			level.sendParticles(i%3==0 ? ParticleTypes.PORTAL : i%3==1 ? ParticleTypes.REVERSE_PORTAL : ParticleTypes.WITCH,
				entity.getX() + Math.cos((i+pivot * Math.PI)/count * 2.0 * Math.PI) * range,
				entity.getY() + 0.5,
				entity.getZ() + Math.sin((i+pivot * Math.PI)/count * 2.0 * Math.PI) * range,
				1, 0.0, 0.5, 0.0, 0.0);
		}
	}

	@Override
	public void spawnChargedParticles(int power, ServerLevel serverLevel, Entity entity)
	{
		if(power > 1)
		{
			spawnMagicCircle(9,5,entity,serverLevel);
			spawnMagicCircle(6,3,entity,serverLevel);
		}
		else if(power == 1)
		{
			spawnMagicCircle(6,4,entity,serverLevel);
			spawnMagicCircle(4,2,entity,serverLevel);
		}
		else
		{
			//do nothing teehee
		}
	}

	@Override
	public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack itemStack, final int ticksRemaining) {
		super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
		if (this.getUseDuration(itemStack, livingEntity) - ticksRemaining > getCharge2time()) {
			if(level instanceof ServerLevel serverLevel)
				spawnChargedParticles(2, serverLevel, livingEntity);
		}
		else if(this.getUseDuration(itemStack, livingEntity) - ticksRemaining > getCharge1time()) {
			if(level instanceof ServerLevel serverLevel)
				spawnChargedParticles(1, serverLevel, livingEntity);
		}
	}
}
