package io.github.jvuong4.bloomfestal.entity;

import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HealOrb extends Fireball {
	private float range = 8.0F;
	private float potency = 4.0F;


	public HealOrb(final EntityType<? extends HealOrb> type, final Level level) {
		super(type, level);
		setRemainingFireTicks(0);
	}


	public HealOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, mob, direction, level);
		setRemainingFireTicks(0);
	}

	public HealOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.HEAL_ORB, x, y, z, direction, level);
		setRemainingFireTicks(0);
	}

	public void setStats(float r, float p)
	{
		range = r;
		potency = p;
	}

	@Override
	protected void onHitEntity(final EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			Entity var7 = hitResult.getEntity();
			Entity owner = this.getOwner();
			//no burning!
			//int remainingFireTicks = var7.getRemainingFireTicks();
			//var7.igniteForSeconds(5.0F);

			if(var7 instanceof LivingEntity mob)
			{
				if(mob.isInvertedHealAndHarm())
				{
					DamageSource damageSource = this.damageSources().indirectMagic(this, owner);
					if (!var7.hurtServer(serverLevel, damageSource, potency)) {
						//var7.setRemainingFireTicks(remainingFireTicks);
					} else {
						EnchantmentHelper.doPostAttackEffects(serverLevel, var7, damageSource);
					}
				}
				else
				{
					mob.heal(potency);
				}
			}


		}
	}

	@Override
	protected void onHitBlock(final BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		//do nothing!
	}

	@Override
	protected void onHit(final HitResult hitResult) {
		super.onHit(hitResult);
		if (!this.level().isClientSide()) {
			this.discard();
		}
	}

}
