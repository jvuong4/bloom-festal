package io.github.jvuong4.bloomfestal.item;

import io.github.jvuong4.bloomfestal.entity.RewarpOrb;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class RewarpFestal extends Item {
	public RewarpFestal(final Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if(player.hasEffect(BFEffects.SILENCE))
		{
			level.playLocalSound(player,SoundEvents.SHIELD_BLOCK.value(),SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			return InteractionResult.FAIL;
		}
		ItemStack itemStack = player.getItemInHand(hand);
		level.playSound(
			null,
			player.getX(),
			player.getY(),
			player.getZ(),
			SoundEvents.EVOKER_CAST_SPELL,
			SoundSource.NEUTRAL,
			0.5F,
			0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
		);
		if (level instanceof ServerLevel serverLevel) {
			double d = 20.0;
			Vec3 viewVector = player.getViewVector(1.0F);
			Vec3 direction = new Vec3(viewVector.x, viewVector.y, viewVector.z);
			RewarpOrb entity = new RewarpOrb(level, player, (direction.normalize()).scale(d));
			entity.setPos(player.getX() + viewVector.x, player.getY(0.5) + 0.5, entity.getZ() + viewVector.z);
			Projectile.spawnProjectile(entity, serverLevel, itemStack);


			double end = 81.0;
			double pivot = player.getRandom().nextDouble();
			double range = 4;
			for(double i=0; i<end; i++)
			{
				serverLevel.sendParticles(i%3==0 ? ParticleTypes.PORTAL : i%3==1 ? ParticleTypes.REVERSE_PORTAL : ParticleTypes.WITCH,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * range,
					player.getY() + 0.5,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * range,
					1, 0.0, 0.5, 0.0, 0.0);
			}
			end = 16.0;
			pivot = player.getRandom().nextDouble();
			for(double i=0; i<end; i++)
			{
				serverLevel.sendParticles(i%2>0 ? ParticleTypes.PORTAL : ParticleTypes.REVERSE_PORTAL,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * range/2.0,
					player.getY() + 1,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * range/2.0,
					1, 0.0, 0.5, 0.0, 0.0);
			}
			end = 8.0;
			pivot = player.getRandom().nextDouble();
			for(double i=0; i<end; i++)
			{
				serverLevel.sendParticles(i%2>0 ? ParticleTypes.PORTAL : ParticleTypes.REVERSE_PORTAL,
					player.getX() + Math.cos((i+pivot)/end * 2.0 * Math.PI) * range/4.0,
					player.getY() + 1,
					player.getZ() + Math.sin((i+pivot)/end * 2.0 * Math.PI) * range/4.0,
					1, 0.0, 0.5, 0.0, 0.0);
			}
		}
		MobEffectInstance instance = new MobEffectInstance(BFEffects.SILENCE,  20, 0, false, true, true);
		player.addEffect(instance);
		player.awardStat(Stats.ITEM_USED.get(this));
		itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
		itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
		return InteractionResult.SUCCESS;
	}
}
