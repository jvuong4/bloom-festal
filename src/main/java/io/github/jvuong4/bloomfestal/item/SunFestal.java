package io.github.jvuong4.bloomfestal.item;

import io.github.jvuong4.bloomfestal.entity.HealOrb;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class SunFestal extends HealingStaff {
	protected float healingPotency = 10.0f;
	protected int range = 6;

	public SunFestal(final Properties properties) {
		super(properties);
		super.healingPotency = this.healingPotency;
		super.range = this.range;
	}

}
