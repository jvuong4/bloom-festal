package io.github.jvuong4.bloomfestal.entity.MagicalOrbs;

import io.github.jvuong4.bloomfestal.compat.DualStanceCompat;
import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class RewarpOrb extends MagicalOrb {
	private int age = 0;
	private float teleportRadius = 4f;


	public RewarpOrb(final EntityType<? extends RewarpOrb> type, final Level level) {
		super(type, level);
	}


	public RewarpOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.REWARP_ORB, level, mob, direction);
	}

	public RewarpOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.REWARP_ORB, level, x, y, z, direction);
	}

	@Override
	public void setCharge(int val)
	{
		super.setCharge(val);
		setStats();
	}

	public void setStats()
	{
		age = 0;
		switch(charge)
		{
			case 0:
				particleSpawnChance = 0.25F;
				teleportRadius = 0F;
				accelerationPower = 0.8F;
				range = 7;
				break;
			case 1:
				particleSpawnChance = 0.5F;
				teleportRadius = 4F;
				accelerationPower = 0.9F;
				range = 9;
				break;
			case 2:
				particleSpawnChance = 1F;
				teleportRadius = 5F;
				accelerationPower = 1F;
				range = 11;
				break;
			default:
				charge = 0;
				particleSpawnChance = 0.25F;
				teleportRadius = 0F;
				accelerationPower = 0.8F;
				range = 7;
				break;
		}


	}

	@Override
	protected void createParticleTrail() {
		ParticleOptions trailParticle = this.getTrailParticle();
		Vec3 position = this.position();
		if (trailParticle != null) {
			this.level().addParticle(trailParticle, position.x, position.y, position.z, 0.0, 0.0, 0.0);
			if(this.level().getRandom().nextFloat() < particleSpawnChance)
			{
				Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
				this.level().addParticle(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 0.0, 0.0, 0.0);
			}
		}
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.PORTAL;
	}

	public @Nullable Entity getOwner() {
		if (this.owner != null) {
			Level var2 = this.level();
			if (var2 instanceof ServerLevel) {
				ServerLevel serverLevel = (ServerLevel)var2;
				return (Entity)this.owner.getEntity(serverLevel, Entity.class);
			}
		}
		return super.getOwner();
	}

	private static @Nullable Entity findOwnerIncludingDeadPlayer(final ServerLevel serverLevel, final UUID uuid) {
		Entity owner = serverLevel.getEntityInAnyDimension(uuid);
		return (Entity)(owner != null ? owner : serverLevel.getServer().getPlayerList().getPlayer(uuid));
	}

	private static boolean isAllowedToTeleportOwner(final Entity owner, final Level newLevel) {
		if (owner.level().dimension() == newLevel.dimension()) {
			if (!(owner instanceof LivingEntity)) {
				return owner.isAlive();
			} else {
				LivingEntity livingOwner = (LivingEntity)owner;
				return livingOwner.isAlive() && !livingOwner.isSleeping();
			}
		} else {
			return owner.canUsePortal(true);
		}
	}

	private void playSound(final Level level, final Vec3 position) {
		level.playSound((Entity)null, position.x, position.y, position.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
	}


	public void rewarp()
	{
		for(int i = 0; i < 32; ++i) {
			this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * (double)2.0F, this.getZ(), this.random.nextGaussian(), (double)0.0F, this.random.nextGaussian());
		}

		Level var3 = this.level();
		if (var3 instanceof ServerLevel level) {
			if (!this.isRemoved()) {
				Entity owner = this.getOwner();
				if (owner != null && isAllowedToTeleportOwner(owner, level)) {
					Vec3 teleportPos = this.oldPosition();
					if (owner instanceof ServerPlayer) {
						ServerPlayer player = (ServerPlayer)owner;
						if (player.connection.isAcceptingMessages()) {

							if (this.isOnPortalCooldown()) {
								owner.setPortalCooldown();
							}

							//teleport all Players within 4 spaces of owner
							if(teleportRadius > 0)
							for (ServerPlayer target : this.level().getEntitiesOfClass(ServerPlayer.class, owner.getBoundingBox().inflate(teleportRadius))) {
								if (!(owner.distanceToSqr(target) > teleportRadius * teleportRadius)) {
									if(!(target.getUUID() == owner.getUUID()))
									{
											double xVariation = 2.0F * this.random.nextDouble() - (double)1.0F;
											double zVariation = 2.0F * this.random.nextDouble() - (double)1.0F;

											Entity newTarget = target.teleport(new TeleportTransition(level, teleportPos.add(xVariation,0,zVariation), target.getDeltaMovement(), target.getYRot(), target.getXRot(), TeleportTransition.DO_NOTHING));
											if (newTarget != null) {
												newTarget.resetFallDistance();
											}

											if (newTarget instanceof LivingEntity) {
												LivingEntity livingEntity = (LivingEntity)newTarget;
												livingEntity.resetCurrentImpulseContext();
											}
									}
								}
							}

							//TODO:: Attempt Dual Stance Compat
							if(FabricLoader.getInstance().isModLoaded("dual_stance"))
							{
								DualStanceCompat.teleportLinkedPlayer(player, this, level, teleportPos);
							}

							ServerPlayer newOwner = player.teleport(new TeleportTransition(level, teleportPos, Vec3.ZERO, 0.0F, 0.0F, Relative.union(new Set[]{Relative.ROTATION, Relative.DELTA}), TeleportTransition.DO_NOTHING));
							if (newOwner != null) {
								newOwner.resetFallDistance();
								newOwner.resetCurrentImpulseContext();
								//newOwner.hurtServer(player.level(), this.damageSources().enderPearl(), 5.0F);
							}

							this.playSound(level, teleportPos);
						}
					} else {
						//teleport all Players within 4 spaces of owner
						if(teleportRadius > 0)
						for (ServerPlayer target : this.level().getEntitiesOfClass(ServerPlayer.class, owner.getBoundingBox().inflate(teleportRadius))) {
							if (!(owner.distanceToSqr(target) > teleportRadius * teleportRadius)) {
								if(!(target.getUUID() == owner.getUUID()))
								{

										double xVariation = 2.0F * this.random.nextDouble() - (double)1.0F;
										double zVariation = 2.0F * this.random.nextDouble() - (double)1.0F;

										Entity newTarget = target.teleport(new TeleportTransition(level, teleportPos.add(xVariation,0,zVariation), target.getDeltaMovement(), target.getYRot(), target.getXRot(), TeleportTransition.DO_NOTHING));
										if (newTarget != null) {
											newTarget.resetFallDistance();
										}

										if (newTarget instanceof LivingEntity) {
											LivingEntity livingEntity = (LivingEntity)newTarget;
											livingEntity.resetCurrentImpulseContext();
										}
								}
							}
						}

						Entity newOwner = owner.teleport(new TeleportTransition(level, teleportPos, owner.getDeltaMovement(), owner.getYRot(), owner.getXRot(), TeleportTransition.DO_NOTHING));
						if (newOwner != null) {
							newOwner.resetFallDistance();
						}

						if (newOwner instanceof LivingEntity) {
							LivingEntity livingEntity = (LivingEntity)newOwner;
							livingEntity.resetCurrentImpulseContext();
						}

						this.playSound(level, teleportPos);
					}

					this.discard();
					return;
				}

				this.discard();
				return;
			}
		}
	}

	@Override
	protected void onHitEntity(final EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
	}

	@Override
	public void onLifeOver()
	{
		rewarp();
		this.discard();
	}


	@Override
	protected void onHitBlock(final BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
	}

	@Override
	protected void onHit(final HitResult hitResult) {
		super.onHit(hitResult);
		rewarp();
		if (!this.level().isClientSide()) {
			this.discard();
		}
	}

}
