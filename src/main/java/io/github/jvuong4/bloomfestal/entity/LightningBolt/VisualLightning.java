package io.github.jvuong4.bloomfestal.entity.LightningBolt;

import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class VisualLightning extends LightningBolt {

	public VisualLightning(EntityType<? extends LightningBolt> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick()
	{
		this.baseTick();
		if (this.life == 2) {
			if (this.level().isClientSide()) {
				this.level()
					.playLocalSound(
						this.getX(), this.getY(), this.getZ(), BFSounds.THUNDER, SoundSource.WEATHER, 1.0F, 0.8F + this.random.nextFloat() * 0.2F, false
					);
				this.level()
					.playLocalSound(
						this.getX(), this.getY(), this.getZ(), BFSounds.THUNDER, SoundSource.WEATHER, 0.2F, 0.5F + this.random.nextFloat() * 0.2F, false
					);
			} else {
			}
		}
		this.life--;
		if (this.life < 0) {
			if (this.flashes == 0) {
				/*
				if (this.level() instanceof ServerLevel) {
					List<Entity> viewers = this.level()
						.getEntities(
							this,
							new AABB(this.getX() - 15.0, this.getY() - 15.0, this.getZ() - 15.0, this.getX() + 15.0, this.getY() + 6.0 + 15.0, this.getZ() + 15.0),
							entityx -> entityx.isAlive() && !this.hitEntities.contains(entityx)
						);

					for (ServerPlayer player : ((ServerLevel)this.level()).getPlayers(playerx -> playerx.distanceTo(this) < 256.0F)) {
						CriteriaTriggers.LIGHTNING_STRIKE.trigger(player, this, viewers);
					}
				}
				 */

				this.discard();
			} else if (this.life < -this.random.nextInt(10)) {
				this.flashes--;
				this.life = 1;
				this.seed = this.random.nextLong();
			}
		}
	}



}
