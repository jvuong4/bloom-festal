package io.github.jvuong4.bloomfestal.item.ProjectileItems;

import io.github.jvuong4.bloomfestal.entity.HarmOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrb;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrbs.BolganoneOrb;
import io.github.jvuong4.bloomfestal.item.ProjectileMagicItem;
import io.github.jvuong4.bloomfestal.registry.BFEffects;
import io.github.jvuong4.bloomfestal.registry.BFParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class HarmingStaff extends ProjectileMagicItem {
	public HarmingStaff(final Properties properties) {
		super(properties);
	}

	@Override
	protected SimpleParticleType getChargedParticle() {
		return BFParticles.HARM_PETALS_PARTICLE;
	}

	@Override
	protected MagicalOrb getOrb(ServerLevel level, LivingEntity player, Vec3 direction, double d) {
		return new HarmOrb(level, player, (direction.normalize()).scale(d));
	}
}
